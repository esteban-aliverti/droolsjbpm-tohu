<%
	new drools.rex.ExecutionServerHelper(request.getSession()).removeKnowledgeSession();
	response.sendRedirect("questionnaire.jsp?" + request.getQueryString());
%>
