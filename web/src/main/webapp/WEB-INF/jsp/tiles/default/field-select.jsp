<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<spring:bind path="${fieldname}">
<c:set var="disabled" scope="page"><tiles:getAsString name="disabled"/></c:set>

<div class="<tiles:getAsString name="class"/><c:choose><c:when test="${status.error}"> <tiles:getAsString name="errorclass"/></c:when></c:choose>">
	<label><c:out value="${field.label}" escapeXml="false"/></label>

    <c:if test="${disabled ne ''}">
        <div class="<tiles:getAsString name="contentclass"/>">
           <input value="<c:out value="${field.valueAsLabel}"/>" type="text" class="<tiles:getAsString name="inputclass"/>" readonly="readonly"/>
        </div>
    </c:if>

    <c:if test="${disabled eq ''}">
    <select name="${fieldname}.value" class="<tiles:getAsString name="inputclass"/>" data-width="400px" data-size="20" placeholder="<c:out value="${field.tooltip}" escapeXml="false"/>">
        <c:forEach var="option" items="${field.options}">
        <option value="${option.value}" <c:if test="${field.value eq option.value}">selected="selected"</c:if>>
        <c:choose>
        <c:when test="${!empty option.label}">${option.label}</c:when>
        <c:otherwise>${option.value}</c:otherwise>
        </c:choose>
        </option>
        </c:forEach>
    </select>

	<c:if test="${status.error}"><span class="alert-msg"><i class="icon-remove-sign"></i> <c:out value="${status.errorMessage}" escapeXml="false"/></span></c:if>
    </c:if>
    
</div>

<%@ include file="/WEB-INF/jsp/tiles/default/field-validators.jsp" %>

</spring:bind>

       