package org.tohu.test;

import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tohu.IntermediateTestFramework;
import org.tohu.test.element.Question;
import org.tohu.test.element.data.DynamicPageData;
import org.tohu.test.enums.BrowserType;
import org.tohu.test.enums.WidgetType;


public class DynamicPageTest extends IntermediateTestFramework {

    private static final Logger logger = LoggerFactory.getLogger(DynamicPageTest.class);      
    
    public DynamicPageTest(BrowserType browser, String url, String isGUIBusyID) {
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
    
    // Start with initial page and drive it around.
    @org.junit.Test
    public void testDynamicPage() throws Exception {
        logger.debug("Starting tests");
        try {
            Question element;
            selectDropDownAnswer("item_3_1", "0.2");
            assertFalse("item_2_1 not deleted", isItemPresent("item_2_1"));
            
            element = elementMap.get(WidgetType.RADIO_BOOLEAN);
            DynamicPageData.populateTestDataForDynamicPageQuestion(element, "item_4_3", false);
            element.check();
            
            element = elementMap.get(WidgetType.DROPDOWN);
            DynamicPageData.populateTestDataForDynamicPageQuestion(element, "item_3_1", false);
            element.check();          

            element = elementMap.get(WidgetType.TEXT);
            DynamicPageData.populateTestDataForDynamicPageQuestion(element, "item_3_1_1", false);
            element.check();
            
            element = elementMap.get(WidgetType.DROPDOWN);
            DynamicPageData.populateTestDataForDynamicPageQuestion(element, "item_3_2", false);
            element.check();
            
            checkGroup(
                "group3",
                new String[] { "group3Style" },
                "Group 3",
                new String[] { "item_3_1", "item_3_1_1", "item_3_2", "item_3_3" });
                            
            clickCheckboxAnswer("item_4_1");
            
            element = elementMap.get(WidgetType.CHECKBOX);
            DynamicPageData.populateTestDataForDynamicPageQuestion(element, "item_4_1", true);
            element.check();
                
            clickCheckboxAnswer("item_4_1");
            
            element = elementMap.get(WidgetType.CHECKBOX);
            DynamicPageData.populateTestDataForDynamicPageQuestion(element, "item_4_1", false);
            element.check();        
                            
            setTextAnswer("item_2_2", "Test Text");
            
            element = elementMap.get(WidgetType.TEXTAREA);
            DynamicPageData.populateTestDataForDynamicPageQuestion(element, "item_2_2", false);
            element.check();          

            setTextAnswer("item_2_1", "Test Text");
            
            element = elementMap.get(WidgetType.TEXT);
            DynamicPageData.populateTestDataForDynamicPageQuestion(element, "item_2_1", false);
            element.check();
            
            clickRadioButtonAnswer("item_2_3", "2");
            
            checkQuestionnaire(
                "questionnaire1",
                new String[] { "questionnaire1Style1", "questionnaire1Style2" },
                "Questionnaire 1a",
                new String[][] { 
                                new String[] {"group1", "Group 1"}, 
                                new String[] {"group2", "Group 2a"},
                                new String[] {"group3", "Group 3"} });
            checkControl(
                WidgetType.LINK,
                "questionnaire1_action_0",
                new String[] { "previous" },
                "Previous");
            
            checkControl(
                WidgetType.LINK,
                "questionnaire1_action_1",
                new String[] { "next" },
                "Next");
            
            assertFalse("item_2_1 not deleted", isItemPresent("item_2_1"));
            
            element = elementMap.get(WidgetType.RADIO_GROUP);
            DynamicPageData.populateTestDataForDynamicPageQuestion(element, "item_5_2", false);
            element.check();
            
            element = elementMap.get(WidgetType.TEXT);
            DynamicPageData.populateTestDataForDynamicPageQuestion(element, "item_5_1", false);
            element.check();
                    
            checkGroup(
                "group5",
                new String[] { "group5Style1", "group5Style2" },
                "Group 5",
                new String[] { "item_5_1", "item_5_2" });
            
            checkGroup("group2", 
                    new String[] {
                    "group2Style1",
                    "group2Style2",
                    "group2Style3" }, "Group 2a", new String[] { "group5", "item_2_2", "item_2_3" });
            
            element = elementMap.get(WidgetType.DROPDOWN);
            DynamicPageData.populateTestDataForDynamicPageQuestion(element, "item_2_3", false);
            element.check();
            
            mouseDownControl("questionnaire1_action_1");
            checkQuestionnaire(
                "questionnaire2",
                new String[] { "questionnaire2Style" },
                "Questionnaire 2",
                new String[][] { new String[] {"group6", "Group 6"} });

            mouseDownControl("questionnaire2_action_1");
            checkAlert("You must fix all errors first.");

            setTextAnswer("item_6_1", "Test Text");
            mouseDownControl("questionnaire2_action_1");
            checkQuestionnaire(
                "questionnaire3",
                null,
                "All Done",
                new String[][] {});
            
            assertHooks();          
        }
        catch (Exception ex) {
            handleException("JQueryClientTest.testDynamicPage()", ex);
        }
    }    
        
}
