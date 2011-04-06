/*
 * Copyright 2009 Solnet Solutions Limited (http://www.solnetsolutions.co.nz/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author John Bebbington
 *
 *
 * Javascript to generate and display the JQuery Dynamic UI HTML.
 */

// Flag indicating if we tabbed out of a question field (true = tabbed forward, false = tabbed
// backward, null = didn't tab out). 
var questionTabForward = null;

// Special presentationStyle constants.
var RADIO_STYLE = 0;
var TEXTAREA_STYLE = 1;
var FILE_STYLE = 2;
var DATEPICKER_STYLE = 3;
var READONLY_STYLE = 4;
var IMAGE_STYLE = 5;
var BUTTON_STYLE = 6;


/**
 * Create a new Questionnaire and display it.
 * 
 * @param obj QuestionnaireObject.
 * @param isGUIBusyID Optional ID of a hidden field to store the isGUIBusy flag so the selenium test client can see it.
 */
function createQuestionnaire(obj, isGUIBusyID) {
	debugObject("createQuestionnaire() obj=", obj);
	// Wrap Questionnaire in a HTML form.
	var html = "<form name='myform' id='" + obj.id
		+ "_form' method='POST' action='#' ENCTYPE='multipart/form-data'>";
	if (!isNull(isGUIBusyID)) {
		html += "<input id='" + isGUIBusyID + "' name='" + isGUIBusyID
			+ "' type='hidden' tabindex='-1' value='true'/>";
	}
	html += "</form>";
	return addToParent(obj.hierarchy, html);
}

/**
 * Create a control block for the Questionnaire and display it.
 * 
 * @param obj QuestionnaireObject.
 */
function createControls(obj) {
	debugObject("createControls() obj=", obj);
	var html = "<div id='" + obj.id + "_controls' class='Controls'></div>";
	return addToParent(obj.hierarchy, html);
}

/**
 * Create a new Group and display it.
 * 
 * @param obj GroupObject or QuestionnaireObject.
 */
function createGroup(obj) {
	debugObject("createGroup() obj=", obj);
 	var html = "<div id='" + obj.id + "' class='";
 	html += getObjectClass(obj);
 	html += "'><p id='" + obj.id + "_label'>" + usuallyXmlEscape(obj.label) + "</p>";
 	html += "<div id='" + obj.id + "_items' class='GroupItems'></div></div>";
	return addToParent(obj.hierarchy, html);
}

 /**
  * Update a previously displayed Group.
  * 
  * @param obj GroupObject or QuestionnaireObject.
  */
function updateGroup(obj) {
 	debugObject("updateGroup() obj=", obj);
 	setElementClass(obj.id, getObjectClass(obj));
 	getJQElement(obj.id + "_label").text(obj.label);
 	/*
 	if (obj.items != null && obj.items.length > 0) {	
 		// check if the group's items need to be rebuilt
 		// note one can add/delete/update the group's item
 		// list dynamically 	
 		var remaingGroupItemsArray = recordExistingItems(obj);
 		removeAllGroupItems(obj);
 		createExistingItems(obj, remaingGroupItemsArray);		
 	}
 	*/
}

 /**
  * only record the existing jq elements which remain 
  * in the "Group" items list	
  */
function recordExistingItems(obj) {
 	var existingGroupItemsArray = new Array();
 	for(var count = 0; count < obj.items.length; count++) {
 		var jq = $("#" + obj.items[count]);
 		if (jq != null) {
 			existingGroupItemsArray[count] = jq;	
 		}			
 	}	
 	return existingGroupItemsArray;
}

 /**
  * remove all jq Elements inside this "Group"
  */
function removeAllGroupItems(obj){
 	var jqGroupItems = getJQElement(obj.id+"_items").children().remove();	
}
  
 /**
  * create the correct ordering of the "group" items
  */
