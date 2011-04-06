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
 * Javascript for the interface between the JQuery Dynamic UI and the Drools Execution Server.
 * 
 * Pre-processes ajax responses into 1-3 ordered lists (create, update and delete) of custom
 * javascript objects representing Facts. In the case of the create list the parent facts will
 * always appear before their children.
 */

// Persistent State (exists across AJAX calls). 
var persistentState = null;
// Temporary state (only exists for the duration of the current AJAX call).
var temporaryState = null;

var useSoap = false;

//==================================================================================================
// START INTERFACE SECTION
// We are going to pretend we have encapsulation, the functions in this section are the only ones
// which should be called from other javascript modules.
//==================================================================================================

function enableSoap(){
    useSoap = true;
}

function disableSoap(){
    useSoap = false;
}

function addSoapEnvelope(request){
    if (!useSoap){
        return request;
    }
    request = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:soap=\"http://soap.jax.drools.org/\"> <soapenv:Header/><soapenv:Body><soap:execute><!--Optional:--><soap:arg0><![CDATA["+request;
    request +="]]></soap:arg0></soap:execute></soapenv:Body></soapenv:Envelope>";

    return request;
}

/**
 * Execute the initial Questionnaire query.
 * 
 * @return ResultSetObject
 */
function getInitialQuestionnaire() {
	var request = 
		"<batch-execution>\n"
	  + "	<fire-all-rules />\n"
	  + "	<query out-identifier='activeObjects' name='activeObjects'/>\n"
	  + "</batch-execution>\n";
	// allows one to customize the batchExcecutioner
	if (window.onBatchExecutionInitialQuestionnaire) {
		request = onBatchExecutionInitialQuestionnaire();
	}	
        request = addSoapEnvelope(request);
	persistentState = new PersistentStateObject();
	temporaryState = new TemporaryStateObject();
	return preProcessServerQuestionnaire(callDrools(request));
}

/**
 * Answer a question and return the changes.
 * 
 * @param questionID String The ID of the question answered.
 * @param answer String The answer.
 * @return ResultSetObject
 */
function setQuestionAnswer(questionID, answer) {
	var request = 
		  "<batch-execution>\n"
		+ "		<insert out-identifier='changes'>\n"
		+ "			<org.tohu.xml.ChangeCollector/>\n"
		+ "		</insert>\n"
		+ "		<insert>\n"
		+ "			<org.tohu.Answer>\n"
		+ "				<questionId>" + xmlEscape(questionID) + "</questionId>\n"
		+ "				<value>" + xmlEscape(answer) + "</value>\n"
		+ "			</org.tohu.Answer>\n"
		+ "		</insert>\n"
		+ "		<fire-all-rules />\n"
	    + "</batch-execution>";
	// allows one to customize the batchExcecutioner	
	if (window.onBatchExecutionQuestionAnswer) {
		request = onBatchExecutionQuestionAnswer(questionID, answer);
	}			 	    	    
	temporaryState = new TemporaryStateObject();
	return preProcessServerChanges(callDrools(request));
}

/**
 * Sets the activeItem of the Questionnaire.
 * 
 * @param activeItem String The ID of the new active item.
 * @return ResultSetObject
 */
function setActiveItem(activeItem) {
	var request = 
		  "<batch-execution>\n"
		+ "		<modify factHandle='" + xmlEscape(persistentState.questionnaire.factHandle) + "'>\n"
		+ "			<set accessor='activeItem' value='\"" + activeItem + "\"' />\n"
		+ "		</modify>\n"
		+ "		<fire-all-rules />\n"
		+ "		<query out-identifier='activeObjects' name='activeObjects' />\n"
	    + "</batch-execution>";
	// allows one to customize the batchExcecutioner	
	if (window.onBatchExecutionActiveItem) {
		request = onBatchExecutionActiveItem(activeItem);
	}			 	    
	// Clear state and start again.
	persistentState = new PersistentStateObject();
	temporaryState = new TemporaryStateObject();
	return preProcessServerQuestionnaire(callDrools(request));
}

//==================================================================================================
// END INTERFACE SECTION
//==================================================================================================

/**
 * This function will re-create the content section representing the
 * questionnaire being processed and what "active group" is being currently
 * handled.  additionally the action points will be dynamically determined 
 * by the questionnaire and what state they are in (e.g validation errors exist)
 * 
 * @param response The XML string of the AJAX response.
 * @return A ResultSetObject with only the createList populated.
 */
