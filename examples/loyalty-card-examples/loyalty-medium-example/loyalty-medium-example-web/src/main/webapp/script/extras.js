/*
* Overriding because "requiredMissing" errors are hidden so we need to inform user that all fields are required. 
*/
function notifyErrors() {
	alert("Please answer all questions.");
}

function postProcessCreate(obj) {
	if (hasStyle(obj, "progress")) {
		$("#progressbar p").css("width", obj.label);
		$("#progressbar p").attr("title", obj.label);
	}
	
	if (obj.objType == QUESTIONNAIRE_OBJECT) {
		$("#LoyaltyQuestionnaire_label").wrapInner("<h1 class='ui-widget-header'></h1>");
	}
	
	if (obj.id == "ContactDetailsPage") {
		$("#ContactDetailsPage_label").wrapInner("<h4></h4>");
	}
	
	if (obj.id == "DemographicDetailsPage") {
		$("#DemographicDetailsPage_label").wrapInner("<h4></h4>");
	}
	
	if (obj.objType == ACTION_OBJECT && hasStyle(obj, "next")) {
			$(".Controls .next").prepend("<span class='ui-icon ui-icon-circle-arrow-e'/>").addClass("ui-state-default ui-corner-all");
	}
	
	if (obj.objType == ACTION_OBJECT && hasStyle(obj, "previous")) {
				$(".Controls .previous").prepend("<span class='ui-icon ui-icon-circle-arrow-w'/>").addClass("ui-state-default ui-corner-all");
	}
	
	if (obj.objType == ACTION_OBJECT && hasStyle(obj, "done")) {
			$(".Controls .done span").replaceWith("<span class='ui-icon ui-icon-circle-check'/>");
	}
}

