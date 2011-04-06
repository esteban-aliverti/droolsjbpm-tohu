package org.tohu.test;


import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tohu.IntermediateTestFramework;
import org.tohu.test.element.Question;
import org.tohu.test.element.data.InitialPageData;
import org.tohu.test.enums.BrowserType;
import org.tohu.test.enums.WidgetType;

import static org.junit.Assert.assertFalse;

public class InitialPageTest extends IntermediateTestFramework {

    private static final Logger logger = LoggerFactory.getLogger(InitialPageTest.class);
       
    public InitialPageTest(BrowserType browser, String url, String isGUIBusyID) {
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
    
    // Test all HTML elements of the initial page are present and correct.
    @org.junit.Test
    public void testInitialPage() throws Exception {
        logger.debug("JQueryClientTest.testInitialPage()");
        Question element;
        try {
            checkQuestionnaire(
                "questionnaire1",
                new String[] { "questionnaire1Style" },
                "Questionnaire 1",
                new String[][] { new String[] {"group1", "Group 1"}, 
                                 new String[] {"group2", "Group 2"}, 
                                 new String[] {"group3", "Group 3"} });
            checkGroup(
                "group1",
                new String[] { "group1Style" },
                "Group 1",
                new String[] { "item_1_1", "item_1_2", "group4", "item_1_3" });
            checkGroup(
                "group2",
                new String[] { "group2Style" },
                "Group 2",
                new String[] { "item_2_1", "item_2_2", "item_2_3" });
            checkGroup(
                "group3",
                new String[] { "group3Style" },
                "Group 3",
                new String[] { "item_3_1", "item_3_2", "item_3_3" });
            checkGroup(
                "group4",
                new String[] { "group4Style" },
                "Group 4",
                new String[] { "item_4_1", "item_4_3", "item_4_4", "item_4_5" });
            checkNote(
                WidgetType.IMAGE,
                "item_1_1",
                new String[] { "image", "item_1_1Style1", "item_1_1Style2" },
                "images/earth.jpg");
            checkNote(
                WidgetType.PARAGRAPH,
                "item_1_2",
                new String[] { "item_1_2Style" },
                "Item 1.2");
            
            element = elementMap.get(WidgetType.TEXT);
            InitialPageData.populateTestDataForInitialPageQuestion(element, "item_1_3");
            element.check();
            
            element = elementMap.get(WidgetType.TEXT);
            InitialPageData.populateTestDataForInitialPageQuestion(element, "item_2_1");
            element.check();            
            
            element = elementMap.get(WidgetType.TEXTAREA);
            InitialPageData.populateTestDataForInitialPageQuestion(element, "item_2_2");
            element.check();
            
            element = elementMap.get(WidgetType.RADIO_GROUP);
            InitialPageData.populateTestDataForInitialPageQuestion(element, "item_2_3");
            element.check();
                        
            element = elementMap.get(WidgetType.DROPDOWN);
            InitialPageData.populateTestDataForInitialPageQuestion(element, "item_3_1");
            element.check();
                        
            element = elementMap.get(WidgetType.DROPDOWN);
            InitialPageData.populateTestDataForInitialPageQuestion(element, "item_3_2");
            element.check();
                        
            element = elementMap.get(WidgetType.TEXT);
            InitialPageData.populateTestDataForInitialPageQuestion(element, "item_3_3");
            element.check();
                    
            element = elementMap.get(WidgetType.CHECKBOX);
            InitialPageData.populateTestDataForInitialPageQuestion(element, "item_4_1");
            element.check();
            
            assertFalse("placeholder displayed", isItemPresent("item_4_2"));
            
            element = elementMap.get(WidgetType.RADIO_BOOLEAN);
            InitialPageData.populateTestDataForInitialPageQuestion(element, "item_4_3");
            element.check();
                    
            element = elementMap.get(WidgetType.DROPDOWN);
            InitialPageData.populateTestDataForInitialPageQuestion(element, "item_4_4");
            element.check();
            
            element = elementMap.get(WidgetType.TEXT);
            InitialPageData.populateTestDataForInitialPageQuestion(element, "item_4_5");
            element.check();
                        
            element = elementMap.get(WidgetType.DROPDOWN);
            InitialPageData.populateTestDataForInitialPageQuestion(element, "item_4_6");
            element.check();
            
            checkControl(
                WidgetType.LINK,
                "questionnaire1_action_1",
                new String[] { "next" },
                "Next");
        }
        catch (Exception ex) {
            handleException("JQueryClientTest.testInitialPage()", ex);
        }
    }
    
}
