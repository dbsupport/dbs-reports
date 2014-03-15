<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<c:set var="name" scope="page"><tiles:getAsString name="name"/></c:set>
<c:set var="label" scope="page"><tiles:getAsString name="label"/></c:set>

<c:set var="sclass" scope="page"><tiles:getAsString name="class"/></c:set>
<c:forEach var="field" items="${sorter.fields}">
<c:if test="${field.name eq name}">
    <c:choose>
	<c:when test="${field.asc}"><c:set var="sclass" scope="page"><tiles:getAsString name="classasc"/></c:set></c:when>
	<c:otherwise><c:set var="sclass" scope="page"><tiles:getAsString name="classdesc"/></c:set></c:otherwise>
	</c:choose>
</c:if>
</c:forEach>

<th tabindex="0" class="${sclass}">
    <a class="sorter-ctrl" href="${requestScope['javax.servlet.forward.request_uri']}?filter.sorter.reorder=${name}">${label}</a>
</th>