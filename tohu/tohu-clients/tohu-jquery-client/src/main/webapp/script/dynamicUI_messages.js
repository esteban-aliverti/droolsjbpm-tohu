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
 * JavaScript message text constants.
 */

// Message display method constants.
var DISPLAY_TYPES = { ALERT: {}, STATUS: {}, NONE: {} };

// Standard error constants.
var ERROR_TYPES = {
	// Coding error.
	INTERNAL: {
		NAME: "Internal Error",
		TEXT: "An internal error has occured: %s, contact support.",
		PARAMS: 1,
		DISPLAY: DISPLAY_TYPES.ALERT,
		LOG: true,
		CONTINUE: false 
	},
	// The AJAX call to the execution server failed for whatever reason.
	AJAX_CALL: {
		NAME: "Ajax Call Error",
		TEXT: "An error has occured calling the rules server, refresh the page and try again.",
		PARAMS: 0,
		DISPLAY: DISPLAY_TYPES.ALERT,
		LOG: true,
		CONTINUE: false 
	},
	// The execution server session timed out.
	SESSION_TIMEOUT: {
		NAME: "Session Timeout Error",
		TEXT: "Your session has timed out, refresh the page and start again.",
		PARAMS: 0,
		DISPLAY: DISPLAY_TYPES.ALERT,
		LOG: false,
		CONTINUE: false 
	},
	// An invalid date was entered and the datepicker threw an exception.
	INVALID_DATE: {
		NAME: "Invalid Date Error",
		TEXT: "%s is not a valid date, please re-enter.",
		PARAMS: 1,
		DISPLAY: DISPLAY_TYPES.ALERT,
		LOG: false,
		CONTINUE: true 
	},
	// Invalid XML was returned from the execution server.
	XML_PARSE: {
		NAME: "XML Parse Error",
		TEXT: "The rules server has returned invalid XML, contact support.",
		PARAMS: 0,
		DISPLAY: DISPLAY_TYPES.ALERT,
		LOG: true,
		CONTINUE: false 
	},
	// The rules have not been defined properly.
	RULES_DEFINITION: {
		NAME: "Rules Definition Error",
		TEXT: "Fact %s in the rule base is invalid: %s, revise the rule base.",
		PARAMS: 2,
		DISPLAY: DISPLAY_TYPES.ALERT,
		LOG: true,
		CONTINUE: false 
	},
	// An object has been created with no parent (could be either a coding error or incorrect rules).
	NO_PARENT: {
		NAME: "No Parent Error",
		TEXT: "The %s fact does not have a parent, revise the rule base.",
		PARAMS: 1,
		DISPLAY: DISPLAY_TYPES.ALERT,
		LOG: true,
		CONTINUE: true 
	},
	// Trying to create an object which is already displayed (could be either a coding error or incorrect rules).
	DUPLICATE: {
		NAME: "Duplicate Error",
		TEXT: "A duplicate of the %s fact has been sent by the rules server, revise the rule base.",
		PARAMS: 1,
		DISPLAY: DISPLAY_TYPES.ALERT,
		LOG: true,
		CONTINUE: true 
	}
};

