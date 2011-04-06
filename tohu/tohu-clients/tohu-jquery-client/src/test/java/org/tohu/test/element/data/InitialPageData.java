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
public class InitialPageData {

   /**
    *
    * method to configure the data for all "Initial Page" test questions, with the expected 
    * content for the HTML element it is to be matched against.
    *  
    * @param element
    * @param questionId -  Tohu id, used to determine what kind 
    *                      of expected data to configure for this question                        
    */
    public static void populateTestDataForInitialPageQuestion(Question element, String questionId) {
        
        if ("item_1_3".equalsIgnoreCase(questionId)) {
            element.expectedId = "item_1_3";
            element.expectedPresentationStyles = new String[] { "datepicker", "item_1_3Style" };
            element.expectedPreLabel = "Item 1.3";
            element.expectedPostLabel = "Date Field with Picker";
            element.expectedRequired = true;
            element.expectedReadonly = false;
            element.expectedAnswer = "16/06/2009";
            element.expectedPossibleAnswers = null;
            element.expectedErrors = new String[][] {};         
        } else if ("item_2_1".equalsIgnoreCase(questionId)) {
            element.expectedId = "item_2_1";
            element.expectedPresentationStyles = new String[] { "item_2_1Style" };
            element.expectedPreLabel = "Item 2.1";
            element.expectedPostLabel = "Simple Text Field";
            element.expectedRequired = true;
            element.expectedReadonly = false;
            element.expectedAnswer = "Item 2.1 Text";
            element.expectedPossibleAnswers = null;
            element.expectedErrors = new String[][] {};         
        } else if ("item_2_2".equalsIgnoreCase(questionId)) {
            element.expectedId = "item_2_2";
            element.expectedPresentationStyles = new String[] { "textarea", "item_2_2Style" };
            element.expectedPreLabel = "Item 2.2";
            element.expectedPostLabel = "Text Area";
            element.expectedRequired = false;
            element.expectedReadonly = false;
            element.expectedAnswer = "Item 2.2 Text";
            element.expectedPossibleAnswers = null;
            element.expectedErrors = new String[][] {};            
        } else if ("item_2_3".equalsIgnoreCase(questionId)) {
            element.expectedId = "item_2_3";
            element.expectedPresentationStyles = new String[] { "item_2_3Style", "radio" };
            element.expectedPreLabel = "Item 2.3";
            element.expectedPostLabel = "Radio Buttons";
            element.expectedRequired = true;
            element.expectedReadonly = false;
            element.expectedAnswer = "1";
            element.expectedPossibleAnswers = new String[][] {
                    new String[] { "0", "Item 2.3.0" },
                    new String[] { "1", "Item 2.3.1" },
                    new String[] { "2", "Item 2.3.2" } };
            element.expectedErrors = new String[][] { new String[] { "1", "ErrorType1", "Item 2.3 Error" } };            
        } else if ("item_3_1".equalsIgnoreCase(questionId)) {
            element.expectedId = "item_3_1";
            element.expectedPresentationStyles = new String[] { "item_3_1Style" };
            element.expectedPreLabel = "Item 3.1";
            element.expectedPostLabel = "Decimal Dropdown List";
            element.expectedRequired = false;
            element.expectedReadonly = false;
            element.expectedAnswer = "0.3";
            element.expectedPossibleAnswers = new String[][] {
                    new String[] { "0.1", "Item 3.1.0.1" },
                    new String[] { "0.2", "Item 3.1.0.2" },
                    new String[] { "0.3", "Item 3.1.0.3" }, };
            element.expectedErrors = new String[][] {};            
        } else if ("item_3_2".equalsIgnoreCase(questionId)) {
            element.expectedId = "item_3_2";
            element.expectedPresentationStyles = new String[] { "item_3_2Style" };
            element.expectedPreLabel = "Item 3.2";
            element.expectedPostLabel = "Text Dropdown List";
            element.expectedRequired = false;
            element.expectedReadonly = false;
            element.expectedAnswer = "";
            element.expectedPossibleAnswers = new String[][] {
                    new String[] { "", "Please specify..." },
                    new String[] { "first", "Item 3.2.1" },
                    new String[] { "second", "Item 3.2.2" },
                    new String[] { "third", "Item 3.2.3" } };
            element.expectedErrors = new String[][] {};            
        } else if ("item_3_3".equalsIgnoreCase(questionId)) {
            element.expectedId = "item_3_3";
            element.expectedPresentationStyles = new String[] { "item_3_3Style" };
            element.expectedPreLabel = "Item 3.3";
            element.expectedPostLabel = "Date Field without Picker";
            element.expectedRequired = true;
            element.expectedReadonly = false;
            element.expectedAnswer = "";
            element.expectedPossibleAnswers = null;
            element.expectedErrors = new String[][] {};            
        } else if ("item_4_1".equalsIgnoreCase(questionId)) {
            element.expectedId = "item_4_1";
            element.expectedPresentationStyles = new String[] { "item_4_1Style" };
            element.expectedPreLabel = "Item 4.1";
            element.expectedPostLabel = "Boolean Checkbox";
            element.expectedRequired = true;
            element.expectedReadonly = false;
            element.expectedAnswer = "false";
            element.expectedPossibleAnswers = null;
            element.expectedErrors = new String[][] {};            
        } else if ("item_4_3".equalsIgnoreCase(questionId)) {
            element.expectedId = "item_4_3";
            element.expectedPresentationStyles = new String[] { "item_4_3Style" };
            element.expectedPreLabel = "Item 4.3";
            element.expectedPostLabel = "Boolean Radio Buttons";
            element.expectedRequired = true;
            element.expectedReadonly = false;
            element.expectedAnswer = "true";
            element.expectedPossibleAnswers = null;
            element.expectedErrors = new String[][] {
                    new String[] { "0", "ErrorType2", "Item 4.3 Error 2" },
                    new String[] { "2", "ErrorType1", "Item 4.3 Error 1" } };            
        } else if ("item_4_4".equalsIgnoreCase(questionId)) {
            element.expectedId = "item_4_4";
            element.expectedPresentationStyles = new String[] { "item_4_4Style" };
            element.expectedPreLabel = "Item 4.4";
            element.expectedPostLabel = "Multiple answer select";
            element.expectedRequired = false;
            element.expectedReadonly = false;
            element.expectedAnswer = null;
            element.expectedPossibleAnswers = new String[][] {
                    new String[] { "foo", "foo" },
                    new String[] { "bar", "bar" },
                    new String[] { "baz", "baz" },
                    new String[] { "qux", "qux" },
                    new String[] { "quux", "quux" }};
            element.expectedErrors = new String[][] {};            
        } else if ("item_4_5".equalsIgnoreCase(questionId)) {
            element.expectedId = "item_4_5";
            element.expectedPresentationStyles = new String[] { "item_4_5Style" };
            element.expectedPreLabel = "Item 4.5";
            element.expectedPostLabel = "Text Field with special characters";
            element.expectedRequired = false;
            element.expectedReadonly = false;
            element.expectedAnswer = "Text <>&'\"!@#$%^*()_-+={}[]:;?/,.`\\<>&'\"!@#$%^*()_-+={}[]:;?/,.`\\";
            element.expectedPossibleAnswers = null;
            element.expectedErrors = new String[][] {};            
        } else if ("item_4_6".equalsIgnoreCase(questionId)) {
            element.expectedId = "item_4_6";
            element.expectedPresentationStyles = new String[] { "item_4_6Style" };
            element.expectedPreLabel = "Item 4.6";
            element.expectedPostLabel = "Text Dropdown List with no null entry";
            element.expectedRequired = false;
            element.expectedReadonly = false;
            element.expectedAnswer = "";
            element.expectedPossibleAnswers = new String[][] {
                    new String[] {"", "Please select..."},
                    new String[] {"first", "Item 4.6.1"},
                    new String[] {"second", "Item 4.6.2"},
                    new String[] {"third", "Item 4.6.3"}};
            element.expectedErrors = new String[][] {};                                      
        }   
    }
}