function preProcessServerQuestionnaire(response) {
	var retVal = new ResultSetObject();

	var xml = string2xml(response);
	if (xml != null) {
		preProcessServerCreateList("result > query-results > row", xml, false, retVal);

		// Add generated Actions.
		var actions = getQuestionnaireActions(persistentState.questionnaire);
		for (var i = 0; i < actions.length; i++) {
			retVal.createList.push(actions[i]);
			persistentState.actions[actions[i].id] = actions[i];
		}
	}

	debugObject("preProcessServerQuestionnaire() retVal=", retVal);
	return retVal;
}

/**
 * This function will convert the String response into an XML 
 * DOM.
 *
 * The XML DOM is then itereated through using JQuery to 
 * extract all the Fact Objects which will create, update, 
 * or delete HTML elements, thus refreshing the content to 
 * reflect the response of the rules engine
 *
 * @param response The XML string of the AJAX response.
 * @return A ResultSetObject.
 */
function preProcessServerChanges(response) {
	var retVal = new ResultSetObject();

	var xml = string2xml(response);
	if (xml != null) {

		// Process delete list.
		preProcessServerDeleteList("result > * > delete > *", xml, retVal);

		// Process update list.
		preProcessServerUpdateList("result > * > update > *", xml, retVal);

		// Process create list.
		preProcessServerCreateList("result > * > create > entry", xml, true, retVal);

		if (temporaryState.questionnaireChanged) {
			var possibleActions = getQuestionnaireActions(persistentState.questionnaire);			
			var temporaryActions = new Array();						
			// iterate through all the possible actions			
			for (var i = 0; i < possibleActions.length; i++) {
				var possibleAction = possibleActions[i];
				var existingActionFound = false;
				for (actionID in persistentState.actions) {
					var existingAction = persistentState.actions[actionID];
					// if the newly created current action is the same as an existing one
					// then update it the existing one with the new details.					
					if (existingAction.id == possibleAction.id) {
						existingAction.presentationStyles = possibleAction.presentationStyles;
						existingAction.label = possibleAction.label;
						existingAction.actionType = possibleAction.actionType;
						existingAction.action = possibleAction.action;
						existingAction.hierarchy = possibleAction.hierarchy;
						temporaryActions[existingAction.id] = existingAction;
						// we will need to update the html element representing
						// this action.
						retVal.updateList.push(existingAction);
						existingActionFound = true;
					}
				}
				// if the current "possibleAction" did not exist before,
				// then add it to the createList 
				if (existingActionFound == false) {
					retVal.createList.push(possibleAction);
					temporaryActions[possibleAction.id] = possibleAction;
				}				
			}
			persistentState.actions = temporaryActions;
		}
	}
	
	debugObject("preProcessServerChanges() retVal=", retVal);
	return retVal;
}

/**
 * Iterate through the XML response looking for Fact Objects to be created and 
 * update the ResultSetObject with these new Fact Objects.
 * The ResultSetObject will be referenced later to determine which content
 * needs to be added during this rendering process.
 * 
 * @param jQueryPath String The JQuery selection path for the created facts in the ajax response.
 * @param xml XMLDOM The ajax response.
 * @param changes Boolean flag indicating if we are processing changes. 
 * @param resultSet ResultSetObject to populate. 
 */
