package org.tohu;

import static org.junit.Assert.fail;
import static org.junit.Assume.assumeTrue;

import java.io.IOException;
import java.util.concurrent.locks.LockSupport;

import org.apache.commons.httpclient.HttpConnection;
import org.mortbay.http.HttpContext;
import org.mortbay.http.HttpServer;
import org.mortbay.http.SocketListener;
import org.mortbay.http.handler.ResourceHandler;
import org.mortbay.util.URI;
import org.openqa.selenium.server.RemoteControlConfiguration;
import org.openqa.selenium.server.SeleniumServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tohu.test.enums.BrowserType;

import com.thoughtworks.selenium.DefaultSelenium;


public class Browser {

    private static final Logger logger = LoggerFactory.getLogger(Browser.class);      

    /** The selenium browser handle */
    protected static DefaultSelenium selenium = null;
    /** The maximum number of milliseconds to wait for the AJAX call and subsequent processing to complete */
    protected static String processingTimeout = "10000";
    /** System time at which the test case started for debugging purposes */
    protected static long startTime = System.currentTimeMillis();
    /** Background thread to run the Selenium Server */
    protected static SeleniumServerThread seleniumThread = null;
    /** Background thread to run the Jerry Server */
    protected static JettyServerThread jettyThread = null; 
    
    public static DefaultSelenium getInstance(BrowserType browser, String urlString) throws Exception {
      newBrowser(browser, urlString);
      /*
      if (selenium == null) { 
        newBrowser(browser, urlString); 
      } else if (browserNameChanged(browser)) { 
        //stop(); 
        newBrowser(browser, urlString); 
      }
      */ 
      return selenium; 
    }  
    
    private static void newBrowser(BrowserType browser, String urlString) throws Exception { 
        if (!isHTTPServerRunning("localhost", 4444)) {
            //  Selenium Server not running on localhost:4444, start it as a separate thread.
            logger.debug("Starting Selenium Server");
            seleniumThread = new SeleniumServerThread(Thread.currentThread());
            seleniumThread.start();
            logger.debug("Waiting for Selenium Server");
            LockSupport.parkNanos(60000000000l);
            logger.debug("Selenium Server Started");
        }

        // Determine the Selenium base URL and if we need to start a Jetty Server.
        URI uri = new URI(urlString);
        // TODO Replace the above line with this one if Selenium ever allows a Jetty version
        //      more recent then 5.1, see http://jira.openqa.org/browse/SRC-176
        //HttpURI uri = new HttpURI(testURL);

        String baseURL = "http://localhost:8080/";
        if (uri.getScheme().equals("http")) {
            baseURL = "http://" + uri.getHost() + ":" + uri.getPort() + "/";
            if (!isHTTPServerRunning(uri.getHost(), uri.getPort())) {
                // HTTP Server not running there.
                if (uri.getHost().equals("localhost")) {
                    // Start Jetty Server as a separate thread.
                    logger.debug("Starting Jetty Server");
                    jettyThread = new JettyServerThread(Thread.currentThread(), uri.getPort());
                    jettyThread.start();
                    logger.debug("Waiting for Jetty Server");
                    LockSupport.parkNanos(60000000000l);
                    logger.debug("Jetty Server Started");
                }
                else {
                    // Nothing we can do Jetty Server only runs locally.
                    fail("No HTTP server running at " + baseURL);
                }
            }
        }
        logger.debug("baseURL=" + baseURL);
        logger.debug("Starting browser");
        
        selenium = new DefaultSelenium("localhost", 4444, browser.getBrowserName(), baseURL);
        try {
            selenium.setSpeed("200");
            selenium.start();
        }
        catch (Exception ex) {
            logger.debug("exception=" + ex);
            if (((BrowserType.SAFARI.equals(browser)) && ex.toString().contains(
                "java.lang.RuntimeException: Safari could not be found in the path!"))
                    || ((BrowserType.CHROME.equals(browser)) && ex.toString().contains(
                        "java.lang.RuntimeException: Chrome could not be found in the path!"))                
                    || ((BrowserType.FIREFOX.equals(browser)) && ex.toString().contains(
                        "java.lang.RuntimeException: Firefox could not be found in the path!"))
                    || ((BrowserType.IEXPLORER.equals(browser)) && ex.toString().contains(
                        "java.lang.RuntimeException: Internet Explorer could not be found in the path!"))) {
                // Browser not installed, treat this as a successful test.
                logger.debug("browser [" + browser.getBrowserName() + "] not installed.");
                assumeTrue(false);
            }
            throw ex;
        }
            
        logger.debug("done");        
    } 
  