function createExistingItems(obj, itemsArray) {
 	// get the div container for all the "Groups items"
 	var previousJQElement = getJQElement(obj.id+"_items");
 	// add the jq elements which were recorded
 	for(var count = 0; count < itemsArray.length; count++) {
 		var jqCurrentElement = itemsArray[count];
 		previousJQElement.after(jqCurrentElement);
 		previousJQElement = jqCurrentElement;
 	}	
}


/**
 * Build the HTML for the Question input(s) and return it.  
 * 
 * @param obj QuestionObject.
 * @return HTML string;
 */
function buildQuestionInput(obj) {
	debugObject("buildQuestionInput() obj=", obj);
	var html = "";
	var multipleChoice = (obj.possibleAnswers != null);
	
	switch (obj.answerType) {
	case "list":
	case "text":
	case "number":
	case "decimal":
		if (multipleChoice) {
			var keyValue = null;
			if (isSpecialStyle(obj, RADIO_STYLE)) {	
				// Build up a radio-button group for the question.				
				html += "<ul id='" + obj.id + "_input' class='radioList'>";
				for (var i = 0; i < obj.possibleAnswers.length; i++) {
					html += "<li class='listItem'>";
					keyValue = obj.possibleAnswers[i][0];
					debug("buildQuestionInput() radio possibleAnswer=" + keyValue);
					if (keyValue != "null") {
						var radioID = obj.id + "_input_" + keyValue;
						html += "<input id='" + radioID + "' name='" + obj.id + "' type='radio' value='" + keyValue + "'";
						if (obj.answer == keyValue) {
							html += " checked";
						}
						html += " class='answer_radio'>";
						html += "<label for='" + radioID + "'>" + usuallyXmlEscape(obj.possibleAnswers[i][1]) + "</label>";
					}
					html += "</li>";					
				}
				html += "</ul>";
			}
			else {
				// Build up a drop-down list for the question.
				var isMultiSelect = obj.answerType == "list";
				var multiAnswers = isMultiSelect ? splitWithEscapes(obj.answer, ',') : [];
				var multiple = isMultiSelect ? "multiple='true' size='9' " : "size='1' ";
				html += "<select id='" + obj.id + "_input' name='" + obj.id + "' " + multiple + "class='answer'>";
				var somethingSelected = false;
				var optionHtml = "";
				for (var i = 0; i < obj.possibleAnswers.length; i++) {
					keyValue = obj.possibleAnswers[i][0];
					debug("buildQuestionInput() select possibleAnswer=" + keyValue);
					if (keyValue == "null") {
						keyValue = "";
					}
					optionHtml += "<option ";
					var isAnswer = (obj.answer == keyValue);
					for (var j = 0; j < multiAnswers.length; j++) {
						if (keyValue == multiAnswers[j]) {
							isAnswer = true;
						}
					}
					if (isAnswer) {
						optionHtml += "selected='true'";
						somethingSelected = true;
					}
					optionHtml += "value='" + keyValue + "'>" + usuallyXmlEscape(obj.possibleAnswers[i][1]) + "</option>";
				}
				if (!somethingSelected && !isMultiSelect) {
					optionHtml = "<option value=''>Please select...</option>" + optionHtml;
				}
				html += optionHtml + "</select>";
			}
		}
		else {
			// Not multiple-choice.
			if (isSpecialStyle(obj, TEXTAREA_STYLE)) {
				// Render a text area
				html += "<textarea id='" + obj.id + "_input' name='" + obj.id + "' class='answer'>";
				html += usuallyXmlEscape(obj.answer);
				html += "</textarea>";
			}
			else if (isSpecialStyle(obj, FILE_STYLE)) {
				html += "<input id='" + obj.id + "_input' name='" + obj.id + "' type='file' value='" + usuallyXmlEscape(obj.answer) + "' class='answer'>";
			}
			else {
				html += "<input id='" + obj.id + "_input' name='" + obj.id + "' type='text' value='" + usuallyXmlEscape(obj.answer) + "' class='answer'>";
			}
		}
		break;

	case "boolean":
		if (isSpecialStyle(obj, RADIO_STYLE)) {
			html += "<span id='" + obj.id + "_input' class='radioGroup'>";
			html += "<input id='" + obj.id + "_input_true' name='" + obj.id + "' type='radio' value='true'";
			if (obj.answer == "true") {
				html += " checked";
			}
			html += " class='answer_radio'>";
			html += "<label for='" + obj.id + "_input_true'>Yes</label>&nbsp;";
			html += "<input id='" + obj.id + "_input_false' name='" + obj.id + "' type='radio' value='false'";
			if (obj.answer == "false") {
				html += " checked";
			}
			html += " class='answer_radio'>";
			html += "<label for='" + obj.id + "_input_false'>No</label>";
			html += "</span>";
		}
		else {
			html += "<input id='" + obj.id + "_input' name='" + obj.id + "' type='checkbox'";
			if (obj.answer == "true") {
				html += " checked";
			}
			html += " class='answer_checkbox'>";
		}
		break;
	
	case "date":
		if (multipleChoice) {
			if (!handleError(ERROR_TYPES.RULES_DEFINITION, [ obj.id, "multiple-choice questions cannot have date answers" ],
						"dynamicUI_html.buildQuestionInput", objectToString(obj))) {
				return "";
			}
		}
		if (!isBlank(obj.answer)) {
			try {
				obj.answer = $.datepicker.formatDate(persistentState.questionnaire.clientDateFormat, $.datepicker.parseDate("yy-mm-dd", obj.answer));
			}
			catch (ex) {
				if (!handleError(ERROR_TYPES.INVALID_DATE, [ obj.answer ], "dynamicUI_html.buildQuestionInput", ex)) {
					obj.answer = "";
				}
			}
		}
		html += "<input id='" + obj.id + "_input' name='" + obj.id + "' type='text' value='" + usuallyXmlEscape(obj.answer) + "' class='answer'>";
		break;
	
	default:
		if (!handleError(ERROR_TYPES.RULES_DEFINITION, [ obj.id, "invalid answerType: " + obj.answerType ],
				"dynamicUI_html.buildQuestionInput", objectToString(obj))) {
			return "";
		}
	}
	
	debug("buildQuestionInput() html=" + html);
	return html;
}

