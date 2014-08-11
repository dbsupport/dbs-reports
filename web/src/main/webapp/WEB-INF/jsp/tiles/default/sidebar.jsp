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
                    <li><a class="<c:if test="${fn:startsWith(id, 'dbs-page-report-unarchived')}">active</c:if>" href="report/unarchived">Twoje raporty</a></li>
                    <li><a class="<c:if test="${fn:startsWith(id, 'dbs-page-report-pattern')}">active</c:if>" href="report/pattern/list/init">Definicje</a></li>
                    <li><a class="<c:if test="${fn:endsWith(id, 'dbs-page-report-archives')}">active</c:if>" href="report/archived/init">Archiwum</a></li>
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
                	<li>&nbsp;</li>
                	<sec:authorize access="hasAnyRole('Admin')">
                    <li><a class="<c:if test="${fn:startsWith(id, 'dbs-page-parameter-edit')}">active</c:if>" href="param/edit">Parametry<br/>aplikacji</a></li>
                    </sec:authorize>
                </ul>
            </li>            
            </sec:authorize>
            <sec:authorize access="hasAnyRole('Real')">
            <li>
                <c:if test="${fn:startsWith(id, 'dbs-page-activedirectory')}">
                <div class="pointer">
                    <div class="arrow"></div>
                    <div class="arrow_border"></div>
                </div>
                </c:if>            
                <a class="dropdown-toggle" href="#">
                    <i class="icon-user-md"></i>
                    <span>Real</span>
                    <i class="icon-chevron-down"></i>
                </a>
                <ul class="submenu <c:if test="${fn:startsWith(id, 'dbs-page-activedirectory')}">active</c:if>">
                    <li><a class="<c:if test="${fn:startsWith(id, 'dbs-page-activedirectory-list')}">active</c:if>" href="activedirectory/list/init">Active Directory</a></li>
                </ul>
            </li>            
            </sec:authorize>
            
            
        </ul>
    </div>
    <!-- end sidebar -->