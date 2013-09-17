<%@ include file="/WEB-INF/jsp/tiles/taglib.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" dir="ltr" xml:lang="pl" lang="pl">
<head>
	<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
	<meta http-equiv="Content-Language" content="pl" />
	<meta name="author" content="<spring:message code="projekt.organizacja"/>" />
	<meta name="copyright" content="&copy; <spring:message code="projekt.organizacja"/>" />
	<meta name="description" content="<spring:message code="projekt.opis"/>" />
	<meta name="robots" content="none" />

	<title><spring:message code="projekt.nazwa"/></title>
	<base href="<%=request.getScheme()%>://<%=request.getServerName()%>:<%=request.getServerPort()%><%=request.getContextPath()%>/"/>

	<link rel="shortcut icon" href="gfx/eurobank.ico" type="image/x-icon" />
	
	<script type="text/javascript" src="jquery/jquery.js"></script>
	<link rel="stylesheet" href="bootstrap/css/bootstrap.css" type="text/css"/>
	<script type="text/javascript" src="bootstrap/js/bootstrap.js"></script>
	
	<link rel="stylesheet" href="css/eurobank.css" type="text/css"/>
	<script type="text/javascript" src="js/eurobank.js"></script>
	
</head>
<body id="<tiles:getAsString name="pageid"/>">

	<div class="container-fluid" id="eb-container">
	
	    <div class="row-fluid" id="eb-header">
	    	<tiles:insertAttribute name="header"/>
	    </div>
	
		<div class="row-fluid" id="eb-center">

			<tiles:insertAttribute name="wait"/>
			
			<div class="span5 offset4" id="eb-main">
			
				<div class="eb-dashed"><h1 class="eb-h1"><tiles:insertAttribute name="title"/></h1></div>
	            
	            <div id="eb-messages"><tiles:insertAttribute name="messages"/></div>
	
	            <div id="eb-content"><tiles:insertAttribute name="content"/></div>
            
	        </div>
	    </div>

		<div class="row-fluid" id="eb-footer">
			<tiles:insertAttribute name="footer"/>
		</div>

	</div>
	
</body>
</html>

