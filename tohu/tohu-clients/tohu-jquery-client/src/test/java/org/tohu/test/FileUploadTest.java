package org.tohu.test;

import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tohu.IntermediateTestFramework;
import org.tohu.test.element.Question;
import org.tohu.test.element.data.FileUploadData;
import org.tohu.test.enums.BrowserType;
import org.tohu.test.enums.WidgetType;


public class FileUploadTest extends IntermediateTestFramework {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadTest.class);
       
    public FileUploadTest(BrowserType browser, String url, String isGUIBusyID) {
        super(browser, url, isGUIBusyID);
    } 
    
    @Before
    public void setUp() throws Exception {
        try {
            openURL(new String[] { "ajaxCall=fileupload" });         
        }catch(Exception ex) {
            logger.error("problem in test setup method for browser: " + super.browser.getBrowserName(), ex);
            throw ex;
        }               
    }
    
    // Test can upload a file.
    @org.junit.Test
    public void testFileUpload() throws Exception {
        logger.debug("JQueryClientTest.testFileUpload()");
        Question element;
        try {
            element = elementMap.get(WidgetType.FILE);
            FileUploadData.populateTestDataForFileUploadPage(element, "item_1_1");
            element.check();                        
        }
        catch (Exception ex) {
            handleException("JQueryClientTest.testFileUpload()", ex);
        }
    }
    
}