/**
 * Attach a change event handler for the Question.  
 * 
 * @param obj QuestionObject.
 */
function attachChangeHandler(obj) {
	debugObject("attachChangeHandler() obj=", obj);
	var input = getJQElement(obj.id + "_input");
	
	if ( (input.is("span")) || (input.is("ul")) ){
		debug("attachChangeHandler() radio");
		input.find(":radio").unbind();
		input.find(":radio").click(function() {		
			if ($(this).attr("checked")) {				
				handleChangeEvent(obj.id, $(this).attr("value"), questionTabForward);
			}
		});
		input.find(":radio").keydown(function(event) {			
			if (event.keyCode == 9) {
				if (event.shiftKey) {
					questionTabForward = false;
				}
				else {
					questionTabForward = true;
				}
			}
		});
		input.find(":radio").focus(function() {		
			questionTabForward = null;
		});
	}
	else if (input.is(":checkbox")) {
		debug("attachChangeHandler() checkbox");
		input.unbind();
		input.click(function() {			
			var newValue = null;
			if ($(this).attr("checked")) {
				newValue = "true";
			}
			else {
				newValue = "false";
			}
			handleChangeEvent(obj.id, newValue, questionTabForward);
		});
		input.keydown(function(event) {			
			if (event.keyCode == 9) {
				if (event.shiftKey) {
					questionTabForward = false;
				}
				else {
					questionTabForward = true;
				}
			}
		});
		input.focus(function() {		
			questionTabForward = null;
		});
	}
	else {
		debug("attachChangeHandler() other");
		input.unbind();
		if (obj.answerType == "date") {
			input.change(function() {				
				var value = $(this).attr("value");
				try {
					if (!isBlank(value)) {
						value = $.datepicker.formatDate("yy-mm-dd", $.datepicker.parseDate(persistentState.questionnaire.clientDateFormat, value));
					}
					handleChangeEvent(obj.id, value, questionTabForward);
				}
				catch (ex) {
					handleError(ERROR_TYPES.INVALID_DATE, [ value ], "dynamicUI_html.attachChangeHandler", ex);
					value = "";
				}
			});
		}
		else {
			input.change(function() {
				if ($(this).attr("multiple")) {
					var allSelectedValues = "";
					var delimiter = "";
					$.each($(this).parent().find("option:selected"), function (i, v) {allSelectedValues += delimiter + v.value; delimiter = ',';})
					handleChangeEvent(obj.id, allSelectedValues, questionTabForward);
				} else {
					handleChangeEvent(obj.id, $(this).attr("value"), questionTabForward);
				}
			});		
		}
		input.keydown(function(event) {			
			if (event.keyCode == 9) {
				if (event.shiftKey) {
					questionTabForward = false;
				}
				else {
					questionTabForward = true;
				}
			}
		});
		input.focus(function() {			
			questionTabForward = null;
		});
	}
}

