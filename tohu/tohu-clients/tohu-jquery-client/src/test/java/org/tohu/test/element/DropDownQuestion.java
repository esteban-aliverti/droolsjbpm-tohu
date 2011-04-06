package org.tohu.test.element;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.thoughtworks.selenium.DefaultSelenium;

/**
 * DropDown entity to compare the expected content against the real content
 * accessible through the required selenium reference object
 * 
 * @author rb1317
 *
 */
public class DropDownQuestion extends Question {

    public DropDownQuestion() {
        super();
    }
    
    public DropDownQuestion(DefaultSelenium selenium) {
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
        
        assertTrue(expectedId + " no dropdown", selenium.isElementPresent("css=#" + expectedId + "_input"));
        if (expectedPossibleAnswers != null) {
            for (int i = 0; i < expectedPossibleAnswers.length; i++) {
                assertEquals(expectedId + " wrong list item " + i,
                             expectedPossibleAnswers[i][1],
                             selenium.getText("css=#" + expectedId + "_input option[value=" + expectedPossibleAnswers[i][0] + "]"));
            }
            if (expectedAnswer != null) {
                assertEquals(expectedId + " wrong list item selected",
                             expectedAnswer,
                             selenium.getSelectedValue("css=#" + expectedId + "_input"));
            }
        }
        if (expectedReadonly != null) {
            assertEquals(expectedId + " wrong readonly", 
                         expectedReadonly.booleanValue(),
                         selenium.isElementPresent("css=#" + expectedId + " div.readonly_overlay"));
        }

        checkErrors(expectedId, expectedErrors);
    }

}