    /**
     * Closes the browser and stops any background threads which were started in basicSetUp.
     * 
     * @throws Exception
     */
    public static void stop() throws Exception {
        try {
            selenium.stop();
            if (jettyThread != null) {
                logger.debug("Stopping Jetty Server");
                jettyThread.stopServer();
                jettyThread.join();
            }
            if (seleniumThread != null) {
                logger.debug("Stopping Selenium Server");
                seleniumThread.stopServer();
                seleniumThread.join();
            }
            logger.debug("Done");            
        } catch (Exception ex) {
            logger.error("error stopping selenium: ", ex);
            throw ex;
        }

    }

    
    /**
     * Returns true if there is an HTTP server running on the specified host and port.
     * @param host Host name.
     * @param port Port number.
     * @return boolean
     */
    private static boolean isHTTPServerRunning(String host, int port) {
        boolean retVal = false;
        try {
            HttpConnection connection = new HttpConnection(host, port);
            connection.open();
            retVal = true;
            connection.close();
        }
        catch (IOException ioe) {
            retVal = false;
        }
        return retVal;
    }

    /**
     * Thread class for running the Selenium Server on localhost:4444.
     */
    private static class SeleniumServerThread extends Thread {
        private SeleniumServer server = null;
        private Thread parentThread = null;

        public SeleniumServerThread(Thread parentThread) {
            this.parentThread = parentThread;
        }

        public void run() {
            try {
                RemoteControlConfiguration rcc = new RemoteControlConfiguration();
                // Uncomment for debugging.
                //rcc.setBrowserSideLogEnabled(true);
                //rcc.setDebugMode(true);
                //rcc.setLogOutFileName("SeleniumServer.log");
                server = new SeleniumServer(rcc);
                server.start();
            }
            catch (Exception ex) {
                logger.error("SeleniumServerThread.run()", ex);
            }
            finally {
                // Notify the parent thread that the server has finished starting up.
                LockSupport.unpark(parentThread);
            }
        }

        public void stopServer() throws Exception {
            server.stop();
        }
    }

    /**
     * Thread class for running a Jetty HTTP Server which serves up the test page and the
     * dummy xml files for the AJAX calls.
     */
    private static class JettyServerThread extends Thread {
        private HttpServer server = null;
        //private Server server = null;
        private Thread parentThread = null;
        private int port = 0;

        public JettyServerThread(Thread parentThread, int port) {
            this.parentThread = parentThread;
            this.port = port;
            logger.debug("port=" + port);
        }

        public void run() {
            try {
                server = new HttpServer();
                SocketListener listener = new SocketListener();
                listener.setPort(port);
                listener.setMinThreads(1);
                listener.setMaxThreads(5);
                server.addListener(listener);
                HttpContext context = server.addContext("/");
                context.setResourceBase(this.getClass().getResource("../../../test-classes/").toExternalForm());
                context.setRedirectNullPath(true);
                context.addWelcomeFile("test.html");
                logger.debug("serving " + context.getBaseResource());
                ResourceHandler handler = new ResourceHandler();
                handler.setAcceptRanges(true);
                handler.setDirAllowed(true);
                context.addHandler(handler);
                //context.addHandler(new NotFoundHandler());
                //server.setStopGracefully(true);
                // Uncomment for debugging.
                //server.setTrace(true);
                //NCSARequestLog log = new NCSARequestLog("JettyServer.log");
                //log.setExtended(true);
                //log.setLogCookies(true);
                //log.setLogLatency(true);
                //server.setRequestLog(log);
                server.start();

                // TODO Replace the above code with this if Selenium ever allows a Jetty version
                //      more recent then 5.1, see http://jira.openqa.org/browse/SRC-176
                //ResourceHandler handler = new ResourceHandler();
                //handler.setWelcomeFiles(new String[] { "test.html" });
                //handler.setResourceBase(this.getClass().getResource("../../../").toExternalForm());
                //logger.debug("JettyServerThread.run() serving " + handler.getBaseResource());
                //HandlerList handlers = new HandlerList();
                //handlers.setHandlers(new Handler[] { handler, new DefaultHandler() });
                //server = new Server(port);
                //server.setHandler(handlers);
                //server.start();
            }
            catch (Exception ex) {
                logger.error("JettyHandlerThread.run()", ex);
            }
            finally {
                // Notify the parent thread that the server has finished starting up.
                LockSupport.unpark(parentThread);
            }
        }

        public void stopServer() throws Exception {
            server.stop();
            server.destroy();
        }
    }
      
}
