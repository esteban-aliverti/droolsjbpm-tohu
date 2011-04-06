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
public class FileUploadData {

   /**
    *
    * method to configure the data for all "File Upload" test questions, with the expected 
    * content for the HTML element it is to be matched against.
    *  
    * @param element
    * @param questionId -  Tohu id, used to determine what kind 
    *                      of expected data to configure for this question                        
    */    
    public static void populateTestDataForFileUploadPage(Question element, String questionId){
        if ("item_1_1".equalsIgnoreCase(questionId)) {
            element.expectedId = "item_1_1";
            element.expectedPresentationStyles = new String[] { "item_1_1Style", "file" };
            element.expectedPreLabel = "Item 1.1";
            element.expectedPostLabel = "File Upload";
            element.expectedRequired = false;
            element.expectedReadonly = false;
            element.expectedAnswer = "";
            element.expectedPossibleAnswers = null;
            element.expectedErrors = new String[][] {};            
        }
    }
}
