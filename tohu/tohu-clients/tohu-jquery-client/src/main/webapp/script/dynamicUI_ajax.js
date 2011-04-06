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
 * Javascript for JQuery Dynamic UI ajax calls.
 */

// URL for AJAX calls to Drools Execution Server.
var droolsURL = null;

var droolsBusy = false;

/**
* Gets the URL to the rules servlet
 *
 * @return The URL String.
 */
function getDroolsURL() {
	return droolsURL;
}

/**
 * Sets the URL to the rules servlet
 * 
 * @param url URL string.
 */
function setDroolsURL(url) {
	droolsURL = url;
}

/**
 * Makes an AJAX request to the Drools Execution Server.
 * 
 * @param request XML string to send to Drools.
 * @return XML string returned from Drools.
 */
function callDrools(request) {
	while(droolsBusy) {
		sleep(10);
	}
	droolsBusy = true;
        var mimeType = useSoap?"text/xml":"text/plain";
	try {
		debugFull("callDrools() request=" + request);
		var retVal = null;
		// Save the session ID before we make the call, if the server sends back a different one it
		// means the session has timed out.
		var oldSessionID = $.cookie("JSESSIONID");
		//debug("callDrools() oldSessionID=" + oldSessionID);
		$.ajax( {
			type : "POST",
			url : droolsURL,
			contentType : mimeType,
			processData : false,
			async : false,
			data : request,
			dataType : "text",
			success : function(response, status) {
				//if (response.substr(0, 5) != "<?xml") { 
				//	response = "<?xml version='1.0' encoding='UTF-8'?>\n" + response;
				//}
				retVal = response;
				debugFull("callDrools() status=" + status + "\nresponse=" + response);
			},
			error : function(httpRequest, status, exception) {
				if (!handleError(ERROR_TYPES.AJAX_CALL, null, "dynamicUI_ajax.callDrools",
						"status=" + status + " exception=" + exception + " response=" + httpRequest.responseText)) {
					// Stop further processing.
					retVal = null;
				}
			}
		});
		var newSessionID = $.cookie("JSESSIONID");
		//debug("callDrools() newSessionID=" + newSessionID);
		if (!isNull(oldSessionID) && !isNull(newSessionID) && (oldSessionID != newSessionID)) {
			if (!handleError(ERROR_TYPES.SESSION_TIMEOUT)) {
				// Stop further processing.
				retVal = null;
			}
		}
		return retVal;
	} finally {
		droolsBusy = false;	
	}
}

/**
 * Makes an AJAX request to the Drools Execution Server to delete the session.
 */
function resetDrools() {
	debug("resetDrools()");
	$.ajax( {
		type : "DELETE",
		url : droolsURL,
		async : false,
		success : function(response, status) {
			debug("resetDrools() status=" + status + " response=" + response);
		},
		error : function(httpRequest, status, exception) {
			handleError(ERROR_TYPES.AJAX_CALL, null, "dynamicUI_ajax.resetDrools",
				    "status=" + status + " exception=" + exception + " response=" + httpRequest.responseText);
		}
	});
	clearCookie('JSESSIONID');
}
