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
 * Custom Javascript objects for JQuery Dynamic UI.
 */

// State Objects.

/**
 * Custom object for the temporary interface state (Stateless - only exists for the duration of the current AJAX call).
 */
function TemporaryStateObject() {
	// Boolean flag indicating if the Questionnaire fact has been created.
	this.newQuestionnaire = false;
	// Boolean flag indicating if the Questionnaire fact has been created or updated.
	this.questionnaireChanged = false;
	// Array of Questionnaire and Group objects. 
	this.groups = new Array();
	// Hash map of Questionnnaire, Group, Question and Note objects, keyed by ID. 
	this.items = new Object();
}

/**
 * Custom object for the persistent interface state (Stateful - exists across AJAX calls).
 */
function PersistentStateObject() {
	// The Questionnaire fact object. 
	this.questionnaire = null;
	// Hash map of HierarchyObject's for non-error objects, keyed by ID.
	this.hierarchy = new Object();
	// Hash map of Questionnaire Actions, keyed by ID.
	this.actions = new Object();
	// Sequence number to generate unique IDs for errors.
	this.errorSequence = 0;
}

// Fact Objects.

// Object type constants.
var QUESTIONNAIRE_OBJECT = 0;
var GROUP_OBJECT = 1;
var QUESTION_OBJECT = 2;
var NOTE_OBJECT = 3;
var ERROR_OBJECT = 4;
var ACTION_OBJECT = 5;
var DELETE_OBJECT = 6;
var HIERARCHY_OBJECT = 7;
var RESULT_SET_OBJECT = 8;

/**
 * Custom object for a fact object's position within the hierarchy.
 */
function HierarchyObject(parentID, position, level, placeHolder) {
	this.objType = HIERARCHY_OBJECT;
	// ID of parent object (root node = null).
	this.parentID = parentID;
	// Integer position within parent's children (0 .. n-1).
	this.position = position;
	// Integer level of the object within the hierarchy (root node = 0).
	this.level = level;
	// Array of child ID's for Questionnaire and Group objects.
	this.children = null;
	// Boolean flag indicating if this is a place holder for a child which doesn't currently exist.
	this.placeHolder = placeHolder;
	// ID of the sibling to insert this child before.
	this.before = null;
}

/**
 * Custom object for the Questionnaire fact.
 */
function QuestionnaireObject() {
	this.objType = QUESTIONNAIRE_OBJECT;
	this.id = null;
	this.jq = null;
	this.presentationStyles = null;
	this.label = null;
	// Array of child IDs.
	this.items = new Array();
	this.availableItems = new Array();
	this.activeItem = null;
	this.completionAction = null;
	this.enableActionValidation = null;
	this.hierarchy = new HierarchyObject(null, 0, 0, false);
	// Handle for the Questionnaire fact.
	this.factHandle = null;
	// Boolean flag indicating if the Questionnaire has errors (InvalidAnswers)
	this.hasErrors = null;
	this.clientDateFormat = null;
	this.markupAllowed = null;
}

/**
 * Custom object for the Group fact.
 */
function GroupObject() {
	this.objType = GROUP_OBJECT;
	this.id = null;
	this.jq = null;
	this.presentationStyles = null;
	this.label = null;
	// Array of child IDs.
	this.items = new Array();
	this.hierarchy = null;
}

/**
 * Custom object for the Question and MultipleChoiceQuestion facts.
 */
function QuestionObject() {
	this.objType = QUESTION_OBJECT;
	this.id = null;
	this.jq = null;
	this.presentationStyles = null;
	this.preLabel = null;
	this.postLabel = null;
	this.answerType = null;
	this.answer = null;
	// Boolean flag.
	this.required = null;
	// Array of 2-element [value,label] arrays.
	this.possibleAnswers = null;
	this.hierarchy = null;
}

/**
 * Custom object for the Note fact.
 */
function NoteObject() {
	this.objType = NOTE_OBJECT;
	this.id = null;
	this.jq = null;
	this.presentationStyles = null;
	this.label = null;
	this.hierarchy = null;
}

/**
 * Custom object for the InvalidAnswer fact.
 */
function ErrorObject() {
	this.objType = ERROR_OBJECT;
	this.id = null;
	this.jq = null;
	this.type = null;
	this.reason = null;
	this.hierarchy = null;
}

/**
 * Custom object for the Action fact (currently generated in the interface module, not from the server).
 */
function ActionObject() {
	this.objType = ACTION_OBJECT;
	this.id = null;
	this.jq = null;
	this.presentationStyles = null;
	this.label = null;
	this.actionType = null;
	this.action = null;
	this.hierarchy = null;
}

