package org.tohu.test.element;

import org.tohu.test.enums.WidgetType;


/**
 * Factory method to create Element objects
 * 
 * @author rb1317
 *
 */
public class ElementFactory {

    public static Question getQuestion(WidgetType widgetType) {
        switch (widgetType) {
            case CHECKBOX : 
                return new CheckboxQuestion();
            case DATEPICKER : 
                return new DatePickerQuestion();
            case TEXT :
                return new TextQuestion();
            case FILE :
                return new FileUploadQuestion();
            case RADIO_BOOLEAN:
                return new RadioBooleanQuestion();
            case RADIO_GROUP:
                return new RadioGroupQuestion();
            case DROPDOWN:
                return new DropDownQuestion();
            case TEXTAREA:
                return new TextAreaQuestion();
            default:
                return new TextQuestion();                
        }
    }
    
}
