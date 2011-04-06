function postProcessCreate(obj) {
	if ((obj.objType == QUESTION_OBJECT) && hasStyle(obj, "yesNoButtons")) {
		var selector = "#" + obj.id + " input";
		var yesNoId = obj.id + "_yn";
	    if ($("#" + yesNoId).length == 0) {
	    	var value = $(selector).attr("checked") ? "Yes" : "No";
	    	var html = "<span><span id=\"" + yesNoId + "\" class=\"answer\">" + value + "</span>"; 
	    	html += "<span class=\"yesNoControls\">";
	    	html += "<a href=\"#\" onclick=\"$('" + selector + "').attr('checked', true).click()\">Yes</a>";
	    	html += "<a href=\"#\" onclick=\"$('" + selector + "').attr('checked', false).click()\">No</a>";
	    	html += "</span></span>";
			$(selector).after(html);
			$(selector).click(function() {
			    var value = $(this).attr("checked") ? "Yes" : "No";
			    var yesNoId = $(this).parent().attr("id") + "_yn";
			    $("#" + yesNoId).text(value);
			});
		}
	}
}
