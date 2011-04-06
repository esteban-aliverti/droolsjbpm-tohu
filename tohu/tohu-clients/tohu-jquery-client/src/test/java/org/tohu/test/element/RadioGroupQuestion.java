package org.tohu.test.element;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.thoughtworks.selenium.DefaultSelenium;

/**
 * RadioGroup entity to compare the expected content against the real content
 * accessible through the required selenium reference object
 * 
 * @author rb1317
 *
 */
public class RadioGroupQuestion extends Question {

    public RadioGroupQuestion() {
        super();
    }
    
    public RadioGroupQuestion(DefaultSelenium selenium) {
        super(selenium);
    }
    
    /**
     * Checks that all properties of this expected element (Question) match against
     * those of the actual HTML Element accessible through the selenium object.
     *  
     */        
    public void check() {

        if (selenium == null) {
            throw new RuntimeException("One must set the selenmium field on " +
                    "the Element before invoking activity on it");
        }
        
        assertTrue("Question " + expectedId + " not found", selenium.isElementPresent("id=" + expectedId));
        checkStyles(expectedId, expectedPresentationStyles, expectedRequired, expectedErrors);
        checkLabels(expectedId, expectedPreLabel, expectedPostLabel);        
        
        assertTrue(expectedId + " no radio group", selenium.isElementPresent("css=#" + expectedId + "_input"));
        if (expectedPossibleAnswers != null) {
            for (int i = 0; i < expectedPossibleAnswers.length; i++) {
                String htmlID = expectedId + "_input_" + expectedPossibleAnswers[i][0];                         
                assertTrue(expectedId + " wrong input type " + i,
                           selenium.isElementPresent("css=#" + htmlID + "[type=radio]"));
                assertTrue(expectedId + " wrong radio value " + i,                          
                           selenium.isElementPresent("css=#" + htmlID + "[value=" + expectedPossibleAnswers[i][0] + "]"));
                assertEquals(expectedId + " wrong radio button " + i + " label",
                             expectedPossibleAnswers[i][1],
                             selenium.getText("css=#" + expectedId + "_input" + " label[for=" + htmlID + "]"));
                if (expectedAnswer != null) {
                    assertEquals(expectedId + " wrong radio button selected",
                                 expectedAnswer.equals(expectedPossibleAnswers[i][0]),
                                 selenium.isChecked("css=#" + expectedId + "_input_" + i));
                }
                if (expectedReadonly != null) {
                    assertEquals(expectedId + " wrong readonly radio button " + i,
                                 expectedReadonly.booleanValue(),
                                 selenium.isElementPresent("css=#" + expectedId + " div.readonly_overlay"));
                }
            }
        }

        checkErrors(expectedId, expectedErrors);

    }

}
