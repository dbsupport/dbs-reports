<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<c:set var="show" scope="page"><tiles:getAsString name="tooltips"/></c:set>

<c:if test="${show}">placeholder="<c:out value="${field.tooltip}" escapeXml="false"/>" title="<c:out value="${field.tooltip}" escapeXml="false"/>"</c:if>
       