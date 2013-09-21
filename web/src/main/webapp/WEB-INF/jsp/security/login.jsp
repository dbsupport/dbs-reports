<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<tiles:insertDefinition name="tiles-security" flush="true">
<tiles:putAttribute name="id" type="string">dbs-page-login</tiles:putAttribute>
<tiles:putAttribute name="title" type="string">witamy w serwisie</tiles:putAttribute>
<tiles:putAttribute name="content" type="string">

    <div class="login-wrapper">
        <div class="box">
            <div class="content-wrap">
				<form action="security_check.ebk" method="post" class="form-horizontal" onsubmit="return EB.OnSubmit(this);">
                <h6>witamy w serwisie</h6>
                <input name="username" class="form-control" type="text" placeholder="Login">
                <input name="password" class="form-control" type="password" placeholder="Hasło">
				<div class="controls">
	          		<button type="submit" name="login" class="btn-glow primary login">zaloguj się</button>
                </div>                
				</form>
            </div>
        </div>

    </div>
	
</tiles:putAttribute>
</tiles:insertDefinition>