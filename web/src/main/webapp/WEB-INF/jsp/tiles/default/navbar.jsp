<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>

    <!-- navbar -->
    <header class="navbar navbar-inverse" role="banner">
        <div class="navbar-header">
            <a class="navbar-brand" href="profile"><img src="img/dbs/logo.png"></a>
        </div>
        <ul class="nav navbar-nav pull-right hidden-xs">
            <li class="hidden-xs hidden-sm">
                <input class="search" type="text" placeholder="Wyszukaj definicję..." onchange="location.href='report/pattern/list?name='+this.value"/>
            </li>
            
            
 			<c:if test="${currentprofile.someNote}">            
            
            <li class="notification-dropdown hidden-xs hidden-sm">
                <a href="#" class="trigger">
                    <i class="icon-warning-sign"></i>
                </a>
                <div class="pop-dialog">
                    <div class="pointer right">
                        <div class="arrow"></div>
                        <div class="arrow_border"></div>
                    </div>
                    <div class="body">
                        <a href="#" class="close-icon"><i class="icon-remove-sign"></i></a>
                        <div class="messages">
                            <a href="profile" class="item">
			                	<c:choose>
			                    <c:when test="${!empty currentprofile.note.editor.photo}"><img src="profile/photo/${currentprofile.note.editor.photo.id}" class="img-circle smallavatar" /></c:when>
			                    <c:otherwise><img src="img/no-img-personal.png" class="img-circle smallavatar" /></c:otherwise>
			                    </c:choose>                            
                                <div class="name">
                                <c:choose>
			                    <c:when test="${currentprofile.note.editor.global}">${currentprofile.note.editor.description}</c:when>
			                    <c:otherwise>${currentprofile.note.editor.name}</c:otherwise>
			                    </c:choose>
			                    </div>
                                <div class="msg"><c:out value="${fn:substring(currentprofile.note.content, 0, 80)}" escapeXml="true"/></div>
                                <span class="time"><i class="icon-time"></i> <fmt:formatDate value="${currentprofile.note.editDate}" type="both" pattern="dd-MM-yyyy HH:mm:ss" /></span>
                            </a>
                            <div class="footer">
                                <a href="profile" class="logout">&nbsp;</a>
                            </div>
                        </div>
                    </div>
                </div>
            </li>
            
            </c:if>
            
            <li class="dropdown">
                <a href="#" class="dropdown-toggle hidden-xs hidden-sm" data-toggle="dropdown">
                    Twoje konto
                    <b class="caret"></b>
                </a>
                <ul class="dropdown-menu">
                    <li><a href="profile">Twój profil</a></li>
                    <li><a href="report/archives/current">Twoje archiwum</a></li>
                    <li><a href="report/archives/temporary">Twoje raporty</a></li>
                    <sec:authorize access="hasAnyRole('Admin')">
                    <li><a href="report/pattern/list/current">Twoje definicje</a></li>
                    </sec:authorize>
                </ul>
            </li>
            <li class="settings hidden-xs hidden-sm">
                <a href="security/logout" role="button">
                    <i class="icon-share-alt"></i>
                </a>
            </li>
        </ul>
    </header>
    <!-- end navbar -->