/**
 * Add any required UI widgets for the Question.  
 * 
 * @param obj QuestionObject.
 */
function addQuestionWidgets(obj) {
	debugObject("addQuestionWidgets() obj=", obj);
	if ((obj.answerType == "date") && isSpecialStyle(obj, DATEPICKER_STYLE)) {
		var input = getJQElement(obj.id + "_input");
		if (!input.hasClass("hasDatepicker")) {
			input.datepicker({
				buttonImageOnly: true,
				buttonImage: 'images/calendar.gif',
				changeMonth: true,
				changeYear: true,
				dateFormat: persistentState.questionnaire.clientDateFormat,
				showOn: 'button'
			});
		}
	}
}

/**
 * Remove any UI widgets from the Question.  
 * 
 * @param obj QuestionObject.
 */
function removeQuestionWidgets(obj) {
	debugObject("removeQuestionWidgets() obj=", obj);
	var input = getJQElement(obj.id + "_input");
	if (input.hasClass("hasDatepicker")) {
		input.datepicker("destroy");
	}
}

/**
 * Checks whether a question is readonly and modifies the HTML accordingly.  
 * 
 * @param obj QuestionObject.
 */
function checkQuestionReadonly(obj) {
	if (isReadonly(obj)) {
		var input = $("#" + obj.id + " :input");
		if (!input.next().hasClass("readonly_overlay")) {
			input.readonly(true);
		}
		input.attr("tabindex", "-1");
	}
}

/**
 * Create a new Question and display it.  
 * 
 * @param obj QuestionObject.
 */
function createQuestion(obj) {
	debugObject("createQuestion() obj=", obj);
	var html = "<div id='" + obj.id + "' class='";
	html += getObjectClass(obj);
	html += "'>";
	if (obj.preLabel == "") {
		obj.preLabel = "&nbsp;";
	}
	html += "<span id='" + obj.id + "_preLabel' class='preLabel'>" + usuallyXmlEscape(obj.preLabel) + "</span>&nbsp";
	html += buildQuestionInput(obj);
	html += "&nbsp;<span id='" + obj.id + "_postLabel' class='postLabel'>" + usuallyXmlEscape(obj.postLabel) + "</span>";
	html += "<ul id='" + obj.id + "_errors' class='questionErrors'></ul>";
	html += "</div>";
	var result = addToParent(obj.hierarchy, html);
	attachChangeHandler(obj);
	addQuestionWidgets(obj);
	checkQuestionReadonly(obj);	
	return result;
}

/**
 * Update a previously displayed Question.  
 * 
 * @param obj QuestionObject.
 */
function updateQuestion(obj) {
	debugObject("updateQuestion() obj=", obj);
	var error = obj.jq.hasClass("error");
	setElementClass(obj.id, getObjectClass(obj));
	if (error) {
		obj.jq.addClass("error");		
	}
	//if (obj.preLabel == "") {
	//	obj.preLabel = "&nbsp;";
	//}
	getJQElement(obj.id + "_preLabel").text(obj.preLabel);
	getJQElement(obj.id + "_postLabel").text(obj.postLabel);
	removeQuestionWidgets(obj);
	getJQElement(obj.id + "_input").replaceWith(buildQuestionInput(obj));
	attachChangeHandler(obj);
	addQuestionWidgets(obj);
	checkQuestionReadonly(obj);
}

