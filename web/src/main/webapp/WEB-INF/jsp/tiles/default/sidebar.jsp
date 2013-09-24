<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<c:set var="id" scope="page"><tiles:getAsString name="id"/></c:set>

    <!-- sidebar -->
    <div id="sidebar-nav">
	       <ul id="dashboard-menu">
            <li class="<c:if test="${fn:startsWith(id, 'dbs-page-user')}">active</c:if>">
            	<c:if test="${fn:startsWith(id, 'dbs-page-user')}">
                <div class="pointer">
                    <div class="arrow"></div>
                    <div class="arrow_border"></div>
                </div>
                </c:if>
                <a class="dropdown-toggle" href="#">
                    <i class="icon-group"></i>
                    <span>Start</span>
                    <i class="icon-chevron-down"></i>
                </a>
                <ul class="submenu <c:if test="${fn:startsWith(id, 'dbs-page-user')}">active</c:if>">
                    <li><a class="<c:if test="${fn:startsWith(id, 'dbs-page-user-list')}">active</c:if>" href="user/list">Użytkownicy</a></li>
                    <li><a class="<c:if test="${fn:startsWith(id, 'dbs-page-user-new')}">active</c:if>" href="user/new">Nowy użytkownik</a></li>
                    <li><a class="<c:if test="${fn:startsWith(id, 'dbs-page-user-profile')}">active</c:if>" href="user/profile">Profil</a></li>
                </ul>
            </li>
            <li>
            	<c:if test="${fn:startsWith(id, 'dbs-page-report')}">
                <div class="pointer">
                    <div class="arrow"></div>
                    <div class="arrow_border"></div>
                </div>
                </c:if>            
                <a class="dropdown-toggle" href="#">
                    <i class="icon-edit"></i>
                    <span>Raporty</span>
                    <i class="icon-chevron-down"></i>
                </a>
                <ul class="submenu <c:if test="${fn:startsWith(id, 'dbs-page-report')}">active</c:if>">
                    <li><a class="<c:if test="${fn:startsWith(id, 'dbs-page-report-list')}">active</c:if>" href="report/list">Dostępne raporty</a></li>
                    <li><a class="<c:if test="${fn:startsWith(id, 'dbs-page-report-import')}">active</c:if>" href="report/import">Import definicji</a></li>
                </ul>
            </li>
        </ul>
    </div>
    <!-- end sidebar -->