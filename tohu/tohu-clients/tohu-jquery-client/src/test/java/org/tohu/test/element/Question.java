package org.tohu.test.element;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.thoughtworks.selenium.DefaultSelenium;

/**
 * Base entity to configure expected content for a Tohu Question
 * It will also compare the expected content against the real content
 * accessible through the required selenium reference object
 * 
 * @author rb1317
 *
 */
public abstract class Question {
    
    protected DefaultSelenium selenium;
    
    public String expectedId;
    // presentationStyles List of styles to check for (null means don't check).    
    public String[] expectedPresentationStyles;
    // preLabel The question pre-label text to check for (null means don't check).    
    public String expectedPreLabel;
    // postLabel The question post-label text to check for (null means don't check).    
    public String expectedPostLabel;
    // required Flag indicating if the question is required (null means don't check).    
    public Boolean expectedRequired;
    // readonly Flag indicating if the question is readonly (null means don't check).    
    public Boolean expectedReadonly;
    // answer - the expected answer to this question 
    public String expectedAnswer;
    // possibleAnswers List of possible answers, 1st element Value, 2nd element Label (null means don't check).    
    public String[][] expectedPossibleAnswers;
    // errors List of possible errors, 1st element Type, 2nd element Reason (null means don't check).    
    public String[][] expectedErrors;
    
    public Question() {
        super();
    }
    public Question(DefaultSelenium selenium) {
        this();
        this.selenium = selenium;
    }
    
    public abstract void check();
    
    /**
     * checks the expected presentation styles against the actual styles for the question
     * 
     * @param questionId
     * @param presentationStyles
     * @param required
     * @param errors
     */
    protected void checkStyles(String questionId, String[] presentationStyles, Boolean required, String[][] errors) {
        
        if (selenium == null) {
            throw new RuntimeException("One must set the selenmium field on " +
            		"the Element before invoking activity on it");
        }
        
        if (presentationStyles != null) {
            String classAttr = selenium.getAttribute("css=#" + questionId + "@class");
            checkStyle(questionId, classAttr, "Question", true);
            if (required != null) {
                checkStyle(questionId, classAttr, "required", required.booleanValue());
            }
            if (errors != null) {
                checkStyle(questionId, classAttr, "error", (errors.length > 0));
            }
            for (int i = 0; i < presentationStyles.length; i++) {
                checkStyle(questionId, classAttr, presentationStyles[i], true);
            }
        }        
    }
    
    /**
     * Checks the specified style is present or not present.
     * 
     * @param questionId ID of the HTML element.
     * @param classAttr The class attribute of the HTML element.
     * @param className The name of the class to check for.
     * @param present The presence to check for.
     */
    protected void checkStyle(String questionId, String classAttr, String className, boolean present) {
        if (present) {
            assertTrue(
                questionId + " does not have style '" + className + "'",
                (" " + classAttr + " ").contains(" " + className + " "));
        }
        else {
            assertFalse(questionId + " has style '" + className + "'", (" " + classAttr + " ").contains(" "
                    + className
                    + " "));
        }
    }
    
    /**
     * Tests the pre and post labels of the Tohu Question
     * @param questionId - ItemId
     * @param preLabel
     * @param postLabel
     */
    protected void checkLabels(String questionId, String preLabel, String postLabel) {
        
        if (selenium == null) {
            throw new RuntimeException("One must set the selenmium field on " +
                    "the Element before invoking activity on it");
        }
        
        if (preLabel != null) {
            assertEquals(questionId + " wrong preLabel", 
                         preLabel, 
                         selenium.getText("css=#" + questionId + "_preLabel"));
        }
        if (postLabel != null) {
            assertEquals(questionId + " wrong postLabel", 
                         postLabel, 
                         selenium.getText("css=#" + questionId + "_postLabel"));
        }        
    }
    

    /**
     * checks the expected errors against what the actual errors for the question are
     * 
     * @param id
     * @param errors
     */
    protected void checkErrors(String id, String[][] errors) {
        if (errors != null) {
            assertTrue(id + " missing error container", selenium.isElementPresent("css=#" + id + "_errors"));
            assertTrue(id + " missing error ul styleClass questionErrors", selenium.isElementPresent("css=#" + id + "_errors.questionErrors"));            
            for (int i = 0; i < errors.length; i++) {
                assertTrue(id + " missing error container item'" + errors[i][1] + "'", selenium.isElementPresent("css=#" + id + "_error_" + errors[i][0] + ".questionError"));
                assertTrue(id + " missing error container item style class'" + errors[i][1] + "'", selenium.isElementPresent("css=#" + id + "_error_" + errors[i][0] + "_span." +  errors[i][1]));
                assertTrue(id + " missing error container item text style class'" + errors[i][1] + "'", selenium.isElementPresent("css=#" + id + "_error_" + errors[i][0] + "_span.InvalidAnswer"));                
                assertEquals(errors[i][2], selenium.getText("css=#" + id + "_error_" + errors[i][0] + "_span") );
            }
        }        
    }
    
    public DefaultSelenium getSelenium() {
        return selenium;
    }
    
    public void setSelenium(DefaultSelenium selenium) {
        this.selenium = selenium;
    }


}
