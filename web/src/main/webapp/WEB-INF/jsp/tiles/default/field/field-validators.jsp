<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<c:set var="showvalidators" scope="page"><tiles:getAsString name="validators"/></c:set>

<c:if test="${showvalidators eq true and !empty field.validators}">
<div class="<tiles:getAsString name="class"/> success">
	<div class="col-md-12">
	<c:forEach items="${field.validators}" var="validator">
	<span class="alert-msg"><i class="icon-ok-sign"></i>
	<spring:message code="${validator.description.code}" arguments="${validator.description.args}" argumentSeparator=","></spring:message>
	</span>
	</c:forEach>
	</div>
</div>
</c:if>


       