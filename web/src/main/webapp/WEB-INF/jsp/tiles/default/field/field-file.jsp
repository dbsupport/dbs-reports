<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>


<spring:bind path="${fieldname}">
<div class="<tiles:getAsString name="class"/><c:choose><c:when test="${status.error}"> <tiles:getAsString name="errorclass"/></c:when></c:choose>">
	<label><c:out value="${field.label}" escapeXml="false"/></label>
	<div class="<tiles:getAsString name="contentclass"/>">

	<c:set var="disabled"><tiles:getAsString name="disabled"/></c:set>
	<c:if test="${disabled ne 'disabled'}">
	<input name="${fieldname}.file"
	       value="<c:out value="${field.value}"/>"
	       type="file"
	       class="<tiles:getAsString name="inputclass"/>"
	       <%@ include file="/WEB-INF/jsp/tiles/default/field/field-tooltip.jsp" %>  
	       <tiles:getAsString name="attributes"/>/>
	</c:if>

	<c:if test="${disabled eq 'disabled'}">
	<input name="${fieldname}.value"
		   value="<c:out value="${field.value}"/>"
		   type="text"
		   class="<tiles:getAsString name="inputclass"/>"
			<%@ include file="/WEB-INF/jsp/tiles/default/field/field-tooltip.jsp" %>
			<tiles:getAsString name="attributes"/>/>
	</c:if>

	<c:if test="${status.error}"><span class="alert-msg"><i class="icon-remove-sign"></i> <c:out value="${status.errorMessage}" escapeXml="false"/></span></c:if>
	</div>
</div>

<%@ include file="/WEB-INF/jsp/tiles/default/field/field-validators.jsp" %>

</spring:bind>

       