/**
 * Custom object for deleting an object.
 */
function DeleteObject(error, id, reason) {
	this.objType = DELETE_OBJECT;
	this.error = error;
	this.id = id;
	this.reason = reason;
}

// Interface Objects.

/**
 * Interface Result Set object.
 */
function ResultSetObject() {
	this.objType = RESULT_SET_OBJECT;
	// List of objects to create.
	this.createList = new Array();
	// List of objects to update.
	this.updateList = new Array();
	// List of objects to delete.
	this.deleteList = new Array();
}

// Custom Object Helper Functions.

/**
 * Displays the size and IDs in an object array. 
 */
function objectListToString(list) {
	var retVal = null;
	if (list) {
		retVal = "[" + list.length + "]";
		for (var i = 0; i < list.length; i++) {
			retVal += " " + list[i].id;
		}
	}
	return retVal;
}

/**
 * Returns a string representation of the specified object.
 */
function objectToString(obj) {
	var retVal = null;
	if (obj) {
		switch (obj.objType) {
		case QUESTIONNAIRE_OBJECT: 
			retVal = "QUESTIONNAIRE: id=" + obj.id + " items=" + arrayToString(obj.items) + " availableItems=" + arrayToString(obj.availableItems)
				+ " activeItem=" + obj.activeItem + " hasErrors=" + obj.hasErrors
				+ " factHandle=" + obj.factHandle;
			break;
		case GROUP_OBJECT: 
			retVal = "GROUP: id=" + obj.id + " items=" + arrayToString(obj.items) + " hierarchy="
				+ objectToString(obj.hierarchy);
			break;
		case QUESTION_OBJECT: 
			retVal = "QUESTION: id=" + obj.id + " answer=" + obj.answer + " hierarchy="
				+ objectToString(obj.hierarchy);
			break;
		case NOTE_OBJECT: 
			retVal = "NOTE: id=" + obj.id + " hierarchy=" + objectToString(obj.hierarchy);
			break;
		case ERROR_OBJECT: 
			retVal = "ERROR: id=" + obj.id + " hierarchy=" + objectToString(obj.hierarchy);
			break;
		case DELETE_OBJECT: 
			retVal = "DELETE: " + (obj.error ? "error id=" + obj.id + " reason=" + obj.reason : "id=" + obj.id);
			break;
		case HIERARCHY_OBJECT: 
			retVal = "HIERARCHY: parentID=" + obj.parentID + " position=" + obj.position
				+ " level=" + obj.level + " children=" + arrayToString(obj.children)
				+ " placeHolder=" + obj.placeHolder + " before=" + obj.before;
			break;
		case RESULT_SET_OBJECT: 
			retVal = "RESULTSET:\n\tcreateList=" + objectListToString(obj.createList)
				+ "\n\tupdateList=" + objectListToString(obj.updateList)
				+ "\n\tdeleteList=" + objectListToString(obj.deleteList);
			break;
		default:
			retVal = jQuery.param(obj);
		}
	}
	return retVal; 
}

/**
 * Displays the persistent state for debugging.
 * 
 * @param state PersistentStateObject
 */
function debugPersistentState(state) {
	if (isDebug()) {
		debug("debugPersistentState() questionnaire=" + objectToString(state.questionnaire));
		for (id in state.hierarchy) {
			debug("debugPersistentState() hierarchy[" + id + "]=" + objectToString(state.hierarchy[id]));
		}
		for (id in state.actions) {
			debug("debugPersistentState() actions[" + id + "]=" + objectToString(state.actions[id]));
		}
	}
}

/**
 * Display a debug message and a text representation of the specified object if either FireBug
 * (for FireFox) or Web Development Helper (for IE) is installed.
 * 
 * @param msg String Message to display.
 * @param obj Custom javascript object to display.
 */
function debugObject(msg, obj) {
	if (isDebug()) {
		debugFull(msg + objectToString(obj));
	}
}

/**
 * Returns a copy of the specified object.
 */
function cloneObject(obj) {
	var retVal = null;
	if (obj) {
		if (obj.objType == HIERARCHY_OBJECT) {
			retVal = new HierarchyObject(obj.parentID, obj.position, obj.level, obj.placeHolder);
			retVal.before = obj.before;
			if (!isNull(obj.children)) {
				retVal.children = new Array();
				for (var i = 0; i < obj.children.length; i++) {
					retVal.children[i] = obj.children[i];
				}
			}
		}
		else {
			handleError(ERROR_TYPES.INTERNAL, [ "cloneObject() not implemented for this object type: " + obj.objType ],
					"dynamicUI_objects.cloneObject", objectToString(obj));
		}
	}
	return retVal;
}