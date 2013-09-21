<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="pl.com.dbs.reports.support.web.WebMessages" %>

<c:if test="${!empty eberrors}">
	  <c:forEach items="${eberrors}" var="msg">
		<div class="alert alert-error">
	  		<button type="button" class="close" data-dismiss="alert">&times;</button>
	  		<c:out value="${msg}" escapeXml="false"/>
		</div>
	  </c:forEach>
</c:if>

<c:if test="${!empty ebwarnings}">
	  <c:forEach items="${ebwarnings}" var="msg">
		<div class="alert alert-warning">
	  		<button type="button" class="close" data-dismiss="alert">&times;</button>
	  		<c:out value="${msg}" escapeXml="false"/>
		</div>
	  </c:forEach>
</c:if>

<c:if test="${!empty ebsuccess}">
	  <c:forEach items="${ebsuccess}" var="msg">
		<div class="alert alert-success">
	  		<button type="button" class="close" data-dismiss="alert">&times;</button>
	  		<c:out value="${msg}" escapeXml="false"/>
		</div>
	  </c:forEach>
</c:if>


<c:if test="${!empty ebinfos}">
	  <c:forEach items="${ebinfos}" var="msg">
		<div class="alert alert-info">
	  		<button type="button" class="close" data-dismiss="alert">&times;</button>
	  		<c:out value="${msg}" escapeXml="false"/>
		</div>
	  </c:forEach>
</c:if>