function preProcessServerCreateList(jQueryPath, xml, changes, resultSet) {
	if (isDebug()) {
		debug("preProcessServerCreateList() jQueryPath=" + jQueryPath + " changes=" + changes
				+ " xml=" + $(xml).toXML() + " resultSet=" + objectToString(resultSet));
	}
	var obj = null;
	var i, j;
	var noLongerPlaceHolders = new Array();
	
	$(jQueryPath, xml).each(function () {
		//debug("preProcessCreateList() found=" + $(this).toXML());
		obj = createFactObject(this, false);
		if (obj != null) {
			var hierarchy = persistentState.hierarchy[obj.id];
			if (isNull(hierarchy) || hierarchy.placeHolder) {
				preProcessServerCreateObject(obj);
				resultSet.createList.push(obj);
			}
			else if (!handleError(ERROR_TYPES.DUPLICATE, [ obj.id ],
						"dynamicUI_interface.preProcessServerCreateList", objectToString(obj))) {
				return;
			}
		}
	});

	// Build up the the hierarchy top-down, start with Questionnnaire or Groups which have known parents.
	if (temporaryState.newQuestionnaire) {
		buildHierarchy(persistentState.questionnaire);
	}
	else {
		for (i = 0; i < temporaryState.groups.length; i++) {
			var group = temporaryState.groups[i];
			//debugObject("preProcessCreateList() group=", group);
			if (!isNull(persistentState.hierarchy[group.id])) {
				buildHierarchy(group);
			}
		}
	}

	// Set parent for all objects except Errors and the Questionnaire.
	for (i = 0; i < resultSet.createList.length; i++) {
		obj = resultSet.createList[i];
		if ((obj.objType != ERROR_OBJECT) && (obj.objType != QUESTIONNAIRE_OBJECT)) {
			obj.hierarchy = cloneObject(persistentState.hierarchy[obj.id]);
			if (!isNull(obj.hierarchy) && obj.hierarchy.placeHolder) {
				// Add this to the list of objects to mark as no longer place holders, have to
				// do it after this step otherwise we might try to insert a new object before
				// another new object which hasn't been created yet.
				noLongerPlaceHolders.push(obj.id);
			}
			// If we are doing changes, work out which non-placeholder sibling to insert this before.
			if (changes && !isNull(obj.hierarchy) && !isNull(obj.hierarchy.parentID)) {
				var siblings = persistentState.hierarchy[obj.hierarchy.parentID].children;
				if (!isNull(siblings)) {
					// Work forward from this object to find the first non-placeholder sibling, if any.
					for (j = (obj.hierarchy.position + 1); j < siblings.length; j++) {
						var sibling = persistentState.hierarchy[siblings[j]];
						if (!isNull(sibling) && !sibling.placeHolder) {
							obj.hierarchy.before = siblings[j];
							break;
						}
					}
				}
			}
		}
	}
			                                     
	// Sort create list so all children appear before their parents and siblings are in order.
	resultSet.createList.sort(sortByHierarchy);
	
	// Reset the place holder flag on new objects.
	for (i = 0; i < noLongerPlaceHolders.length; i++) {
		persistentState.hierarchy[noLongerPlaceHolders[i]].placeHolder = false;
	}
	
	debugPersistentState(persistentState);
}

/**
 * Update the persistentState and temporaryState to reflect the 
 * current "Fact Object"  being processed from the XML response. 
 * 
 * @param obj Fact object.
 */
function preProcessServerCreateObject(obj) {
	debugObject("preProcessServerCreateObject() obj=", obj);
	
	switch (obj.objType) {
	case QUESTIONNAIRE_OBJECT:
		persistentState.questionnaire = obj;
		// Questionnaire is always at the top of the hierarchy.
		persistentState.hierarchy[obj.id] = new HierarchyObject(null, 0, 0, false);
		temporaryState.newQuestionnaire = true;
		temporaryState.questionnaireChanged = true;
		temporaryState.items[obj.id] = obj;
		break;
	case GROUP_OBJECT:
		temporaryState.groups.push(obj);
		temporaryState.items[obj.id] = obj;
		break;
	case QUESTION_OBJECT:
		temporaryState.items[obj.id] = obj;
		break;
	case NOTE_OBJECT:
		temporaryState.items[obj.id] = obj;
		break;
	case ERROR_OBJECT:
		break;
	default:
		handleError(ERROR_TYPES.INTERNAL, [ "invalid object type: " + obj.objType ],
				"dynamicUI_interface.preProcessServerCreateObject", objectToString(obj));
	}
}

/**
 * Iterate through the XML response looking for Fact Objects to be updated and 
 * update the ResultSetObject with these Fact Objects.
 * The ResultSetObject will be referenced later to determine which content
 * needs to be updated during this rendering process.
 * 
 * @param jQueryPath String The JQuery selection path for the updated facts in the ajax response.
 * @param xml XMLDOM The ajax response.
 * @param resultSet ResultSetObject to populate. 
 */
