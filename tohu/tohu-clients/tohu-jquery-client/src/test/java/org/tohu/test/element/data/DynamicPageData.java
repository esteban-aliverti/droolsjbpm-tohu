package org.tohu.test.element.data;

import org.tohu.test.element.Question;

/**
 * This class is used to configure all the elements used in the "Dynamic Page" test
 *  
 * The element is populated with the expected content for the test it will run in.
 *  
 * 
 * @author rb1317
 *
 */
public class DynamicPageData {

    /**
     *
     * method to configure the data for all "Dynamic Page" test questions, with the expected 
     * content for the HTML element it is to be matched against.
     *  
     * @param element
     * @param questionId -  Tohu id, used to determine what kind 
     *                      of expected data to configure for this question 
     * @param itemClicked - Some questions are checked more than once based on a click
     *                      and the expected data changes between each click                       
     */
    public static void populateTestDataForDynamicPageQuestion(Question element, String questionId, boolean itemClicked) { 
        if ("item_4_3".equalsIgnoreCase(questionId)) {
            element.expectedId = "item_4_3";
            element.expectedPresentationStyles = new String[] { "item_4_3Style" };
            element.expectedPreLabel = "Item 4.3";
            element.expectedPostLabel = "Boolean Radio Buttons";
            element.expectedRequired = true;
            element.expectedReadonly = false;
            element.expectedAnswer = "false";
            element.expectedPossibleAnswers = null;
            element.expectedErrors = new String[][] {};            
        } else if ("item_3_1".equalsIgnoreCase(questionId)) {
            element.expectedId = "item_3_1";
            element.expectedPresentationStyles = new String[] { "item_3_1Style" };
            element.expectedPreLabel = "Item 3.1";
            element.expectedPostLabel = "Decimal Dropdown List";
            element.expectedRequired = false;
            element.expectedReadonly = false;
            element.expectedAnswer = "0.2";
            element.expectedPossibleAnswers = new String[][] {
                    new String[] { "0.1", "Item 3.1.0.1" },
                    new String[] { "0.2", "Item 3.1.0.2" },
                    new String[] { "0.3", "Item 3.1.0.3" }, };
            element.expectedErrors = new String[][] { new String[] { "3", "ErrorType1", "Item 3.1 Error 1" } };          
        } else if ("item_3_1_1".equalsIgnoreCase(questionId)) {
            element.expectedId = "item_3_1_1";
            element.expectedPresentationStyles = new String[] { "item_3_1_1Style" };
            element.expectedPreLabel = "Item 3.1.1";
            element.expectedPostLabel = "Another Simple Text Field";
            element.expectedRequired = false;
            element.expectedReadonly = false;
            element.expectedAnswer = "Item 3.1.1 Text";
            element.expectedPossibleAnswers = null;
            element.expectedErrors = new String[][] {};                        
        } else if ("item_3_2".equalsIgnoreCase(questionId)) {
            element.expectedId = "item_3_2";
            element.expectedPresentationStyles = new String[] { "item_3_2Style1", "item_3_2Style2" };
            element.expectedPreLabel = "Item 3.2a";
            element.expectedPostLabel = "Text Dropdown List";
            element.expectedRequired = true;
            element.expectedReadonly = false;
            element.expectedAnswer = "second";
            element.expectedPossibleAnswers = new String[][] {
                    new String[] { "first", "Item 3.2.1" },
                    new String[] { "second", "Item 3.2.2" },
                    new String[] { "third", "Item 3.2.3" } };
            element.expectedErrors = new String[][] {};            
        } else if ("item_4_1".equalsIgnoreCase(questionId) && (itemClicked)) {
            element.expectedId = "item_4_1";
            element.expectedPresentationStyles = new String[] { "item_4_1Style" };
            element.expectedPreLabel = "Item 4.1a";
            element.expectedPostLabel = "A Boolean Checkbox";
            element.expectedRequired = false;
            element.expectedReadonly = false;
            element.expectedAnswer = "true";
            element.expectedPossibleAnswers = null;
            element.expectedErrors = new String[][] {};            
        } else if ("item_4_1".equalsIgnoreCase(questionId) && (!itemClicked)) {
            element.expectedId = "item_4_1";
            element.expectedPresentationStyles = new String[] { "item_4_1Style" };
            element.expectedPreLabel = "Item 4.1";
            element.expectedPostLabel = "Boolean Checkbox";
            element.expectedRequired = true;
            element.expectedReadonly = false;
            element.expectedAnswer = "false";
            element.expectedPossibleAnswers = null;
            element.expectedErrors = new String[][] {};                 
        } else if ("item_2_2".equalsIgnoreCase(questionId)) {
            element.expectedId = "item_2_2";
            element.expectedPresentationStyles = new String[] { "textarea", "item_2_2Style" };
            element.expectedPreLabel = "Item 2.2";
            element.expectedPostLabel = "Text Area";
            element.expectedRequired = false;
            element.expectedReadonly = false;
            element.expectedAnswer = "More Test Text";
            element.expectedPossibleAnswers = null;
            element.expectedErrors = new String[][] {
                    new String[] { "4", "ErrorType3", "Item 2.2 Error 3" },
                    new String[] { "5", "ErrorType1", "Item 2.2 Error 1" },
                    new String[] { "6", "ErrorType2", "Item 2.2 Error 2" } };            
        } else if ("item_2_1".equalsIgnoreCase(questionId)) {
            element.expectedId = "item_2_1";
            element.expectedPresentationStyles = new String[] { "item_2_1Style" };
            element.expectedPreLabel = "Item 2.1a";
            element.expectedPostLabel = "A Simple Text Field";
            element.expectedRequired = true;
            element.expectedReadonly = false;
            element.expectedAnswer = "Still More Test Text";
            element.expectedPossibleAnswers = null;
            element.expectedErrors = new String[][] {};            
        } else if ("item_5_2".equalsIgnoreCase(questionId)) {
            element.expectedId = "item_5_2";
            element.expectedPresentationStyles = new String[] { "item_5_2Style1", "item_5_2Style2", "radio" };
            element.expectedPreLabel = "Item 5.2";
            element.expectedPostLabel = "More Radio Buttons";
            element.expectedRequired = false;
            element.expectedReadonly = false;
            element.expectedAnswer = "";
            element.expectedPossibleAnswers = new String[][] {
                    new String[] { "0", "Item 5.2.0" },
                    new String[] { "1", "Item 5.2.1" },
                    new String[] { "2", "Item 5.2.2" } };
            element.expectedErrors = new String[][] {};            
        } else if ("item_5_1".equalsIgnoreCase(questionId)) {   
            element.expectedId = "item_5_1";
            element.expectedPresentationStyles = new String[] { "item_5_1Style" };
            element.expectedPreLabel = "Item 5.1";
            element.expectedPostLabel = "Simple Text Field";
            element.expectedRequired = true;
            element.expectedReadonly = false;
            element.expectedAnswer = "";
            element.expectedPossibleAnswers = null;
            element.expectedErrors = new String[][] {};                      
        } else if ("item_2_3".equalsIgnoreCase(questionId)) {  
            element.expectedId = "item_2_3";
            element.expectedPresentationStyles = new String[] { "item_2_3Style" };
            element.expectedPreLabel = "Item 2.3";
            element.expectedPostLabel = "Radio Buttons?";
            element.expectedRequired = true;
            element.expectedReadonly = false;
            element.expectedAnswer = "";
            element.expectedPossibleAnswers = new String[][] {
                    new String[] { "0", "Item 2.3.0" },
                    new String[] { "1", "Item 2.3.1" },
                    new String[] { "2", "Item 2.3.2" } };
            element.expectedErrors = new String[][] { new String[] { "1", "ErrorType1", "Item 2.3 Error" } };   
        }
    }
}
