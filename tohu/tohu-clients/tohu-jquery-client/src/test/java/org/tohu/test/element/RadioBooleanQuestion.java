package org.tohu.test.element;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.thoughtworks.selenium.DefaultSelenium;

/**
 * RadioBoolean entity to compare the expected content against the real content
 * accessible through the required selenium reference object
 * 
 * @author rb1317
 *
 */
public class RadioBooleanQuestion extends Question {

    public RadioBooleanQuestion() {
        super();
    }
    
    public RadioBooleanQuestion(DefaultSelenium selenium) {
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
        assertTrue(expectedId + " wrong true button", 
                   selenium.isElementPresent("css=#" + expectedId + "_input_true[type=radio]"));
        assertTrue(expectedId + " wrong false button",
                   selenium.isElementPresent("css=#" + expectedId + "_input_false[type=radio]"));                           
        assertEquals(expectedId + " wrong true button label", 
                     "Yes", 
                     selenium.getText("css=#" + expectedId + "_input label[for=\"" + expectedId + "_input_true\"]"));
        assertEquals(expectedId + " wrong false button label", 
                     "No",
                     selenium.getText("css=#" + expectedId + "_input label[for=\"" + expectedId + "_input_false\"]"));
        if (expectedAnswer != null) {
            assertTrue(expectedId + " wrong radio button selected",
                       selenium.isChecked("css=#" + expectedId + "_input_" + expectedAnswer.toLowerCase()));
        }
        if (expectedReadonly != null) {
            assertEquals(expectedId + " wrong readonly true button",
                         expectedReadonly.booleanValue(),
                         selenium.isElementPresent("css=#" + expectedId + "_input div.readonly_overlay"));                                  
            assertEquals(expectedId + " wrong readonly false button",
                         expectedReadonly.booleanValue(),
                         selenium.isElementPresent("css=#" + expectedId + "_input div.readonly_overlay"));                          
        }

        checkErrors(expectedId, expectedErrors);

    }

}
