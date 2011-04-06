package org.tohu.test;


import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tohu.IntermediateTestFramework;
import org.tohu.test.element.Question;
import org.tohu.test.element.data.ReadOnlyPageData;
import org.tohu.test.enums.BrowserType;
import org.tohu.test.enums.WidgetType;


public class ReadOnlyPageTest extends IntermediateTestFramework {

    private static final Logger logger = LoggerFactory.getLogger(ReadOnlyPageTest.class);   
    
    public ReadOnlyPageTest(BrowserType browser, String url, String isGUIBusyID) {
        super(browser, url, isGUIBusyID);
    } 
    
    @Before
    public void setUp() throws Exception {
        try {
            openURL(new String[] { "ajaxCall=readonly" });         
        }catch(Exception ex) {
            logger.error("problem in test setup method for browser: " + super.browser.getBrowserName(), ex);
            throw ex;
        }               
    }
    
    // Test readonly questions.
    @org.junit.Test
    public void testReadOnlyPage() throws Exception {
        logger.debug("JQueryClientTest.testReadOnly()");
        Question element;
        try {
            element = elementMap.get(WidgetType.TEXT);
            ReadOnlyPageData.populateTestDataForReadOnlyPageQuestion(element, "item_1_1");
            element.check();

            element = elementMap.get(WidgetType.CHECKBOX);
            ReadOnlyPageData.populateTestDataForReadOnlyPageQuestion(element, "item_1_2");
            element.check();            

            element = elementMap.get(WidgetType.TEXT);
            ReadOnlyPageData.populateTestDataForReadOnlyPageQuestion(element, "item_1_3");
            element.check();            

            element = elementMap.get(WidgetType.TEXTAREA);
            ReadOnlyPageData.populateTestDataForReadOnlyPageQuestion(element, "item_1_4");
            element.check();            

            element = elementMap.get(WidgetType.RADIO_GROUP);
            ReadOnlyPageData.populateTestDataForReadOnlyPageQuestion(element, "item_1_5");
            element.check();            

            element = elementMap.get(WidgetType.DROPDOWN);
            ReadOnlyPageData.populateTestDataForReadOnlyPageQuestion(element, "item_1_6");
            element.check();            

            element = elementMap.get(WidgetType.RADIO_BOOLEAN);
            ReadOnlyPageData.populateTestDataForReadOnlyPageQuestion(element, "item_1_7");
            element.check();            

        }
        catch (Exception ex) {
            handleException("JQueryClientTest.testReadOnly()", ex);
        }
    }
    
}
