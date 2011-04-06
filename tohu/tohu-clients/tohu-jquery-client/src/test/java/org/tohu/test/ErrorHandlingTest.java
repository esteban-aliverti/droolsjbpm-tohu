package org.tohu.test;

import java.util.Map;

import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tohu.IntermediateTestFramework;
import org.tohu.test.element.Question;
import org.tohu.test.enums.BrowserType;
import org.tohu.test.enums.WidgetType;


public class ErrorHandlingTest extends IntermediateTestFramework {
    
    private static final Logger logger = LoggerFactory.getLogger(ErrorHandlingTest.class);
  
    protected Map<WidgetType, Question> elementMap;
    
    public ErrorHandlingTest(BrowserType browser, String url, String isGUIBusyID) {
        super(browser, url, isGUIBusyID);
    } 
    
    @Before
    public void setUp() throws Exception {
        try {
            openURL(new String[] { "ajaxCall=errors" });         
        }catch(Exception ex) {
            logger.error("problem in test setup method for browser: " + super.browser.getBrowserName(), ex);
            throw ex;
        }               
    }
    
    // Test handling of errors.
    @org.junit.Test
    public void testErrorHandling() throws Exception {
        logger.debug("JQueryClientTest.testErrorHandling()");
        try {            
            setTextAnswer("error_type", "No ID");
            checkAlert("Fact undefined in the rule base is invalid: no id, revise the rule base."); 
            setTextAnswer("error_type", "Garbage");
            checkAlert("The rules server has returned invalid XML, contact support.");            
            setTextAnswer("error_type", "HTTP Error");
            checkAlert("An error has occured calling the rules server, refresh the page and try again.");       
            setTextAnswer("error_type", "Invalid Fact Type");
            checkAlert("An internal error has occured: unknown tagName: org.tohu.Whatsit, contact support.");
            setTextAnswer("error_type", "Invalid Answer Type");
            checkAlert("Fact item_1_1 in the rule base is invalid: unknown answerType: movie, revise the rule base.");
            setTextAnswer("error_type", "Duplicate");
            checkAlert("A duplicate of the error_type fact has been sent by the rules server, revise the rule base.");
            setTextAnswer("error_type", "No Parent");
            checkAlert("The item_1_1 fact does not have a parent, revise the rule base.");
         
        }
        catch (Exception ex) {
            handleException("JQueryClientTest.testErrorHandling()", ex);
        }
    }
}
