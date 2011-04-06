package org.tohu;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tohu.test.enums.BrowserType;
import org.tohu.test.enums.WidgetType;

public class IntermediateTestFramework extends BaseTestFramework {

    private static final Logger logger = LoggerFactory.getLogger(IntermediateTestFramework.class);      

    /** The maximum number of milliseconds to wait for the AJAX call and subsequent processing to complete */
    protected static String processingTimeout = "10000";

    /** System time at which the test case started for debugging purposes */
    protected static long startTime = System.currentTimeMillis();

    public IntermediateTestFramework() {
        super();
    }
    
    public IntermediateTestFramework(BrowserType browser, String url, String isGUIBusyID) {
        super(browser, url, isGUIBusyID);
    }
    
    /**
     * Opens the test URL with the specified parameters.
     * 
     * @param testParams Array of parameters to be passed to the test page.
     * @throws Exception
     */
    public void openURL(String[] testParams) throws Exception {
        // Add parameters.
        String fullURL = urlString + "?isGUIBusyID=" + isGUIBusyID;
        for (int i = 0; i < testParams.length; i++) {
            fullURL += "&" + testParams[i];
        }
        logger.debug("fullURL=" + fullURL);
        selenium.open(fullURL);
        // Wait up to 30 seconds for the initial page to load.
        selenium.waitForPageToLoad("30000");
        // Make sure we have focus on the main browser window.
        selenium.selectWindow("null");
        logger.debug("done");
    }
    
    /**
     * Waits up to 10 seconds for the AJAX call and subsequent processing to complete.
     * @param step Description of where we are for debugging purposes.
     */
    protected void waitWhileGUIIsBusy(String step) {
        logger.debug("step=" + step);
        selenium.waitForCondition("(selenium.getValue(\"id=" + isGUIBusyID + "\") == 'false')", processingTimeout);
        logger.debug("done");
    }

    /**
     * Set the value in the text field for the specified question to the specified answer.
     * @param quesionID
     * @param answer
     */
    protected void setTextAnswer(String questionID, String answer) {
        // Set field value.
        selenium.type(questionID + "_input", answer);
        waitWhileGUIIsBusy("setTextAnswer: questionID=" + questionID);
    }

    /**
     * Returns the value in the text field for the specified question.
     * @param quesionID
     * @return String
     */
    protected String getTextAnswer(String questionID) {
        return selenium.getValue(questionID + "_input");
    }

    /**
     * Activates the specified action.
     * @param quesionID
     */
    protected void mouseDownControl(String actionID) {
        selenium.mouseDown(actionID);
        waitWhileGUIIsBusy("clickControl: actionID=" + actionID);
    }

    /**
     * Toggles the checkbox for the specified question.
     * @param quesionID
     */
    protected void clickCheckboxAnswer(String questionID) {
        try {
            selenium.click(questionID + "_input");
        }
        catch (RuntimeException ex) {
            if ((browser.equals(BrowserType.IEXPLORER))
                    && ex.getMessage().equals(
                        "ERROR: Command execution failure. Please search the forum at http://clearspace.openqa.org for error details from the log window.  The error message is: Unspecified error.")) {
                // For some reason Selenium throws this exception when simulating a click event in
                // IE running against the Jetty Server/Test Harness, but seems to continue fine if
                // you ignore it.
                logger.debug("Ignoring exception: " + ex);
            }
            else {
                throw ex;
            }
        }
        waitWhileGUIIsBusy("clickCheckboxAnswer: questionID=" + questionID);
    }

    /**
     * Returns the current checkbox value for the specified question.
     * @param quesionID
     * @return boolean
     */
    protected boolean getCheckboxAnswer(String questionID) {
        return selenium.isChecked(questionID + "_input");
    }

    /**
     * Sets the radio button group for the specified question to the specified answer value.
     * @param quesionID
     * @param answer
     */
    protected void clickRadioButtonAnswer(String questionID, String answer) {
        try {
            selenium.click(questionID + "_input_" + answer);
        }
        catch (RuntimeException ex) {
            if ((browser.equals(BrowserType.IEXPLORER))
                    && ex.getMessage().equals(
                        "ERROR: Command execution failure. Please search the forum at http://clearspace.openqa.org for error details from the log window.  The error message is: Unspecified error.")) {
                // For some reason Selenium throws this exception when simulating a click event in
                // IE running against the Jetty Server/Test Harness, but seems to continue fine if
                // you ignore it.
                logger.error("Ignoring IE specific exception: " + ex);
            }
            else {
                throw ex;
            }
        }
        waitWhileGUIIsBusy("clickRadioButtonAnswer: questionID=" + questionID);
    }

    /**
     * Gets the current value of the radio button group for the specified question.
     * @param quesionID
     * @return The answer
     */
    protected String getRadioButtonAnswer(String questionID) {
        if (browser.equals(BrowserType.IEXPLORER)) {
            return selenium.getAttribute("//span[@id='"
                    + questionID
                    + "_input']/input[@type='radio' and @checked='true']/@value");
        }
        else {
            return selenium.getAttribute("//span[@id='"
                    + questionID
                    + "_input']/input[@type='radio' and @checked='']/@value");
        }
    }