/**
 * Create a new Note and display it.
 * 
 * @param obj NoteObject.
 */
function createNote(obj) {
	debugObject("createNote() obj=", obj);
 	var html = "<div id='" + obj.id + "' class='";
 	html += getObjectClass(obj);
 	html += "'>";
 	var escapedLabel = usuallyXmlEscape(obj.label);
	if (isSpecialStyle(obj, IMAGE_STYLE)) {
 		html += "<img id='" + obj.id + "_image' src='" + escapedLabel + "'/>";
 	}
 	else {
 		html += "<p id='" + obj.id + "_label'>" + escapedLabel + "</p>";
 	}
	html += "</div>";
	return addToParent(obj.hierarchy, html);
}

/**
 * Update a previously displayed Note.
 * 
 * @param obj NoteObject.
 */
function updateNote(obj) {
	debugObject("updateNote() obj=", obj);
	setElementClass(obj.id, getObjectClass(obj));
	if (isSpecialStyle(obj, IMAGE_STYLE)) {
		getJQElement(obj.id + "_image").attr("src", obj.label);
	}
	else {
		getJQElement(obj.id + "_label").text(obj.label);
	}
}

/**
 * Create a new Invalid Answer (validation error) and display it.
 * 
 * @param obj ErrorObject.
 */
function createError(obj) {
	debugObject("createError() obj=", obj);
	var html = "<li id='" + obj.id + "' class='questionError'>";
	html += "<span id='" + obj.id + "_span' class='" + getObjectClass(obj) + "'>" + usuallyXmlEscape(obj.reason) + "</span></li>";
	return addToParent(obj.hierarchy, html);
}

/**
 * Update a previously displayed Invalid Answer (validation error).
 * 
 * @param obj ErrorObject.
function updateError(obj) {
	debugObject("updateError() obj=", obj);
	getJQElement(obj.id + "_reason").text(obj.reason);
}


/**
 * Create a control for the Questionnaire and display it.
 * 
 * @param obj ActionObject.
 */
function createControl(obj) {
	debugObject("createControl() obj=", obj);
	var html;
	if (isSpecialStyle(obj, BUTTON_STYLE)) {	
		html = "<input id='" + obj.id + "' name='" + obj.id + "' type='button' class='"
			+ getObjectClass(obj) + "' value='" + usuallyXmlEscape(obj.label) + "'/>";
	}
	else {
		// Default.
		html = "<a id='" + obj.id + "' href='#' class='" + getObjectClass(obj) + "'>" + 
					usuallyXmlEscape(obj.label) +
			   "</a>";			
	}
	var result = addToParent(obj.hierarchy, html);
	getJQElement(obj.id).unbind().mousedown( 
		function() {
			handleActionEvent(this);
		}
	);
	return result;
}

	
/**
 * Update a previously displayed Control.
 * 
 * @param obj ActionObject.
 */
function updateControl(obj) {
	debugObject("updateControl() obj=", obj);
}

/**
 * Delete the HTML element(s) for the specified object.
 * 
 * @param obj DeleteObject.
 */
function deleteAnyObjectType(obj) {
	debugObject("deleteAnyObjectType() obj=", obj);
	if (obj.error) {		
	    getJQElement(obj.id + "_errors").empty();
	}
	else {
		removeElement(obj.id);
	}
}

/**
 * Returns the CSS class(es) for the primary div of the specified object.
 * 
 * @param obj Fact object.
 * @return String CSS class(es)
 */
