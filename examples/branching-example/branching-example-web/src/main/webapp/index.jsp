<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" media="screen" href="css/style.css" />
<title>Branching</title>
</head>
<body>
<div class="Controls">
	<a href="reset.jsp?knowledgebase=simple&<%= request.getQueryString() %>">Simple</a>
	<a href="reset.jsp?knowledgebase=buttons&<%= request.getQueryString() %>">Buttons</a>
	<a href="reset.jsp?knowledgebase=pages&<%= request.getQueryString() %>">Pages</a>
</div>
<div>
	<p>These 3 examples all ask the same questions.</p>
	<table border="1" cellspacing="0">
		<tr>
			<td><b>Simple</b></td>
			<td>
				Dependent questions shown on the same page.
			</td>
		</tr>
		<tr>
			<td><b>Buttons</b></td>
			<td>
				Checkboxes replaced with text showing current setting plus Yes/No buttons to change the value.
				This requires a small chunk of custom Javascript but no changes to the rules other than setting <i>presentationStyles</i> on the question.
			</td>
		</tr>
		<tr>
			<td><b>Pages</b></td>
			<td>
				Dependent questions now shown on a separate page which appears whenever Yes is clicked. i.e. a sub-flow
				This is achieved via a custom rule which fires when the Yes Answer is received.  The rule use questionnaire.navigationBranch to initiate a branch page flow.
			</td>
		</tr>
	</table>
</div>
</body>
</html>
