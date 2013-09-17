<%@ include file="/WEB-INF/jsp/tiles/taglib.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<%--c:set var="env"><spring:message code="projekt.otoczenie"/></c:set --%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="pl">
<head>
	<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
	<meta http-equiv="Content-Language" content="pl" />
	<meta name="author" content="<spring:message code="projekt.organizacja" text="EuroBank SA"/>" />
	<meta name="copyright" content="&copy; <spring:message code="projekt.organizacja" text="&copy; &copy; EuroBank SA"/>" />
	<meta name="description" content="<spring:message code="projekt.opis" text="EuroBank - Płatności Forum" />" />
	<meta name="robots" content="none" />

	<title><spring:message code="projekt.nazwa" text="eb-platnosciforum"/></title>
	<base href="<%=request.getScheme()%>://<%=request.getServerName()%>:<%=request.getServerPort()%><%=request.getContextPath()%>/"/>

	<link rel="shortcut icon" href="gfx/eurobank.ico" type="image/x-icon" />
	<script type="text/javascript" src="jquery/jquery.js"></script>
	<link rel="stylesheet" href="bootstrap/css/bootstrap.css" type="text/css"/>
	<script type="text/javascript" src="bootstrap/js/bootstrap.js"></script>
	
	<link rel="stylesheet" href="css/eurobank.css" type="text/css"/>
	<script type="text/javascript" src="js/eurobank.js"></script>
	
</head>

<body id="eb-page-error">

	<div class="container-fluid" id="eb-container">
	
	    <div class="row-fluid" id="eb-header">
	    	<%@ include file="../tiles/header.jsp" %>
	    </div>
	
		<div class="row-fluid" id="eb-center">
		
			<div class="span10 offset1" id="eb-main">
			
				<div class="eb-dashed"><h1>Ups, problem...</h1></div>
				
				<div id="eb-content">
					<div class="alert alert-error alert-block">
				  		<button type="button" class="close" data-dismiss="alert">&times;</button>
				  		<h4>Błąd! <c:out value="${exceptionId}"/></h4>
				  		<c:out value="${exception}"></c:out>
					</div>
				</div>				
	            
            
	        </div>
	    </div>
	</div>

</body>
</html>

