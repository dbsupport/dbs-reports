<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<c:set var="id" scope="page"><tiles:getAsString name="id"/></c:set>

    <!-- sidebar -->
    <div id="sidebar-nav">
	       <ul id="dashboard-menu">
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
                    <li><a class="<c:if test="${fn:startsWith(id, 'dbs-page-profile-list')}">active</c:if>" href="profile/list">Profile</a></li>
                    <li><a class="<c:if test="${fn:startsWith(id, 'dbs-page-profile-new')}">active</c:if>" href="profile/new">Nowy profil</a></li>
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
                    <li><a class="<c:if test="${fn:startsWith(id, 'dbs-page-report-pattern-list')}">active</c:if>" href="report/pattern/list">Definicje</a></li>
                    <li><a class="<c:if test="${fn:startsWith(id, 'dbs-page-report-archives')}">active</c:if>" href="report/archives">Archiwum</a></li>
                    <li>&nbsp;</li>
                    <li><a class="<c:if test="${fn:startsWith(id, 'dbs-page-report-pattern-import')}">active</c:if>" href="report/pattern/import">Import definicji</a></li>
                </ul>
            </li>
        </ul>
    </div>
    <!-- end sidebar -->