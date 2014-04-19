<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<spring:bind path="${fieldname}">
<div class="<tiles:getAsString name="class"/><c:choose><c:when test="${status.error}"> <tiles:getAsString name="errorclass"/></c:when></c:choose>">
	<label><c:out value="${field.label}" escapeXml="false"/></label>
	<div class="<tiles:getAsString name="contentclass"/>">
	<input name="${fieldname}.value" value="<c:out value="${field.value}"/>" type="text" class="<tiles:getAsString name="inputclass"/>" placeholder="<c:out value="${field.tooltip}" escapeXml="false"/>" <tiles:getAsString name="attributes"/>/>
	<c:if test="${status.error}"><span class="alert-msg"><i class="icon-remove-sign"></i> <c:out value="${status.errorMessage}" escapeXml="false"/></span></c:if>
	</div>
</div>

<%@ include file="/WEB-INF/jsp/tiles/default/field-validators.jsp" %>

</spring:bind>

       