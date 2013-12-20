<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<c:set var="name" scope="page"><tiles:getAsString name="name"/></c:set>

<spring:bind path="${name}">
<div class="<tiles:getAsString name="class"/><c:choose><c:when test="${status.error}"> <tiles:getAsString name="errorclass"/></c:when></c:choose>">
	<label><tiles:getAsString name="label"/></label>
	<div class="<tiles:getAsString name="contentclass"/>">
	<input name="${name}.value" value="<tiles:getAsString name="value"/>" type="text" class="<tiles:getAsString name="inputclass"/>" placeholder="<tiles:getAsString name="tooltip"/>" <tiles:getAsString name="attributes"/>/>
	<c:if test="${status.error}"><span class="alert-msg"><i class="icon-remove-sign"></i> <c:out value="${status.errorMessage}" escapeXml="false"/></span></c:if>
	</div>
</div>
<c:if test="${!empty validators}">
<div class="<tiles:getAsString name="class"/> success">
	<div class="<tiles:getAsString name="contentclass"/>">
	<c:forEach items="${validators}" var="validator">
	<span class="alert-msg"><i class="icon-ok-sign"></i>
	<spring:message code="${validator.description.code}" arguments="${validator.description.args}" argumentSeparator=","></spring:message>
	</span>
	</c:forEach>
	</div>
</div>
</c:if>
</spring:bind>

       