function preProcessServerUpdateList(jQueryPath, xml, resultSet) {
	if (isDebug()) {
		debug("preProcessServerUpdateList() jQueryPath=" + jQueryPath + " xml=" + $(xml).toXML()
				+ " resultSet=" + objectToString(resultSet));
	}
	
	$(jQueryPath, xml).each(function () {
		var obj = createFactObject(this, false);
		if (obj != null) {
			debug("preProcessServerUpdateList() id=" + obj.id);
			preProcessServerUpdateObject(obj);
			resultSet.updateList.push(obj);
		}
	});
}

/**
 * update the Fact, TemporaryState and PersistentState Objects
 * and check if the Facts Object needs to add children to the 
 * the existing hierarchy
 *
 * @param obj Fact object.
 */
function preProcessServerUpdateObject(obj) {
	debugObject("preProcessServerUpdateObject() obj=", obj);
	if (obj.objType == QUESTIONNAIRE_OBJECT) {
		// Don't lose the factHandle.
		obj.factHandle = persistentState.questionnaire.factHandle;
		persistentState.questionnaire = obj;
		temporaryState.questionnaireChanged = true;
		buildHierarchy(obj);
	}
	else if (obj.objType == GROUP_OBJECT) {
		buildHierarchy(obj);
	}
}

/**
 * Iterate through the XML response looking for Fact Objects to be deleted and 
 * Update the ResultSetObject with these Fact Objects.
 * The ResultSetObject will be referenced later to determine which content
 * needs to be removed during this rendering process.
 * 
 * @param jQueryPath String The JQuery selection path for the deleted facts in the ajax response.
 * @param xml XMLDOM The ajax response.
 * @param resultSet ResultSetObject to populate. 
 */
function preProcessServerDeleteList(jQueryPath, xml, resultSet) {
	if (isDebug()) {
		debug("preProcessServerDeleteList() jQueryPath=" + jQueryPath + " xml=" + $(xml).toXML()
				+ " resultSet=" + objectToString(resultSet));
	}
	
	$(jQueryPath, xml).each(function () {
		var obj = createFactObject(this, true);
		if (obj != null) {
			preProcessServerDeleteObject(obj);
			resultSet.deleteList.push(obj);
		}
	});
}

/**
 * update the hierarchy reflecting that this
 * Fact Object is to be deleted.
 *
 * @param obj Fact object.
 */
function preProcessServerDeleteObject(obj) {
	debugObject("preProcessServerDeleteObject() obj=", obj);
	
	if (!obj.error) {
		var hierarchy = persistentState.hierarchy[obj.id];
		if (!isNull(hierarchy)) {
			hierarchy.placeHolder = true;
		}
	}
}

/**
 * Add the Fact Object's children to the hierarchy, assumes the object itself has already been added.
 * 
 * @param obj Fact object.
 */
function buildHierarchy(obj) {
	debugObject("buildHierarchy() obj=", obj);
	// Add children.
	if ((obj.objType == QUESTIONNAIRE_OBJECT) || (obj.objType == GROUP_OBJECT)) {
		var hierarchy = persistentState.hierarchy[obj.id];
		hierarchy.children = obj.items;
		for (var i = 0; i < obj.items.length; i++) {
			var childID = obj.items[i];
			var childHierarchy = persistentState.hierarchy[childID];
			var child = temporaryState.items[childID];
			if (isDebug()) {
				debug("buildHierarchy() childID=" + childID + " childHierarchy="
						+ objectToString(childHierarchy) + " child=" + objectToString(child));
			}
			if (!isNull(child)) {
				persistentState.hierarchy[childID] =
						new HierarchyObject(obj.id, i, (hierarchy.level + 1), false);
				buildHierarchy(child);
			}
			else if (isNull(childHierarchy)) {
				persistentState.hierarchy[childID] =
						new HierarchyObject(obj.id, i, (hierarchy.level + 1), true);
			}
		}
	}
}

/**
 * Sort a list of Fact Objects so parents appear before their children (by level then by parentID then
 * by position).
 * 
 * @param obj1 First object to compare.
 * @param obj2 Second object to compare.
 * @return 0 = same, -1 = obj1 before obj2, 1 = obj2 before obj1
 */
