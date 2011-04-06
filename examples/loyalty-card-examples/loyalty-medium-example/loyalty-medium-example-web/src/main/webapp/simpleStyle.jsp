<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<% pageContext.include("/jsps/include.jspf"); %>
<link rel="stylesheet" type="text/css" media="screen" href="css/oldStyle.css" />
<script type="text/javascript" src="script/oldExtras.js"></script>
<script type="text/javascript">
  $(document).ready(function() {
    onQuestionnaireLoad("bodyContent", "loyalty");
  });
</script>
<title>Loyalty Card</title>
</head>
<body>

<center>

<table border=0 width="90%">
  <tr>
	<td valign="top">
       <div id="bodyContent"></div>
        </td>
        <td valign="top" width=240>
        <div id="progressindicator" class="clearfix">
    	<img src="images/Solnet.png" id="Solnet" width=240>
        	<h3>Your Progress:</h3>
          	<div id="progressbar">
            	<p>&nbsp;</p>
          	</div>
        </div>
    </td>
  </tr>
</table>

<a href="nostyles.jsp">Without CSS</a><br>
<a href="index.jsp">Other CSS</a>
 
</center>

</body>
</html>
