<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<tiles:insertDefinition name="tiles-security" flush="true">
<tiles:putAttribute name="id" type="string">dbs-page-login</tiles:putAttribute>
<tiles:putAttribute name="title" type="string">witamy w serwisie</tiles:putAttribute>
<tiles:putAttribute name="content" type="string">

    <div class="login-wrapper">
        <div class="box">
            <div class="content-wrap">
				<form action="security/check" method="post" class="form-horizontal dbs-form">
                <h6>witamy w serwisie</h6>
                <c:choose>
                <c:when test="${env eq 'dev' or env eq 'tes'}">
                <input name="username" class="form-control" type="text" placeholder="Login" value="adam"/>
                <input name="password" class="form-control" type="password" placeholder="Hasło" value="adam"/>
                </c:when>
                <c:otherwise>
                <input name="username" class="form-control" type="text" placeholder="Login" value=""/>
                <input name="password" class="form-control" type="password" placeholder="Hasło" value=""/>
                </c:otherwise>
                </c:choose>
				<div class="controls">
	          		<button type="submit" name="login" class="btn-glow primary login">Zaloguj się</button>
                </div>                
				</form>
            </div>
        </div>

    </div>
	
</tiles:putAttribute>
</tiles:insertDefinition>