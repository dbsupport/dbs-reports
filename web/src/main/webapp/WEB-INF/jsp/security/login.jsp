<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<tiles:insertDefinition name="tiles-welcome" flush="true">
<tiles:putAttribute name="pageid" type="string">bds-page-login</tiles:putAttribute>
<tiles:putAttribute name="title" type="string">witamy w serwisie</tiles:putAttribute>
<tiles:putAttribute name="content" type="string">

	<form action="security_check.ebk" method="post" class="form-horizontal" onsubmit="return EB.OnSubmit(this);">
        <fieldset>
        	<legend>logowanie do systemu</legend>
            <div class="control-group eb-control-group eb-required">
            	<label for="usernameField" class="control-label eb-control-label-half">login</label>
            	<div class="controls">
				<input id="usernameField" type="text" name="security_username" class="input-medium eb-required"/>
				</div>
            </div>
            <div class="control-group eb-control-group-next  eb-required">
            	<label for="passwordField" class="control-label eb-control-label-half">hasło</label>
            	<div class="controls">
				<input id="passwordField" type="password" name="security_password" class="input-medium eb-required"/>
				</div>
            </div>
            
        </fieldset>
		<div class="form-actions eb-form-actions">
			<div class="controls">
          		<button type="submit" name="login" class="btn btn-danger btn-primary"><i class="icon-star icon-white"></i>&nbsp;&nbsp;zaloguj się</button>
          	</div>
		</div>
	</form>
	
</tiles:putAttribute>
</tiles:insertDefinition>