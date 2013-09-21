<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
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

	<!-- link rel="shortcut icon" href="gfx/eurobank.ico" type="image/x-icon" /-->
	
	<script type="text/javascript" src="jquery/jquery.js"></script>
	<link rel="stylesheet" href="bootstrap/css/bootstrap.css" type="text/css"/>
	<script type="text/javascript" src="bootstrap/js/bootstrap.js"></script>
	
	<link rel="stylesheet" href="css/dbs.css" type="text/css"/>
	<script type="text/javascript" src="js/dbs.js"></script>
	
</head>
<body id="<tiles:getAsString name="pageid"/>">

	<div class="container-fluid" id="dbs-container">
	
	    <div class="row-fluid" id="dbs-header">
			<div class="span3 offset1">
			    <h2><a href="" title="raporty">raporty</a></h2>
			</div>
			<div class="span7">&nbsp;</div>
	    </div>
	
		<div class="row-fluid" id="dbs-center">

			<tiles:insertAttribute name="wait"/>
			
			<div class="span5 offset4" id="dbs-main">
			
				<div class="eb-dashed"><h1 class="eb-h1"><tiles:insertAttribute name="title"/></h1></div>
	            
	            <div id="dbs-messages"><tiles:insertAttribute name="messages"/></div>
	
	            <div id="dbs-content"><tiles:insertAttribute name="content"/></div>
            
	        </div>
	    </div>

		<div class="row-fluid" id="dbs-footer">
			<div class="span2 offset1">
				<div id="dbs-footer-phone" title="0 000 000 000 - centrum obsługi klienta"></div>
			</div>
			<div class="span8">
				<div id="dbs-footer-email">DataBase Support S.A., ul. ..., 50-000 Wrocław, <a href=mailto:online@dbs.pl>online@dbs.pl</a></div>
			</div>
		</div>

	</div>
	
</body>
</html>