function getObjectClass(obj) {
	if (window.onGetObjectClass) {
		// Application-specific override.
		var objectClass = onGetObjectClass(obj);
		if (!isNull(objectClass)) {
			return objectClass;
		}
	}

	// Default.
	var retVal = "";
	switch (obj.objType) {
	case QUESTIONNAIRE_OBJECT:
		retVal += "Questionnaire";
		break;
	case GROUP_OBJECT:
		retVal += "Group";
		break;
	case QUESTION_OBJECT:
		retVal += "Question clearfix";
		break;
	case NOTE_OBJECT:
		retVal += "Note";
		break;
	case ERROR_OBJECT:
		retVal += "InvalidAnswer";
		if (obj.type != null) {
			retVal += " " + obj.type;
		}
		break;
	case ACTION_OBJECT:
		retVal += "Control";
		break;
	}
	if (obj.presentationStyles != null) {
		retVal += " " + obj.presentationStyles.replace(/,/g, " ");
	}
	if (obj.required) {
		retVal += " required";
	}
	return retVal;
}

/**
 * Determines if the specified object has a particular style.
 * 
 * @param obj Fact object.
 * @param style String
 * @return Boolean
 */
function hasStyle(obj, style) {
	return (obj.presentationStyles != null) && (jQuery.inArray(style, obj.presentationStyles.split(",")) >= 0);
}

/**
 * Returns true if a question is readonly.  
 * 
 * @param obj QuestionObject.
 */
function isReadonly(obj) {
	return isSpecialStyle(obj, READONLY_STYLE);	
}

/**
 * Determines if the presentationStyle of the specified object has a special meaning.
 * 
 * @param obj Fact object.
 * @param meaning Integer The special meaning, one of the *_STYLE constants defined above.
 * @return Boolean
 */
function isSpecialStyle(obj, meaning) {
	var retVal = false;
	switch (obj.objType) {
	case QUESTION_OBJECT:
		switch (meaning) {
		case RADIO_STYLE:
			retVal = hasStyle(obj, "radio");
			break;
		case TEXTAREA_STYLE:
			retVal = hasStyle(obj, "textarea");
			break;
		case FILE_STYLE:
			retVal = hasStyle(obj, "file");
			break;
		case DATEPICKER_STYLE:
			retVal = (hasStyle(obj, "datepicker") && !isSpecialStyle(obj, READONLY_STYLE));
			break;
		case READONLY_STYLE:
			retVal = hasStyle(obj, "readonly") || hasStyle(obj, "readonly-inherited");
			break;
		}
		break;
	case NOTE_OBJECT: 
		switch (meaning) {
		case IMAGE_STYLE:
			retVal = hasStyle(obj, "image");
			break;
		}
		break;
	case ACTION_OBJECT: 
		switch (meaning) {
		case BUTTON_STYLE:
			retVal = hasStyle(obj, "button");
			break;
		}
		break;
	}
	return retVal;
}

/**
 * Add an html element to the correct position within the parent.
 * 
 * @param parent HierarchyObject.
 * @param html HTML string for the new element.
 */
function addToParent(hierarchy, html) {
	//debug("addToParent() hierarchy=" + objectToString(hierarchy) + " html=" + html);
	var element = $(html);
	if (hierarchy.before == null) {
		// Insert last.
		getJQElement(hierarchy.parentID).append(element);
	}
	else {
		var jq = getJQElement(hierarchy.before);
		if (jq.length == 0) {
			// Specified sibling doesn't exist yet, insert last.
			getJQElement(hierarchy.parentID).append(element);
		}
		else {
			// Insert before specified sibling.
			jq.before(element);
		}
	}
	return element;
}

/**
 * Returns an html element wrapped in a JQuery object.
 * 
 * @param id The ID of the HTML element to get.
 */
function getJQElement(id) {
	return $("#" + id).eq(0);
}

/**
 * Remove an html element.
 * 
 * @param id The ID of the HTML element to remove.
 */
function removeElement(id) {
	getJQElement(id).remove();
}

/**
 * Set the CSS class of an html element, replacing any existing classes.
 * 
 * @param id String The ID of the HTML element.
 * @param style String The CSS class.
 */
function setElementClass(id, style) {
	var jq = getJQElement(id); 
	jq.removeClass();
	jq.addClass(style);
}
