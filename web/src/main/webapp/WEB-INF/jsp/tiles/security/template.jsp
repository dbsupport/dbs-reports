<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<!DOCTYPE html>
<html class="login-bg">
<head>
	<title><spring:message code="project.name"/></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<meta http-equiv="Content-Language" content="pl" />
	<meta name="author" content="<spring:message code="project.organization.name"/> <spring:message code="project.organization.url"/>" />
	<meta name="copyright" content="&copy; <spring:message code="project.organization.name"/>" />
	<meta name="description" content="<spring:message code="project.desc"/>" />
	<meta name="version" content="<spring:message code="project.version"/>"/>
	<meta name="robots" content="none" />    
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<base href="<%=request.getScheme()%>://<%=request.getServerName()%>:<%=request.getServerPort()%><%=request.getContextPath()%>/"/>
    <!-- bootstrap -->
    <link href="css/bootstrap/bootstrap.css" rel="stylesheet">
    <!-- link href="css/bootstrap/bootstrap-responsive.css" rel="stylesheet"-->
    <link href="css/bootstrap/bootstrap-overrides.css" type="text/css" rel="stylesheet">

    <!-- global styles -->
    <link rel="stylesheet" type="text/css" href="css/compiled/layout.css">
    <link rel="stylesheet" type="text/css" href="css/compiled/elements.css">
    <link rel="stylesheet" type="text/css" href="css/compiled/icons.css">
    <link rel="stylesheet" href="css/dbs/dbs-alerts.css" type="text/css" media="screen" />

    <!-- libraries -->
    <link rel="stylesheet" type="text/css" href="css/lib/font-awesome.css">
    
    <!-- this page specific styles -->
    <link rel="stylesheet" href="css/compiled/signup.css" type="text/css" media="screen" />

    <!-- open sans font -->
    <link href='http://fonts.googleapis.com/css?family=Open+Sans:300italic,400italic,700italic,800italic,400,300,600,700,800' rel='stylesheet' type='text/css'>

    <!--[if lt IE 9]>
      <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
</head>
<body id="<tiles:getAsString name="id"/>">
    <div class="header">
        <a href="">
            <img src="img/logo.png" class="logo" />
        </a>
    </div>

    <!-- main container -->
    
    <%@ include file="/WEB-INF/jsp/tiles/security/alerts.jsp" %>
    
    <tiles:insertAttribute name="content"/>
    
	<!-- scripts -->
    <script src="js/jquery.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <!-- script src="js/theme.js"></script-->    

</body>
</html>