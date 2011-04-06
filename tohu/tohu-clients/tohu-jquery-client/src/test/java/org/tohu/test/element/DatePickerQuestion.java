package org.tohu.test.element;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.thoughtworks.selenium.DefaultSelenium;

/**
 * DatePicker entity to compare the expected content against the real content
 * accessible through the required selenium reference object
 * 
 * @author rb1317
 *
 */
public class DatePickerQuestion extends Question {

    public DatePickerQuestion() {
        super();
    }
    
    public DatePickerQuestion(DefaultSelenium selenium) {
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
        
        assertTrue(expectedId + " no input", selenium.isElementPresent("id=" + expectedId + "_input"));
        assertTrue(expectedId + " no datepicker", selenium.isElementPresent("style=ui-datepicker-trigger"));
        if (expectedAnswer != null) {
            assertEquals(expectedId + " wrong answer", expectedAnswer, selenium.getValue("id=" + expectedId + "_input"));
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
