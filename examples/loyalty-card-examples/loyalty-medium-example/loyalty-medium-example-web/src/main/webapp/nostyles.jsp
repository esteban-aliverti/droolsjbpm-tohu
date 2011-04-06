<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<% pageContext.include("/jsps/include.jspf"); %>
<script type="text/javascript">
  $(document).ready(function() {
	    onQuestionnaireLoad("bodyContent", "loyalty");
  });
</script>
<title>Loyalty Card</title>
</head>
<body>
<div id="container" class="container">
  <div class="span-16 colborder">
  	<div id="bodyContent" ></div>
  </div>
  <div id="sidebar" class="span-7 last">
    <div id="progressindicator" class="clearfix"> <a href="/" title="Home"><img src="images/Solnet.png" id="Solnet" width=200></a>
      <h3>Your Progress:</h3>
      <div id="progressbar">
        <p>&nbsp;</p>
      </div>
    </div>
  </div>


<a href="otherStyle.jsp">Other CSS</a><br>
<a href="index.jsp">Simple CSS</a> 
 
</div>

</body>
</html>
