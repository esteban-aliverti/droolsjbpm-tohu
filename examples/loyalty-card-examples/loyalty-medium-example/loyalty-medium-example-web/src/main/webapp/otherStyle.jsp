<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<% pageContext.include("/jsps/include.jspf"); %>

<link rel="stylesheet" href="css/screen.css" type="text/css" media="screen, projection" />
<link rel="stylesheet" href="css/print.css" type="text/css" media="print" />
<!--[if IE]>
    <link rel="stylesheet" href="css/ie.css" type="text/css" media="screen, projection" />
<![endif]-->
<link rel="stylesheet" type="text/css" href="script/theme/ui.all.css"/>
<link rel="stylesheet" href="css/style.css" type="text/css" media="screen, projection" />

<script type="text/javascript" src="script/extras.js"></script>
<script type="text/javascript">
  $(document).ready(function() {
    onQuestionnaireLoad("bodyContent", "loyalty");
  });
</script>
<script type="text/javascript" src="script/jquery-ui-personalized-1.6rc6.min.js"> </script>
<title>Loyalty Card</title>
</head>
<body>
<div id="container" class="container">
  <div class="span-16 suffix-1">
  	<div id="bodyContent" class="clearfix ui-widget ui-widget-content ui-corner-all"></div>
  </div>
  <div id="sidebar" class="span-7 last">
  
  	<div id="logo"><a href="index.html" title="Home" id="header"><img src="images/Solnet-(CMYK)-REV.png" width="238" height="289" alt="Home"></a></div>
  
  
    <div id="progressindicator" class="clearfix ui-widget ui-widget-content ui-corner-all"> 
      <h3 class="ui-widget-header ui-corner-all">Your Progress:</h3>
      <div id="progressbar">
        <p>&nbsp;</p>
      </div>
      
      
    </div>
  </div>
  </div>
  <a href="nostyles.jsp">Without CSS</a> <br>
  <a href="index.jsp">Simple CSS</a> 
</body>
</html>
