<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<c:if test="${not empty errors or not empty warnings or not empty successes or not empty infos}">
 <!-- alerts -->
<div id="security-alerts-wrapper">
<div class="section">
<div class="row">
			<c:forEach items="${errors}" var="msg">
			   <div class="alert alert-danger">
					<i class="icon-remove-sign"></i>
					<c:out value="${msg}" escapeXml="false"/>
			   </div>
			</c:forEach>
	
			<c:forEach items="${warnings}" var="msg">
				<div class="alert alert-warning">
				    <i class="icon-warning-sign"></i>
				    <c:out value="${msg}" escapeXml="false"/>
				</div>
			</c:forEach>
	
			<c:forEach items="${successes}" var="msg">
				<div class="alert alert-success">
				    <i class="icon-ok-sign"></i>
				    <c:out value="${msg}" escapeXml="false"/>
				</div>
			</c:forEach>
	
			<c:forEach items="${infos}" var="msg">
				<div class="alert alert-info">
				    <i class="icon-exclamation-sign"></i>
				    <c:out value="${msg}" escapeXml="false"/>
				</div>
			</c:forEach>
</div>
</div>
</div>	
<!-- end alerts -->
</c:if>
