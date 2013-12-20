<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<c:set var="name" scope="page"><tiles:getAsString name="name"/></c:set>
<c:set var="label" scope="page"><tiles:getAsString name="label"/></c:set>

<a href="${requestScope['javax.servlet.forward.request_uri']}?filter.sorter.reorder=${name}">${label}</a>
        