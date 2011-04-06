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
}


