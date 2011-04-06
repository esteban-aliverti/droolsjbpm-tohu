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
 * JavaScript utility functions.
 */

// Boolean flag indicating if debug messages should be printed.
var debugFlag = null;

/**
 * Gets the debug flag.
 * 
 * @return Boolean
 */
function getDebugFlag() {
	return debugFlag;
}

/**
 * Sets the debug flag.
 * 
 * @param flag Boolean.
 */
function setDebugFlag(flag) {
	debugFlag = flag;
}

/**
 * Display a debug message (of up to 1000 characters) if either FireBug (for FireFox) or Web
 * Development Helper (for IE) is installed.
 * 
 * @param msg String to display.
 */
function debug(msg) {
	if (debugFlag) {
		debugFull(msg.substr(0, 1000));
	}
}

/**
 * Display a debug message (of any length) if either FireBug (for FireFox) or Web
 * Development Helper (for IE) is installed.
 * 
 * @param msg String to display.
 */
function debugFull(msg) {
	if (debugFlag) {
		// Add timestamp to message.
		var now = new Date();
		msg = now.getFullYear() + "/" + zeroPad(now.getMonth(), 2) + "/" + zeroPad(now.getDay(), 2) + " "
			+ zeroPad(now.getHours(), 2) + ":" + zeroPad(now.getMinutes(), 2) + ":" + zeroPad(now.getSeconds(), 2) + "." + zeroPad(now.getMilliseconds(), 3)
			+ " " + msg;
		if (window.debugService) {
			// Web Development Helper (IE)
			window.debugService.trace(msg);
		}
		else if (window.console) {
			// Firebug (Firefox)
			window.console.log(msg);
		}
		else {
			//alert(msg);
		}
	}
}

/**
 * Pads a number with leading zeros.
 */
function zeroPad(n, digits) {
	var s = n.toString();
	while (s.length < digits) {
		s = "0" + s;
	}
	return s;
}

/**
 * Returns true if either FireBug (for FireFox) or Web Development Helper (for IE) is installed.
 * 
 * @return Boolean
 */
function isDebug() {
	if (debugFlag) {
		if (window.debugService) {
			// Web Development Helper (IE)
			return true;
		}
		else if (window.console) {
			// Firebug (Firefox)
			return true;
		}
	}
	return false;
}

/**
 * Displays the size and elements of an array. 
 */
function arrayToString(anArray) {
	var retVal = null;
	if (anArray) {
		retVal = "Array(" + anArray.length + "): [";
		for (i in anArray) {
			if (i == 0) {
				retVal += anArray[i];
			}
			else {
				retVal += ", " + anArray[i];
			}
		}
		retVal += "]";
	}
	return retVal;
}

/**
 * Display an error message.
 * @param msg String to display.
 */
function showError(msg) {
	alert(msg);
}

/**
 * Get the contents of a browser cookie.
 * 
 * @param cookieName Name of the cookie.
 * @return The cookie contents
 */
function getCookie(cookieName) {
	return $.cookie(cookieName);
}

/**
 * Clear a browser cookie.
 * 
 * @param cookieName Name of the cookie.
 */
function clearCookie(cookieName) {
	debug("clearCookie() cookieName=" + cookieName);
	$.cookie(cookieName, null);
}

/**
 * Determines if two strings are identical, either or both can be null/undefined.
 * 
 * @param str1 String 1.
 * @param str2 String 2.
 * @return boolean True if str1 and str2 are identical or both are null/undefined.
 */
function identicalString(str1, str2) {
	//debug("identicalString() str1=" + str1 + " str2=" + str2);
	var retVal = false;
	if (str1) {
		if (str2) {
			retVal = (str1 == str2);
		}
		else {
			retVal = false;
		}
	}
	else {
		if (str2) {
			retVal = false;
		}
		else {
			retVal = true;
		}
	}
	//debug("identicalString() retVal=" + retVal);
	return retVal;
}

/**
 * Determines if two arrays of strings are identical, either or both can be null/undefined.
 * 
 * @param array1 Array of strings 1.
 * @param array2 Array if strings 2.
 * @return boolean True if array1 and array2 are identical or both are null/undefined.
 */
function identicalArray(array1, array2) {
	//debug("identicalArray() array1=" + array1 + " array2=" + array2);
	var retVal = false;
	if (array1) {
		if (array2) {
			if (array1.length != array2.length) {
				retVal = false;
			}
			else {
				retVal = true;
				for (var i = 0; i < array1.length; i++) {
					if (array1[i] != array2[i]) {
						retVal = false;
						break;
					}
				}
			}
		}
		else {
			retVal = false;
		}
	}
	else {
		if (array2) {
			retVal = false;
		}
		else {
			retVal = true;
		}
	}
	//debug("identicalArray() retVal=" + retVal);
	return retVal;
}

/**
 * Return true if a javascript variable is undefined or null.
 * 
 * @param val The variable to test.
 * @return boolean
 */
function isNull(val) {
	return ((val == undefined) || (val == null));
}

/**
 * Return true if a javascript variable is undefined, null or the empty string.
 * 
 * @param val The variable to test.
 * @return boolean
 */
function isBlank(val) {
	if (isNull(val)) {
		return true;
	}
	else {
		return (val == "");
	}
}

/**
 * Convert an XML string to an XML DOM object.
 * 
 * @param xml XML string.
 * @return XML DOM.
 */
