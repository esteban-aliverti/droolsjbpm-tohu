package org.tohu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tohu.test.DynamicPageTest;
import org.tohu.test.element.ElementFactory;
import org.tohu.test.element.Question;
import org.tohu.test.enums.BrowserType;
import org.tohu.test.enums.WidgetType;

import com.thoughtworks.selenium.DefaultSelenium;


/**
 * The Base Test class used for all regression tests
 * @author rb1317
 *
 */
@RunWith(Parameterized.class)  
public class BaseTestFramework {

    private static final Logger logger = LoggerFactory.getLogger(DynamicPageTest.class);
   
    protected Map<WidgetType, Question> elementMap;
    
    @Parameterized.Parameters   
    public static ArrayList<Object[]> data() { 
      ArrayList<Object []> list = new ArrayList<Object[]>();              
      list.add(new Object[]{BrowserType.IEXPLORER, "http://localhost:9999/unittest-web", "isGUIBusy"});         
      list.add(new Object[]{BrowserType.CHROME, "http://localhost:9999/unittest-web", "isGUIBusy"});                
      list.add(new Object[]{BrowserType.FIREFOX, "http://localhost:9999/unittest-web", "isGUIBusy"});      
      //list.add(new Object[]{BrowserType.SAFARI, "http://localhost:9999/unittest-web", "isGUIBusy"});   
      return list; 
    }  
    
    public BaseTestFramework() {
        super();
    }
    
    public BaseTestFramework(BrowserType browser, String url, String isGUIBusyID) {
        this();        
        this.browser = browser;
        this.isGUIBusyID = isGUIBusyID;
        this.urlString = url;
    }
    
    @Before
    public void openBrowser() throws Exception {
        try {            
            this.selenium = Browser.getInstance(browser, urlString);
            elementMap = new HashMap<WidgetType, Question>();
            for(WidgetType widget : WidgetType.values()) {
                Question element = ElementFactory.getQuestion(widget);
                element.setSelenium(selenium);
                elementMap.put(widget, element);          
            }            
            logger.debug("Opening browser="+browser.getBrowserName()+" with testURL="+ urlString);                        
        } catch(Exception ex) {
            logger.error("Error creating a selenium process for Browser: " + browser.getBrowserName(), ex);
            throw ex;
        }                   
    }
    
    @After
    public void closeBrowser() throws Exception {
        Browser.stop();
    }
    
    protected DefaultSelenium selenium;
    protected String urlString;
    protected String isGUIBusyID;
    protected BrowserType browser;
    
}