    /**
     * Sets the drop-down list for the specified question to the specified answer value.
     * @param quesionID
     * @param answer
     */
    protected void selectDropDownAnswer(String questionID, String answer) {
        selenium.select(questionID + "_input", "value=" + answer);
        waitWhileGUIIsBusy("selectDropDownAnswer: questionID=" + questionID);
    }

    /**
     * Gets the selected drop-down list value for the specified question.
     * @param quesionID
     * @return the Answer
     */
    protected String getDropDownAnswer(String questionID) {
        return selenium.getSelectedValue(questionID + "_input");
    }

    /**
     * Checks that all properties of the Note HTML match those specified.
     * 
     * @param path XPath to the note's primary div.
     * @param widget Type of HTML widget.
     * @param id The note ID.
     * @param presentationStyles List of styles to check for (null means don't check).
     * @param label The note label text to check for (null means don't check). 
     */
    protected void checkNote(
            WidgetType widget,
            String id,
            String[] presentationStyles,
            String label) {
        assertTrue("Note " + id + " not found ", 
                    selenium.isElementPresent("css=#" + id));
        if (presentationStyles != null) {
            String classAttr = selenium.getAttribute("css=#" + id + "@class");
            logger.debug("classAttr=" + classAttr);
            checkStyle(id, classAttr, "Note", true);
            for (int i = 0; i < presentationStyles.length; i++) {
                checkStyle(id, classAttr, presentationStyles[i], true);
            }
        }
        if (label != null) {
            if (widget == WidgetType.IMAGE) {
                    assertTrue(id + " no image", 
                               selenium.isElementPresent("css=#" + id + "_image"));
            }
            else {
                assertEquals(id + " wrong label", 
                             label, 
                             selenium.getText("css=#" + id + "_label"));
            }
        }

    }

    /**
     * Checks that all properties of a Control HTML match those specified.
     * 
     * @param path XPath to the control's primary div.
     * @param widget Type of HTML widget.
     * @param id The control ID.
     * @param presentationStyles List of styles to check for (null means don't check).
     * @param label The control label text to check for (null means don't check). 
     */
    protected void checkControl(
            WidgetType widget,
            String id,
            String[] presentationStyles,
            String label) {
        if (widget == WidgetType.BUTTON) {
            assertTrue("Button control " + id + " not found", 
                       selenium.isElementPresent("css=#" + id));
            if (label != null) {
                assertEquals("Button " + id + " wrong label", 
                             label, 
                             selenium.getAttribute("css=#" + id + "@value"));
            }
        }
        else {
            assertTrue("Link control " + id + " not found", selenium.isElementPresent("css=#" + id));
            if (label != null) {
                assertEquals("Link " + id + " wrong label", 
                             label, 
                             selenium.getText("css=#" + id));
            }
        }
        if (presentationStyles != null) {
            String classAttr = selenium.getAttribute("css=#" + id + "@class");
            logger.debug("classAttr=" + classAttr);
            checkStyle(id, classAttr, "Control", true);
            for (int i = 0; i < presentationStyles.length; i++) {
                checkStyle(id, classAttr, presentationStyles[i], true);
            }
        }
    }

    /**
     * Checks that all properties of a Group HTML match those specified.
     * 
     * @param path XPath to the group's primary div.
     * @param id The group ID.
     * @param presentationStyles List of styles to check for (null means don't check).
     * @param label The group label text to check for (null means don't check).
     * @param items List of item IDs to check for, in order (null means don't check). 
     */
    protected void checkGroup(
            String id,
            String[] presentationStyles,
            String label,
            String[] items) {
        assertTrue("Group " + id + " not found", selenium.isElementPresent("css=#" + id));
        if (presentationStyles != null) {
            String classAttr = selenium.getAttribute("css=#" + id + "@class");
            logger.debug("classAttr=" + classAttr);
            checkStyle(id, classAttr, "Group", true);
            for (int i = 0; i < presentationStyles.length; i++) {
                checkStyle(id, classAttr, presentationStyles[i], true);
            }
        }
        if (label != null) {
            assertEquals(id + " wrong label", 
                    label,
                    selenium.getText("css=#" + id + "_label"));
        }
        if (items != null) {
            for (int i = 0; i < items.length; i++) {
                assertTrue(
                    id + " item " + i + " missing",
                    selenium.isElementPresent("css=#" + items[i]));     
            }
        }
    }

