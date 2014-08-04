<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<spring:bind path="${fieldname}">
<c:set var="disabled" scope="page"><tiles:getAsString name="disabled"/></c:set>

<div class="<tiles:getAsString name="class"/><c:choose><c:when test="${status.error}"> <tiles:getAsString name="errorclass"/></c:when></c:choose>">
	
	
	<c:if test="${disabled ne ''}">
    	<label><c:out value="${field.label}" escapeXml="false"/><br/>${fn:length(field.value)} z ${fn:length(field.options)}</label>
	
		<div class="<tiles:getAsString name="contentclass"/>">
           <input value="<c:out value="${field.valueAsLabels}"/>" type="text" class="<tiles:getAsString name="inputclass"/>" readonly="readonly"/>
		</div>
    </c:if>

    <c:if test="${disabled eq ''}">
        <label><c:out value="${field.label}" escapeXml="false"/></label>
	    <select name="${fieldname}.value" 
	           id="${field.name}" 
	           multiple 
	           class="<tiles:getAsString name="inputclass"/>" 
	           data-width="400px" 
	           data-size="20" 
	           data-selected-text-format="count>3" 
	           <%@ include file="/WEB-INF/jsp/tiles/default/field/field-tooltip.jsp" %> 
	           <tiles:getAsString name="disabled"/>>
	        <c:forEach var="option" items="${field.options}" varStatus="rstatus">
	        <option value="${option.value}" <c:if test="${option.checked}">selected="selected"</c:if>>
	        <c:choose>
	        <c:when test="${!empty option.label}">${option.label}</c:when>
	        <c:otherwise>${option.value}</c:otherwise>
	        </c:choose>
	        </option>
	        </c:forEach>
	    </select>
	    
	
	    <span>&nbsp;</span>
	    <input type="checkbox" class="multiselectall" data-target="${field.name}" <c:if test="${field.allSelected}">checked</c:if>/>
	    <input type="hidden" name="${fieldname}.counter" class="multiselectcounter" value="${fn:length(field.value)}" data-target="${field.name}"/>
    </c:if>
    
	<c:if test="${status.error}"><span class="alert-msg"><i class="icon-remove-sign"></i> <c:out value="${status.errorMessage}" escapeXml="false"/></span></c:if>
	
</div>

<%@ include file="/WEB-INF/jsp/tiles/default/field/field-validators.jsp" %>

</spring:bind>

       