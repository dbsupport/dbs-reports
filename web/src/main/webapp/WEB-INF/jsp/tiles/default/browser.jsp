<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
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
    <link href="css/bootstrap/bootstrap-overrides.css" type="text/css" rel="stylesheet">
    
    <!-- libraries -->
    <link href="css/lib/font-awesome.css" type="text/css" rel="stylesheet" />    

    <!-- global styles -->
    <link rel="stylesheet" type="text/css" href="css/compiled/layout.css">
    <link rel="stylesheet" type="text/css" href="css/compiled/elements.css">
    <link rel="stylesheet" type="text/css" href="css/compiled/icons.css">
    <link rel="stylesheet" href="css/dbs/dbs-alerts.css" type="text/css" media="screen" />
    <link rel="stylesheet" href="css/dbs/dbs-common.css" type="text/css" media="screen" />

    <!-- this page specific styles -->
    <tiles:insertAttribute name="css"/>
    
    <!-- open sans font -->
    <!-- link href='http://fonts.googleapis.com/css?family=Open+Sans:300italic,400italic,700italic,800italic,400,300,600,700,800' rel='stylesheet' type='text/css'-->
	<link rel="stylesheet" href="css/dbs/fonts.google.css" type="text/css" media="screen" />

    <!--[if lt IE 9]>
      <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
</head>
<body id="<tiles:getAsString name="id"/>">

    
    <%@ include file="/WEB-INF/jsp/tiles/default/navbar.jsp" %>
    
    <%@ include file="/WEB-INF/jsp/tiles/default/sidebar.jsp" %>
    
    <%@ include file="/WEB-INF/jsp/tiles/default/loading.jsp" %>
    
    <!-- main container -->
    <div class="content" id="main-container">
	 	
        <!-- settings changer -->
        <div class="skins-nav">
            <a href="#" class="skin first_nav selected">
                <span class="icon"></span><span class="text">Jasny</span>
            </a>
            <a href="#" class="skin second_nav" data-file="css/compiled/skins/dark.css">
                <span class="icon"></span><span class="text">Ciemny</span>
            </a>
        </div>
        
        <%@ include file="/WEB-INF/jsp/tiles/default/alerts.jsp" %>

        <div id="pad-wrapper" class="users-list">
            <div class="row header">
                <h3><tiles:getAsString name="title"/></h3>
			</div>
            <div class="row header">
				<tiles:insertAttribute name="form"/>
            </div>  

            <div class="row">
                <div class="col-md-12">
                
                <tiles:insertAttribute name="content"/>

                </div>                
            </div>
  

        </div>
        
    </div>
    
	<!-- scripts -->
    <script src="js/jquery.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script src="js/theme.js"></script>
    <script src="js/dbs/dbs-common.js"></script>
	<tiles:insertAttribute name="js"/>
</body>
</html>