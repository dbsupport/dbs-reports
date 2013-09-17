<%@ include file="/WEB-INF/jsp/tiles/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<sec:authorize var="userIsAuthenticated" access="isFullyAuthenticated()" />

<c:set var="tile">
<c:choose><c:when test="${userIsAuthenticated==true}">main</c:when><c:otherwise>welcome</c:otherwise></c:choose>
</c:set>

<tiles:insertDefinition name="${tile}" flush="true">
<tiles:putAttribute name="pageid" type="string">eb-page-password-change</tiles:putAttribute>
<tiles:putAttribute name="title" type="string">zmiana hasła</tiles:putAttribute>
<tiles:putAttribute name="content" type="string">

	<form:form action="password_change.ebk" commandName="passwordChangeForm" method="post" class="form-horizontal" onsubmit="return EB.OnSubmit(this);">
        <fieldset>
        	<!-- legend>Zmiana hasła</legend-->
            <div class="control-group eb-control-group eb-required">
            	<label for="login" class="control-label eb-control-label-half">login</label>
            	<div class="controls">
				<form:input path="login" readonly="${passwordChangeForm.loginReadOnly}" type="text" class="input-medium eb-required" cssErrorClass="input-medium eb-error"/>
				<form:errors path="login" cssClass="help-inline" />
				</div>
            </div>
            <div class="control-group eb-control-group-next eb-required">
            	<label for="oldPassword" class="control-label eb-control-label-half">stare hasło</label>
            	<div class="controls">
				<form:input path="oldPassword" type="password" class="input-medium eb-required" cssErrorClass="input-medium eb-error"/>
				<form:errors path="oldPassword" cssClass="help-inline" />
				</div>
            </div>
            <div class="control-group eb-control-group eb-required">
            	<label for="newPassword" class="control-label eb-control-label-half">nowe hasło</label>
            	<div class="controls">
				<form:input path="newPassword" type="password" class="input-medium eb-required" cssErrorClass="input-medium eb-error"/>
				<form:errors path="newPassword" cssClass="help-inline" />
				</div>
            </div>
            <div class="control-group eb-control-group-next eb-required">
            	<label for="newPasswordConfirmation" class="control-label eb-control-label-half">powtórz nowe hasło</label>
            	<div class="controls">
				<form:input type="password" path="newPasswordConfirmation" class="input-medium eb-required" cssErrorClass="input-medium eb-error"/>
				<form:errors path="newPasswordConfirmation" cssClass="help-inline" />
				</div>
            </div>            
        </fieldset>
		<div class="form-actions eb-form-actions">
			<div class="controls">
          		<button type="submit" name="changePassword" class="btn btn-danger btn-primary"><i class="icon-star icon-white"></i>&nbsp;&nbsp;zmień hasło</button>
          	</div>
		</div>
	</form:form>
	
	</tiles:putAttribute>
</tiles:insertDefinition>