package org.tohu.test.element.data;

import org.tohu.test.element.Question;

/**
 * This class is used to configure all the elements used in the "Dynamic Page" test
 *  
 * The element is populated with the expected content for the test it will run in.
 * 
 * @author rb1317
 *
 */
public class ReadOnlyPageData {

   /**
    *
    * method to configure the data for all "Read Only" test questions, with the expected 
    * content for the HTML element it is to be matched against.
    *  
    * @param element
    * @param questionId -  Tohu id, used to determine what kind 
    *                      of expected data to configure for this question                        
    */
    public static void populateTestDataForReadOnlyPageQuestion(Question element, String questionId) {
        if ("item_1_1".equalsIgnoreCase(questionId)) {
            element.expectedId = "item_1_1";
            element.expectedPresentationStyles = new String[] { "item_1_1Style", "readonly" };
            element.expectedPreLabel = "Item 1.1";
            element.expectedPostLabel = "Simple Text Field";
            element.expectedRequired = true;
            element.expectedReadonly = true;
            element.expectedAnswer = "Item 1.1 Text";
            element.expectedPossibleAnswers = null;
            element.expectedErrors = new String[][] {};            
        } else if ("item_1_2".equalsIgnoreCase(questionId)) {
            element.expectedId = "item_1_2";
            element.expectedPresentationStyles = new String[] { "item_1_2Style", "readonly-inherited" };
            element.expectedPreLabel = "Item 1.2";
            element.expectedPostLabel = "Boolean Checkbox";
            element.expectedRequired = true;
            element.expectedReadonly = true;
            element.expectedAnswer = "true";
            element.expectedPossibleAnswers = null;
            element.expectedErrors = new String[][] {};            
        } else if ("item_1_3".equalsIgnoreCase(questionId)) {
            element.expectedId = "item_1_3";
            element.expectedPresentationStyles = new String[] { "item_1_3Style", "readonly", "datepicker" };
            element.expectedPreLabel = "Item 1.3";
            element.expectedPostLabel = "Date Field with Picker";
            element.expectedRequired = true;
            element.expectedReadonly = true;
            element.expectedAnswer = "16/06/2009";
            element.expectedPossibleAnswers = null;
            element.expectedErrors = new String[][] {};            
        } else if ("item_1_4".equalsIgnoreCase(questionId)) {
            element.expectedId = "item_1_4";
            element.expectedPresentationStyles = new String[] { "textarea", "item_1_4Style", "readonly" };
            element.expectedPreLabel = "Item 1.4";
            element.expectedPostLabel = "Text Area";
            element.expectedRequired = false;
            element.expectedReadonly = true;
            element.expectedAnswer = "Item 1.4 Text";
            element.expectedPossibleAnswers = null;
            element.expectedErrors = new String[][] {};            
        } else if ("item_1_5".equalsIgnoreCase(questionId)) {
            element.expectedId = "item_1_5";
            element.expectedPresentationStyles = new String[] { "readonly", "item_1_5Style", "radio" };
            element.expectedPreLabel = "Item 1.5";
            element.expectedPostLabel = "Radio Buttons";
            element.expectedRequired = true;
            element.expectedReadonly = true;
            element.expectedAnswer = "1";
            element.expectedPossibleAnswers = new String[][] {
                    new String[] { "0", "Item 1.5.0" },
                    new String[] { "1", "Item 1.5.1" },
                    new String[] { "2", "Item 1.5.2" } };
            element.expectedErrors = new String[][] {};            
        } else if ("item_1_6".equalsIgnoreCase(questionId)) {
            element.expectedId = "item_1_6";
            element.expectedPresentationStyles = new String[] { "item_1_6Style", "readonly-inherited" };
            element.expectedPreLabel = "Item 1.6";
            element.expectedPostLabel = "Decimal Dropdown List";
            element.expectedRequired = false;
            element.expectedReadonly = true;
            element.expectedAnswer = "0.3";
            element.expectedPossibleAnswers = new String[][] {
                    new String[] { "0.1", "Item 1.6.0.1" },
                    new String[] { "0.2", "Item 1.6.0.2" },
                    new String[] { "0.3", "Item 1.6.0.3" }, };
            element.expectedErrors = new String[][] {};            
        } else if ("item_1_7".equalsIgnoreCase(questionId)) {
            element.expectedId = "item_1_7";
            element.expectedPresentationStyles = new String[] { "item_1_7Style", "readonly" };
            element.expectedPreLabel = "Item 1.7";
            element.expectedPostLabel = "Boolean Radio";
            element.expectedRequired = true;
            element.expectedReadonly = true;
            element.expectedAnswer = "true";
            element.expectedPossibleAnswers = null;
            element.expectedErrors = new String[][] {};            
        }
    }
}
