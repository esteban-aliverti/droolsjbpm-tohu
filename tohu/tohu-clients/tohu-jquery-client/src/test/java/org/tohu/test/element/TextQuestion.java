package org.tohu.test.element;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.thoughtworks.selenium.DefaultSelenium;

/**
 * Text entity to compare the expected content against the real content
 * accessible through the required selenium reference object
 * 
 * @author rb1317
 *
 */
public class TextQuestion extends Question {
    
    public TextQuestion() {
        super();
    }
    
    public TextQuestion(DefaultSelenium selenium) {
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
        
        assertTrue(expectedId + " no input", selenium.isElementPresent("css=#" + expectedId + "_input"));
        if (expectedAnswer != null) {
            assertEquals(expectedId + " wrong answer", expectedAnswer, selenium.getValue("css=#" + expectedId + "_input"));
        }
        if (expectedReadonly != null) {
            assertEquals(
                expectedId + " wrong readonly",
                expectedReadonly.booleanValue(),
                selenium.isElementPresent("css=#" + expectedId + " div.readonly_overlay"));                         
        }

        checkErrors(expectedId, expectedErrors);

    }

}
