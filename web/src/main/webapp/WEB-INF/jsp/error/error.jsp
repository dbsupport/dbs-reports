<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<c:set var="env"><spring:message code="project.environment"/></c:set>

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

    <!-- global styles -->
    <link rel="stylesheet" type="text/css" href="css/compiled/layout.css">
    <link rel="stylesheet" type="text/css" href="css/compiled/elements.css">
    <link rel="stylesheet" type="text/css" href="css/compiled/icons.css">

    <!-- libraries -->
    <link rel="stylesheet" type="text/css" href="css/lib/font-awesome.css">
    
    <!-- this page specific styles -->
    <link rel="stylesheet" href="css/compiled/personal-info.css" type="text/css" media="screen" />

    <!-- open sans font -->
    <!-- link href='http://fonts.googleapis.com/css?family=Open+Sans:300italic,400italic,700italic,800italic,400,300,600,700,800' rel='stylesheet' type='text/css'-->

    <!--[if lt IE 9]>
      <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
</head>
<body id="dbs-page-error">
    <!-- navbar -->
    <header class="navbar navbar-inverse" role="banner">
        <div class="navbar-header">
            <button class="navbar-toggle" type="button" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href=""><img src="img/logo.png"></a>
        </div>
    </header>
    <!-- end navbar -->
    
	<!-- main container .wide-content is used for this layout without sidebar :)  -->
    <div class="content wide-content">
        <div class="settings-wrapper" id="pad-wrapper">
            <div class="row">
                <!-- avatar column -->
                <div class="col-md-3 col-md-offset-1 avatar-box">
                    <div class="personal-image">
                        <img src="img/dbs/error.png" class="avatar img-circle">
                    </div>
                </div>

                <!-- edit form column -->
                <div class="col-md-3 personal-info">
                    <div class="alert alert-danger">
                        <i class="icon-remove-sign"></i>Ups, problem... <c:out value="${exceptionId}"/><br/>
 						<c:if test="${env ne 'dev' and env ne 'tes'}">Zgłoś błąd administratorowi podając powyższy kod błędu!</c:if>                
                    </div>
                    
					<c:if test="${env eq 'dev' or env eq 'tes'}">
                    <h5 class="personal-title">Szczegóły:</h5>
					<c:out value="${exception}"></c:out>
					</c:if>

                </div>
            </div>            
        </div>
    </div>
    <!-- end main container -->    

	<!-- scripts -->
    <script src="js/jquery.js"></script>
    <script src="js/bootstrap.min.js"></script>
</body>
</html>