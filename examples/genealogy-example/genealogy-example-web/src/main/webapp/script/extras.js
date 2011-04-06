function postProcessCreate(obj) {
	if (hasStyle(obj, "icon")) {
		createIcon(obj);
	}
}

function createIcon(obj, image) {
	var selector = "#" + obj.id + " input";
	var iconId = obj.id + "_icon";
    if ($("#" + iconId).length == 0) {
		//var title = $("#" + obj.id + " .preLabel").text();
    	var html = "<span id=\"" + iconId + "\""; 
    	html += "<a href=\"#\" onclick=\"$('" + selector + "').attr('checked', true).click()\">&nbsp;</a>";
    	html += "</span>";
		$(selector).after(html);
	}
}