function sortByHierarchy(obj1, obj2) {
	var retVal = 0;
	if (!isNull(obj1) && !isNull(obj2)) {
		if (!isNull(obj1.hierarchy) && !isNull(obj2.hierarchy)) {
			//debug("sortByHierarchy() obj1.level=" + obj1.hierarchy.level + " obj2.level=" + obj2.hierarchy.level);
			var cmp = (obj1.hierarchy.level - obj2.hierarchy.level);
			if (cmp == 0) {
				if (obj1.hierarchy.parentID == obj2.hierarchy.parentID) {
					if (!isNull(obj1.hierarchy.position) && !isNull(obj2.hierarchy.position)) {
						cmp = (obj1.hierarchy.position - obj2.hierarchy.position);
					}
				}
				else if (obj1.hierarchy.parentID > obj2.hierarchy.parentID) {
					cmp = 1;
				}
				else {
					cmp = -1;
				}
			}
			if (cmp > 0) {
				retVal = 1;
			}
			else if (cmp < 0) {
				retVal = -1;
			}
		}
		//debug("sortByHierarchy() obj1=" + obj1.id + " obj2=" + obj2.id + " retVal=" + retVal);
	}
	return retVal;
}

/**
 * Generates a list of Action objects for the specified Questionnaire object.
 * 
 * @param questionnaire QuestionnaireObject.
 * @return Array of ActionObjects. 
 */
function getQuestionnaireActions(questionnaire) {
	if (window.onGetQuestionnaireActions) {
		// Allow application-specific override of actions.
		var questionnaireActions = onGetQuestionnaireActions(questionnaire);
		if (!isNull(questionnaireActions)) {
			return questionnaireActions;
		}
	}

	// Default actions.
	var retVal = new Array();
	var obj = null;
	var activeIndex = null;

	if (questionnaire.activeItem && (questionnaire.activeItem != "")) {
		activeIndex = jQuery.inArray(questionnaire.activeItem, questionnaire.availableItems);
	}
	if (activeIndex >  0) {
		// Not first item.
		obj = new ActionObject();
		obj.id = questionnaire.id + "_action_0";
		obj.presentationStyles = "previous";
		obj.label = "Previous";
		obj.actionType = "setActive";
		obj.action = questionnaire.availableItems[activeIndex - 1];
		obj.hierarchy = new HierarchyObject(questionnaire.id, 0, 1, false);
		retVal.push(obj);
	}
	if ((activeIndex == null) || (activeIndex == (questionnaire.availableItems.length - 1))) {
		// Last item.
		obj = new ActionObject();
		obj.id = questionnaire.id + "_action_1";
		var isReturn = questionnaire.completionAction == "#return"; 
		var enforceErrors = true;
		if (isReturn) {
			obj.presentationStyles = "next,return";
			obj.label = "Return";
			
			if (questionnaire.hasErrors && window.overrideReturnEnforceErrors) {
				enforceErrors = overrideReturnEnforceErrors();
			}
		}
		else {
			obj.presentationStyles = "next,done";
			obj.label = "Done";
		}
		// TODO Add configurable ability to report on errors
		// based on the BRANCHED pages only
		
		if (questionnaire.hasErrors && enforceErrors) {
			obj.actionType = "showError";
			obj.action = "You must fix all errors first.";
		}
		else {
			if (isReturn) {
				obj.actionType = "setActive";
			}
			else {
				obj.actionType = "completion";
			}
			obj.action = questionnaire.completionAction;
		}
		obj.hierarchy = new HierarchyObject(questionnaire.id, 1, 1, false);
		retVal.push(obj);
	}
	else {
		// Not last item.
		obj = new ActionObject();
		obj.id = questionnaire.id + "_action_1";
		obj.presentationStyles = "next";
		obj.label = "Next";
		
		if (questionnaire.enableActionValidation && questionnaire.hasErrors) {
			obj = processValidation(obj);
		} else {
			obj.actionType = "setActive";
			obj.action = questionnaire.availableItems[activeIndex + 1];			
		}
		obj.hierarchy = new HierarchyObject(questionnaire.id, 1, 1, false);
		retVal.push(obj);
	}
	return retVal;
}

 /**
  * This will determine what reaction the external app will have
  * to validation errors. A hook has being added for the
  * external app to customize what behavior it would like to perform
  * otherwise the default behavior is performed.
  */
 function processValidation(obj) {
	 if (window.onProcessValidation) {
		 var updatedObj = onProcessValidation(obj);
		 if (!isNull(updatedObj)) {
			 return updatedObj;
		 }
	 }
	 obj.actionType = "showError";
	 obj.action = "Not all mandatory questions have being answered";	 
	 return obj;
 }
 
