package org.tohu.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tohu.IntermediateTestFramework;
import org.tohu.test.enums.BrowserType;


public class DateFieldTest extends IntermediateTestFramework {

    private static final Logger logger = LoggerFactory.getLogger(DateFieldTest.class);
       
    public DateFieldTest(BrowserType browser, String url, String isGUIBusyID) {
        super(browser, url, isGUIBusyID);
    }    
    
    @Before
    public void setUp() throws Exception {
        try {
            openURL(new String[] { "ajaxCall=initial" });            
        }catch(Exception ex) {
            logger.error("problem in test setup method for browser: " + super.browser.getBrowserName(), ex);
            throw ex;
        }               
    }
    
    // Test date field.
    @org.junit.Test
    public void testDateField() throws Exception {
        logger.debug("JQueryClientTest.testDateField()");
        try {
            setTextAnswer("item_1_3", "11111111");
            checkAlert("11111111 is not a valid date, please re-enter.");
            
            selenium.click("//div[@id='item_1_3']/img[@class='ui-datepicker-trigger']");
            assertTrue("item_1_3 date picker not visible", selenium.isVisible("//div[@id='ui-datepicker-div']/div"));
            
            setTextAnswer("item_1_3", "01/01/1990");
            assertEquals("item_1_3 wrong value", "02/01/1990", getTextAnswer("item_1_3"));
        }
        catch (Exception ex) {
            handleException("JQueryClientTest.testDateField()", ex);
        }
    }
}
