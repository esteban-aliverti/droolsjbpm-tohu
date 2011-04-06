package org.tohu.test;


import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tohu.IntermediateTestFramework;
import org.tohu.test.enums.BrowserType;


public class InitialPageWithErrorsTest extends IntermediateTestFramework {

    private static final Logger logger = LoggerFactory.getLogger(InitialPageWithErrorsTest.class);   
    
    public InitialPageWithErrorsTest(BrowserType browser, String url, String isGUIBusyID) {
        super(browser, url, isGUIBusyID);
    } 
    
    @Before
    public void setUp() throws Exception {
        try {
            openURL(new String[] { "ajaxCall=initialErrorPage" });          
        }catch(Exception ex) {
            logger.error("problem in test setup method for browser: " + super.browser.getBrowserName(), ex);
            throw ex;
        }               
    }
    
    /**
     * Basic test to ensure validation kicks in straight away
     * on page load for the first page
     * @throws Exception
     */
    @org.junit.Test
    public void testInitialPageWithErrors() throws Exception {
        logger.debug("JQueryClientTest.testInitialPageWithErrors()");
        try {     
            // attempt to navigate to next page
            mouseDownControl("questionnaire1_action_1");
            // check an error alert was generated
            checkAlert("Not all mandatory questions have being answered");
            // check we are still on the same page of the app
            checkQuestionnaire(
                    "questionnaire1",
                    new String[] { "questionnaire1Style" },
                    "Questionnaire 1",  
                    new String[][] { new String[] {"group1", "Group 1"}, 
                                     new String[] {"group2", "Group 2"}, 
                                     new String[] {"group3", "Group 3"} });         
        }
        catch (Exception ex) {
            handleException("BaseJQueryClientTest.testInitialPageWithErrors()", ex);
        }       
    }
    
}
