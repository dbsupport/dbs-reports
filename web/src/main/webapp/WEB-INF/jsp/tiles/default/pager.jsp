<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<c:set var="pages" scope="page"><tiles:getAsString name="pages"/></c:set>

<c:if test="${pager.maxPage gt 1}">
<c:choose>
<c:when test="${pages lt pager.maxPage}">
	<c:choose>
		<c:when test="${pager.page lt 1}"><c:set var="crb" scope="page" value="1"/></c:when>
		<c:when test="${pager.page+pages gt pager.maxPage}"><c:set var="crb" scope="page" value="${pager.maxPage-pages+1}"/></c:when>
		<c:otherwise><c:set var="crb" scope="page" value="${pager.page}"/></c:otherwise>
	</c:choose>	
	
	<c:choose>
		<c:when test="${pager.page+(pages-1) ge pager.maxPage}"><c:set var="cre" scope="page" value="${pager.maxPage}"/></c:when>
		<c:otherwise><c:set var="cre" scope="page" value="${pager.page+(pages-1)}"/></c:otherwise>
	</c:choose>
	
	<c:choose>
	<c:when test="${pager.page-pages gt 1}"><c:set var="prb" scope="page" value="${pager.page-pages}"/></c:when>
	<c:otherwise><c:set var="prb" scope="page" value="1"/></c:otherwise>
	</c:choose>
			
	<c:choose>
	<c:when test="${pager.page+pages gt pager.maxPage}"><c:set var="nrb" scope="page" value="${pager.maxPage-pages+1}"/></c:when>
	<c:otherwise><c:set var="nrb" scope="page" value="${pager.page+pages}"/></c:otherwise>
	</c:choose>			
</c:when>
<c:otherwise>
	<c:set var="crb" scope="page" value="1"/>
	<c:set var="cre" scope="page" value="${pager.maxPage}"/>
	<c:set var="prb" scope="page" value="1"/>
	<c:set var="nrb" scope="page" value="1"/>
</c:otherwise>
</c:choose>	


<div class="row ctrls">
<ul class="<tiles:getAsString name="class"/>">
	<c:choose><c:when test="${pager.page gt 1}"><li><a href="${requestScope['javax.servlet.forward.request_uri']}?filter.pager.page=${pager.page-1}">&#8249;</a></li></c:when>
	<c:otherwise><li><a class="">&#8249;</a></li></c:otherwise>
	</c:choose>
	
	<c:if test="${crb gt 1}">
		<li class=""><a href="${requestScope['javax.servlet.forward.request_uri']}?filter.pager.page=${prb}">..</a></li>
	</c:if>	
	
	<c:forEach begin="${crb}" end="${cre}" step="1" varStatus="ploop">
	<li class="<c:choose><c:when test="${pager.page eq ploop.index}">active</c:when></c:choose>"><a href="${requestScope['javax.servlet.forward.request_uri']}?filter.pager.page=${ploop.index}">${ploop.index}</a></li>
	</c:forEach>
	
	<c:if test="${cre lt pager.maxPage}">
		<li class=""><a href="${requestScope['javax.servlet.forward.request_uri']}?filter.pager.page=${nrb}">..</a></li>
	</c:if>		

	<c:choose><c:when test="${pager.page lt pager.maxPage}"><li><a href="${requestScope['javax.servlet.forward.request_uri']}?filter.pager.page=${pager.page+1}">&#8250;</a></li></c:when>
	<c:otherwise><li><a class="">&#8250;</a></li></c:otherwise>
	</c:choose>
</ul>
</div>
</c:if>
        