/**
 * Create a custom object for the specified xml fact.
 * 
 * @param xml XML DOM of the query result row containing the fact.
 * @param isDelete Boolean flag indicating that we are processing deletes
 * @return Custom javascript object or null if xml is not a valid fact.
 */
function createFactObject(xml, isDelete) {
	//debug("createFactObject() in " + $(xml).toXML());
	var obj = null;
	var items = null;
	var jq = $(xml);
	// JQuery doesn't like .'s in XML tag names, so get the name from the wrapped XML DOM. 
	var tagName = jq.get(0).tagName;
	if ((tagName == "row") || (tagName == "entry")) {
		// Initial query wraps facts in "row" tags, the ChangeCollector wraps the create list in "entry" tags.
		jq = $(xml).children(":first");
		tagName = jq.get(0).tagName;
	}
	if (isDebug()) {
		debug("createFactObject() tagName=" + tagName + " xml=" + jq.toXML());
	}
	switch (tagName) {
	case "org.tohu.Questionnaire":
		obj = new QuestionnaireObject();
		obj.id = getChildText(jq, "id");
		// TODO this meeds some more work to convert date formats specified in some industry standard way into those supported by the JQuery $.datePicker.formatDate function 
		obj.clientDateFormat = getChildText(jq, "clientDateFormat", "").replace(/yy/g, "y");
		obj.presentationStyles = getChildText(jq, "presentationStyles");
		obj.label = getChildText(jq, "label", "");
		items = getChildText(jq, "items");
		if (items) {
			obj.items = items.split(",");
		}
		var availableItems = getChildText(jq, "availableItems");
		if (availableItems) {
			obj.availableItems = availableItems.split(",");
		}
		obj.activeItem = getChildText(jq, "activeItem");
		obj.completionAction = getChildText(jq, "completionAction");
		obj.enableActionValidation = getChildBoolean(jq, "enableActionValidation");
		obj.hasErrors = getChildBoolean(jq, "invalidAnswers");
		obj.factHandle = $("fact-handle", xml).attr("externalForm");
		obj.markupAllowed = getChildBoolean(jq, "markupAllowed");
		break;
	case "org.tohu.Group":
		obj = new GroupObject();
		obj.id = getChildText(jq, "id");
		obj.presentationStyles = getChildText(jq, "presentationStyles");
		obj.label = getChildText(jq, "label", "");
		items = getChildText(jq, "items");
		if (items) {
			obj.items = items.split(",");
		}
		break;
	case "org.tohu.Question":
	case "org.tohu.MultipleChoiceQuestion":
		obj = new QuestionObject();
		obj.id = getChildText(jq, "id");
		obj.presentationStyles = getChildText(jq, "presentationStyles");
		obj.preLabel = getChildText(jq, "preLabel", "");
		obj.postLabel = getChildText(jq, "postLabel", "");
		obj.required = getChildBoolean(jq, "required");
		var possibleAnswers = getChildText(jq, "possibleAnswers");
		if (possibleAnswers && (possibleAnswers != "")) {
			obj.possibleAnswers = new Array();
			var tempArray = splitWithEscapes(possibleAnswers, ",");
			for (var i = 0; i < tempArray.length; i++) {
				var s = tempArray[i];
				obj.possibleAnswers.push(splitWithEscapes(s, "="));
			}
		}
		obj.answerType = getChildText(jq, "answerType");
		if (obj.answerType) {
			var pos = obj.answerType.indexOf('.');
			if (pos >= 0) {
				obj.answerType = obj.answerType.substring(0, pos);
			}
			switch (obj.answerType) {
			case "text":
				obj.answer = getChildText(jq, "textAnswer", "");
				break;
			case "number":
				obj.answer = getChildText(jq, "numberAnswer", "");
				break;
			case "decimal":
				obj.answer = getChildText(jq, "decimalAnswer", "");
				break;
			case "boolean":
				obj.answer = getChildText(jq, "booleanAnswer", "");
				break;
			case "date":
				obj.answer = getChildText(jq, "dateAnswer", "");
				break;
			case "list":
				obj.answer = getChildText(jq, "listAnswer", "");
				break;
			default:
				if (!handleError(ERROR_TYPES.RULES_DEFINITION, [ obj.id, "unknown answerType: " + obj.answerType ],
						"dynamic_interface.createFactObject", objectToString(obj))) {
					obj = null;
				}
			}
		} 
		else if (!handleError(ERROR_TYPES.RULES_DEFINITION, [ obj.id, "no answerType" ],
				"dynamic_interface.createFactObject", objectToString(obj))) {
			obj = null;
		}
		break;
	case "org.tohu.Note":
		obj = new NoteObject();
		obj.id = getChildText(jq, "id");
		obj.presentationStyles = getChildText(jq, "presentationStyles");
		obj.label = getChildText(jq, "label", "");
		break;
	case "org.tohu.InvalidAnswer":
		var questionID = getChildText(jq, "questionId");
		var reason = getChildText(jq, "reason", "");
		if (isDelete) {
			obj = new DeleteObject(true, questionID, reason);
		}
		else {	
			obj = new ErrorObject();
			obj.id = questionID + "_error_" + (persistentState.errorSequence++);
			obj.type = getChildText(jq, "type");
			obj.reason = reason
			// Errors are assigned level 999 so they always sort last.
			obj.hierarchy = new HierarchyObject(questionID, null, 999, false);
		}
		break;
	case "org.tohu.xml.ItemId":
		obj = new DeleteObject(false, getChildText(jq, "id"));
		break;
	default:
		if (!handleError(ERROR_TYPES.INTERNAL, [ "unknown tagName: " + tagName ],
				"dynamic_interface.createFactObject", objectToString(obj))) {
			obj = null;
		}
		obj = null;
	}
	// All objects must have an id otherwise the XML was garbage.
	if ((obj != null) && isNull(obj.id)) {
		if (!handleError(ERROR_TYPES.RULES_DEFINITION, [ "undefined", "no id" ],
				"dynamic_interface.createFactObject", objectToString(obj))) {
			obj = null;
		}
	}
	return obj;
}