    /**
     * Checks that all properties of a Questionnaire HTML match those specified.
     * 
     * @param path XPath to the questionnaire's primary div.
     * @param id The questionnaire ID.
     * @param presentationStyles List of styles to check for (null means don't check).
     * @param label The questionnaire label text to check for (null means don't check).
     * @param items List of item IDs to check for, in order (null means don't check). 
     */
    protected void checkQuestionnaire(
            String id,
            String[] presentationStyles,
            String label,
            String[][] items) {
        assertTrue(
            "Questionnaire form " + id + " not found",
            selenium.isElementPresent("css=#" + id + "_form"));
        assertTrue(
            "Questionnaire group " + id + " not found",
            selenium.isElementPresent("css=#" + id));
        if (presentationStyles != null) {
            String classAttr = selenium.getAttribute("css=#" + id + "@class");
            logger.debug("classAttr=" + classAttr);
            checkStyle(id, classAttr, "Questionnaire", true);
            for (int i = 0; i < presentationStyles.length; i++) {
                checkStyle(id, classAttr, presentationStyles[i], true);
            }
        }
        if (label != null) {
            assertEquals(id + " wrong label", 
                    label, 
                    selenium.getText("css=#" + id + "_label"));
        }
        if (items != null) {
            for (int i = 0; i < items.length; i++) {
                assertTrue(selenium.isElementPresent("css=#" + items[i][0]));
                assertEquals(
                    id + " item " + i + " missing",
                    items[i][1],
                    selenium.getText("css=#" + items[i][0] + "_label"));
            }
        }
    }

    /**
     * Checks the specified style is present or not present.
     * 
     * @param id ID of the HTML element.
     * @param classAttr The class attribute of the HTML element.
     * @param className The name of the class to check for.
     * @param present The presence to check for.
     */
    protected void checkStyle(String id, String classAttr, String className, boolean present) {
        if (present) {
            assertTrue(
                id + " does not have style '" + className + "'",
                (" " + classAttr + " ").contains(" " + className + " "));
        }
        else {
            assertFalse(id + " has style '" + className + "'", (" " + classAttr + " ").contains(" "
                    + className
                    + " "));
        }
    }

    /**
     * Checks that the specified message is displayed in an alert box.
     * 
     * @param message The message text.
     */
    protected void checkAlert(String message) {
        assertTrue("Alert '" + message + "' not displayed", selenium.isAlertPresent());
        assertEquals("Alert '" + message + "' not displayed", message, selenium.getAlert());
    }

    /**
     * Returns true if an item with the specified ID is present on the page.
     * @param itemID
     * @return boolean
     */
    protected boolean isItemPresent(String itemID) {
        return selenium.isElementPresent("//div[@id='" + itemID + "']");
    }

    /**
     * Handles an exception thrown by the test case.
     */
    protected void handleException(String msg, Exception ex) throws Exception {
        logger.error(msg, ex);
        logger.error(selenium.retrieveLastRemoteControlLogs());
        throw ex;
    }
    
    // Check all the JavaScript hooks have been called.
    protected void assertHooks() {
        assertEquals(
            "preQuestionnaireLoad hook not called",
            "true",
            selenium.getEval("window.calledPreQuestionnaireLoad"));
        assertEquals(
            "preRefreshScreen hook not called",
            "true",
            selenium.getEval("window.calledPreRefreshScreen"));
        assertEquals(
            "postRefreshScreen hook not called",
            "true",
            selenium.getEval("window.calledPostRefreshScreen"));
        assertEquals(
            "preProcessDelete hook not called",
            "true",
            selenium.getEval("window.calledPreProcessDelete"));
        assertEquals(
            "postProcessDelete hook not called",
            "true",
            selenium.getEval("window.calledPostProcessDelete"));
        assertEquals(
            "preProcessCreate hook not called",
            "true",
            selenium.getEval("window.calledPreProcessCreate"));
        assertEquals(
            "postProcessCreate hook not called",
            "true",
            selenium.getEval("window.calledPostProcessCreate"));
        assertEquals(
            "preProcessUpdate hook not called",
            "true",
            selenium.getEval("window.calledPreProcessUpdate"));
        assertEquals(
            "postProcessUpdate hook not called",
            "true",
            selenium.getEval("window.calledPostProcessUpdate"));
        assertEquals(
            "preChangeEvent hook not called",
            "true",
            selenium.getEval("window.calledPreChangeEvent"));
        assertEquals(
            "postChangeEvent hook not called",
            "true",
            selenium.getEval("window.calledPostChangeEvent"));
        assertEquals(
            "onChangeEvent hook not called",
            "true",
            selenium.getEval("window.calledOnChangeEvent"));
        assertEquals(
            "preActionEvent hook not called",
            "true",
            selenium.getEval("window.calledPreActionEvent"));
        assertEquals(
            "postActionEvent hook not called",
            "true",
            selenium.getEval("window.calledPostActionEvent"));
        assertEquals(
            "onActionEvent hook not called",
            "true",
            selenium.getEval("window.calledOnActionEvent"));
        assertEquals(
            "onGetQuestionnaireActions hook not called",
            "true",
            selenium.getEval("window.calledOnGetQuestionnaireActions"));
        assertEquals(
            "onGetObjectClass hook not called",
            "true",
            selenium.getEval("window.calledOnGetObjectClass"));     
    }
}
