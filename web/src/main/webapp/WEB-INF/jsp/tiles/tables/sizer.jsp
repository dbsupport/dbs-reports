<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<label>Pokaż po <select size="1" class="pager-ctrl" target="${requestScope['javax.servlet.forward.request_uri']}?filter.pager.pageSize=">
	<option value="10" <c:if test="${pager.pageSize eq 10}">selected="selected"</c:if>>10</option>
	<option value="25" <c:if test="${pager.pageSize eq 25}">selected="selected"</c:if>>25</option>
	<option value="50" <c:if test="${pager.pageSize eq 50}">selected="selected"</c:if>>50</option>
	<option value="100" <c:if test="${pager.pageSize eq 100}">selected="selected"</c:if>>100</option>
</select> wyników</label>
        