function string2xml(xml) {
	//debug("string2xml() xml=" + xml);
	if (!isNull(xml)) {
		if (window.ActiveXObject && window.GetObject) {
			// IE
			var dom = new ActiveXObject('Microsoft.XMLDOM');
			dom.async = 'false';
			dom.loadXML(xml);
			if (dom.parseError.errorCode != 0) {
				if (!handleError(ERROR_TYPES.XML_PARSE, null, "dynamicUI_utility.string2xml(1)",
						"errorCode=" + dom.parseError.errorCode + " reason=" + dom.parseError.reason
							+ " line=" + dom.parseError.line + " linePos=" + dom.parseError.linePos
							+ " srcText=" + dom.parseError.srcText)) {
					return null;
				}
			}
			//debug("string2xml() 1 dom=" + dom);
			return dom.documentElement;
		}
		if (window.DOMParser) {
			// Firefox etc
			var parser = new DOMParser();
			var dom = parser.parseFromString(xml, 'text/xml');
			// Chrome will for some reason except pure text, i.e. 
			// non-xml or non-html text
			// as a valid string to be converted to xml.  Therefore we've 
			// tightened up the condition such that we now expect the root 
			// element to be "execution-results"
			if (dom.documentElement.tagName != "execution-results") {				
				if (!handleError(ERROR_TYPES.XML_PARSE, null, "dynamicUI_utility.string2xml(2)",
						dom.documentElement.firstChild.nodeValue)) {
					return null;
				} 
			}
			//debug("string2xml() 2 dom=" + dom);
			return dom.documentElement;		
		} 		
		handleError(ERROR_TYPES.XML_PARSE, null, "dynamicUI_utility.string2xml(3)", "No XML parser available");
	}
	return null;
}

/**
 * Convert an XML DOM object to an XML string.
 * 
 * @param xml XML DOM.
 * @return XML string.
 */
function xml2string(xml) {
	var string = "";
	if (xml) {
		if (window.ActiveXObject) {
			// IE
			string = xml.xml;
		}
		else {
			// Firefox etc
			var s = new XMLSerializer();
			string = s.serializeToString(xml);
		}
	}
	else {
		string = "null";
	}
	return string;
}


/**
 * Splits some text into words delimited by the specified delimiter.
 * 
 * Occurances of the delimiter d within the text are expected to be escaped as \d
 * 
 * @param string
 * @param delimiter
 * @return
 */
function splitWithEscapes(text, delimiter) {
	var result = new Array();
	var tempArray = text.split(delimiter);
	var i = 0;
	var s = "";
	while (i < tempArray.length) {
		var continues = tempArray[i].charAt(tempArray[i].length - 1) == "\\";
		if (continues) {
			s += tempArray[i].substring(0, tempArray[i].length - 1) + delimiter;
		} else {
			s += tempArray[i];
			result.push(s);
			s = "";
		}
		i++;
	}
	return result;
}

/**
 * Escapes xml special characters within some text. Note: some browsers do not recognise &apos;, so &#39; is used instead.
 * 
 * '  &#39;
 * "  &quot;
 * <  &lt;
 * >   &gt;
 * &   &amp;
 * 
 */
function xmlEscape(text) {
	return text.replace(/&/g, "&amp;").replace(/"/g, "&quot;").replace(/'/g, "&#39;").replace(/</g, "&lt;").replace(/>/g, "&gt;");
}

/**
 * Escape the text unless the Questionnaire specifically allows markup, which defaults to false
 */
function usuallyXmlEscape(text) {
	if (persistentState.questionnaire && persistentState.questionnaire.markupAllowed) {
		return text;
	}
	return xmlEscape(text);
}

/**
 * Handles standard errors.
 * 
 * @param error The error, one of ERROR_TYPES (see dynamicUI_messages.js).
 * @param params Array Optional substitution parameters.
 * @param location String Optional location at which the error occurred, e.g. dynamicUI_main.processCreateList
 * @param details String Optional detailed error message.
 * @return False if processing should stop, True if it should continue.
 */
function handleError(error, params, location, details) {
	var retVal = false;
	var message = null;

	if (error.PARAMS > 0) {
		if (!isNull(params) && (params.length == error.PARAMS)) {
			message = $.vsprintf(error.TEXT, params);
		}
		else {
			return handleError(ERROR_TYPES.INTERNAL,
					[ "handleError called with incorrect number of substitution parameters" ],
					"dynamicUI_utility.handleError",
					"error=" + error.NAME + " params=" + arrayToString(params)
						+ " location=" + location + " details=" + details);
		}
	}
	else {
		message = error.TEXT;
	}
	
	if (error.LOG) {
		debugFull("ERROR[" + error.NAME + "] " + message + "\n\tLocation: " + location + "\n\tDetails: " + details);
	}
	
	if (error.DISPLAY == DISPLAY_TYPES.ALERT) {
		alert(message);
	}
	else if (error.DISPLAY == DISPLAY_TYPES.STATUS) {
		// Displays the message on the browser status bar for 10 seconds.
		if (window.status) {
			window.status = message;
			setTimeout("window.status = ''", 10000);
		}
	}
	
	return error.CONTINUE;
}

/**
 * Sleep.
 * 
 * @param ms time in milliseconds
 */
function sleep(ms) {
	var done = false;
	setTimer("done = true", ms);
	while (!done) {
		// wait
	}	
}

