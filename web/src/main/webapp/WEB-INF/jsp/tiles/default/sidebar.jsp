<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<c:set var="id" scope="page"><tiles:getAsString name="id"/></c:set>

    <!-- sidebar -->
    <div id="sidebar-nav">
	       <ul id="dashboard-menu">
	       <sec:authorize access="hasAnyRole('User,Admin,Management')">
            <li class="<c:if test="${fn:startsWith(id, 'dbs-page-profile')}">active</c:if>">
            	<c:if test="${fn:startsWith(id, 'dbs-page-profile')}">
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
                <ul class="submenu <c:if test="${fn:startsWith(id, 'dbs-page-profile')}">active</c:if>">
                	<li><a class="<c:if test="${fn:startsWith(id, 'dbs-page-profile-profile-current')}">active</c:if>" href="profile">Twój profil</a></li>
                	<sec:authorize access="hasAnyRole('Admin,Management')">
                    <li><a class="<c:if test="${fn:startsWith(id, 'dbs-page-profile-list')}">active</c:if>" href="profile/list">Lista profili</a></li>
                    <li><a class="<c:if test="${fn:startsWith(id, 'dbs-page-profile-new')}">active</c:if>" href="profile/new">Nowy profil</a></li>
                    </sec:authorize>
                </ul>
            </li>
            </sec:authorize>
            <sec:authorize access="hasAnyRole('User,Admin,Management')">
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
                    <li><a class="<c:if test="${fn:startsWith(id, 'dbs-page-report-pattern-list')}">active</c:if>" href="report/pattern/list/init">Definicje</a></li>
                    <li><a class="<c:if test="${fn:startsWith(id, 'dbs-page-report-archives')}">active</c:if>" href="report/archives/init">Archiwum</a></li>
                    <li>&nbsp;</li>
                    <sec:authorize access="hasAnyRole('Admin,Management')">
                    <li><a class="<c:if test="${fn:startsWith(id, 'dbs-page-report-pattern-import')}">active</c:if>" href="report/pattern/import">Import definicji</a></li>
                    </sec:authorize>
                </ul>
            </li>
            </sec:authorize>
            <sec:authorize access="hasAnyRole('Admin,Management')">
            <li>
            	<c:if test="${fn:startsWith(id, 'dbs-page-access') or fn:startsWith(id, 'dbs-page-parameter')}">
                <div class="pointer">
                    <div class="arrow"></div>
                    <div class="arrow_border"></div>
                </div>
                </c:if>            
                <a class="dropdown-toggle" href="#">
                    <i class="icon-cog"></i>
                    <span>Administ.</span>
                    <i class="icon-chevron-down"></i>
                </a>
                <ul class="submenu <c:if test="${fn:startsWith(id, 'dbs-page-access') or fn:startsWith(id, 'dbs-page-parameter')}">active</c:if>">
                    <li><a class="<c:if test="${fn:startsWith(id, 'dbs-page-access-list')}">active</c:if>" href="access/list">Uprawnienia raportowe</a></li>
                    <li><a class="<c:if test="${fn:startsWith(id, 'dbs-page-access-new')}">active</c:if>" href="access/new">Nowe uprawnienie raportowe</a></li>
                	<li>&nbsp;</li>
                    <li><a class="<c:if test="${fn:startsWith(id, 'dbs-page-parameter-edit')}">active</c:if>" href="param/edit">Parametry bazy danych</a></li>
                </ul>
            </li>            
            </sec:authorize>
        </ul>
    </div>
    <!-- end sidebar -->