/**
 * Return a single immediate child of an XML element.
 * 
 * @param xml Either an XML DOM or a JQuery object containing an XML DOM.
 * @param child Name of the child to return (assumed to be unique).
 * @return XML DOM The child or null if no such child.
 */
function getChild(xml, child) {
	if (! xml.jquery) {
		xml = $(xml);
	}
	//debug("getChild() child=" + child + " xml=" + xml.toXML());
	var retVal = xml.children(child);
	//debug("getChild() retVal=" + $(retVal).toXML());
	if (retVal.length == 0) {
		retVal = null;
	}
	else if (retVal.length > 1) {
		retVal = retVal.eq(0);
	}
	//debug("getChild() retVal=" + retVal);
	return retVal;
}

/**
 * Return the text of an immediate child of an XML element.
 * 
 * @param xml Either an XML DOM or a JQuery object containing an XML DOM.
 * @param child Name of the child to return (assumed to be unique).
 * @param defaultText String Optional default text to return.
 * @return String The child text (with whitespace trimmed) or the default text if no such child.
 */
function getChildText(xml, child, defaultText) {
	//debug("getChildText() xml=" + xml.toXML() + " child=" + child + " defaultText=" + defaultText);
	var retVal = null;
	var childXml = getChild(xml, child);
	if (childXml != null) {
		if (childXml.jquery) {
			retVal = jQuery.trim(childXml.text());
		}
		else {
			retVal = jQuery.trim($(childXml).text());
		}
	}
	//debug("getChildText() retVal=" + retVal);
	if (retVal == null) {
		if (!isNull(defaultText)) {
			retVal = defaultText;
		}
		else {
			retVal = null;
		}
	}
	//debug("getChildText() retVal=" + retVal);
	return retVal;
}

/**
 * Return the boolean value of an immediate child of an XML element.
 * 
 * @param xml Either an XML DOM or a JQuery object containing an XML DOM.
 * @param child Name of the child to return (assumed to be unique).
 * @return Boolean value of child or null if there is no such child or it isn't a boolean value.
 */
function getChildBoolean(xml, child) {
	var childText = getChildText(xml, child);
	if (childText == "true") {
		return true;
	}
	else if (childText == "false") {
		return false;
	}
	else {
		return null;
	}
}
