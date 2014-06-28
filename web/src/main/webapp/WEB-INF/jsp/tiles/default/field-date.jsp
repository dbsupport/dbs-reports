<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>


<spring:bind path="${fieldname}">
<div class="<tiles:getAsString name="class"/><c:choose><c:when test="${status.error}"> <tiles:getAsString name="errorclass"/></c:when></c:choose>">
	<label><c:out value="${field.label}" escapeXml="false"/></label>
	
	
	
	<div class="input-append date">
	<c:choose>
	<c:when test="${field.withTime}">
	<input name="${fieldname}.valueAsString" value="${field.valueAsString}" type="text" 
	class="<tiles:getAsString name="inputclass"/> input-datetimepicker" 
	data-date-format="${field.format}" 
	placeholder="<c:out value="${field.tooltip}" escapeXml="false"/>" <tiles:getAsString name="attributes"/>/>
	</c:when>
	<c:otherwise>
    <input name="${fieldname}.valueAsString" value="${field.valueAsString}" type="text" 
    class="<tiles:getAsString name="inputclass"/> input-datepicker" 
    data-date-format="${field.format}" 
    placeholder="<c:out value="${field.tooltip}" escapeXml="false"/>" <tiles:getAsString name="attributes"/>/>	
	</c:otherwise>
	</c:choose>
	
	</div>
	
	<c:if test="${status.error}"><span class="alert-msg"><i class="icon-remove-sign"></i> <c:out value="${status.errorMessage}" escapeXml="false"/></span></c:if>
</div>

<%@ include file="/WEB-INF/jsp/tiles/default/field-validators.jsp" %>

</spring:bind>

       