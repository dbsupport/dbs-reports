<%@ include file="/WEB-INF/jsp/tiles/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<tiles:insertDefinition name="main" flush="true">
<tiles:putAttribute name="pageid" type="string">eb-page-transfer</tiles:putAttribute>
<tiles:putAttribute name="title" type="string">zlecenie przelewu</tiles:putAttribute>
<tiles:putAttribute name="content" type="string">

	<form:form method="post" commandName="transferForm" action="transfer/entry/transfer.ebk" class="form-horizontal" onsubmit="return EB.OnSubmit(this);">
	<fieldset>
		<legend>wprowadzanie danych</legend>
		
		<div class="control-group eb-control-group-next">
			<label for="sender-name" class="control-label">nadawca</label>
			<div class="controls">
			<textarea id="sender-name" class="input-xlarge uneditable-textarea" disabled="disabled"><sec:authentication property="principal.firstname"/> <sec:authentication property="principal.surename"/></textarea>
			</div>
		</div>
			
		<div class="control-group eb-control-group eb-required">
			<form:label path="sender" cssClass="control-label">z konta</form:label>
			<div class="controls">
			<div class="input-append">
			<form:select path="sender" cssErrorClass="input-xlarge eb-error" cssClass="input-xlarge" onchange="EB.OnAccount('sender');">
				<form:option value="" label="--- wybierz ---" title="{'owner':'disabled', 'assets':'0.00'}"/>
				<c:forEach items="${senders}" var="sender">
				<form:option value="${sender.nrb}" label="${sender.nrbFormatted}" title="{'owner':'disabled', 'assets':'${sender.assets}'}"/>
				</c:forEach>
			</form:select>
			<span class="add-on" id="sender-assets">0.00 PLN</span>
			</div>
			<form:errors path="sender" cssClass="help-inline" />
			</div>
		</div>
		
		<div class="control-group eb-control-group-next">
			<label class="control-label">odbiorca</label>
			<div class="controls">
			<textarea id="receiver-name" class="input-xlarge uneditable-textarea" disabled="disabled"></textarea>
			</div>
		</div>
		
		<div class="control-group eb-control-group eb-required">
			<form:label path="receiver" cssClass="control-label">na konto</form:label>
			<div class="controls">
			<form:select path="receiver" cssErrorClass="input-xlarge eb-error" cssClass="input-xlarge" onchange="EB.OnAccount('receiver');">
				<form:option value="" label="--- wybierz ---"/>
				<c:forEach items="${receivers}" var="receiver">
					<form:option value="${receiver.nrb}" label="${receiver.nrbFormatted}" title="{'owner':'${receiver.description}', 'assets':''}"/>
				</c:forEach>
			</form:select>
			<form:errors path="receiver" cssClass="help-inline" />
			</div>
		</div>	
		
		<div class="control-group eb-control-group-next eb-required">
			<form:label path="title" cssClass="control-label">tytułem</form:label>
			<div class="controls">
			<form:textarea path="title" cssErrorClass="eb-error" cssClass="input-xlarge"/>
			<form:errors path="title" cssClass="help-inline" />
			</div>
		</div>
		
		<div class="control-group eb-control-group eb-required">
			<form:label path="fullAmount" cssClass="control-label">kwota</form:label>
			<div class="controls">
			<div class="input-append">
			<form:input path="fullAmount" maxlength="13" cssErrorClass="input-small eb-error" cssClass="input-small" />
			<span class="add-on">PLN</span>
			</div>
			<form:errors path="fullAmount" cssClass="help-inline" />
			</div>
		</div>
		
		<div class="control-group eb-control-group-next">
			<label class="control-label">data wprowadzenia</label>
			<div class="controls">
			<span class="input-small uneditable-input">
			<fmt:formatDate value="${transferForm.date}" type="date" pattern="dd-MMM-yyyy" /></span>
			</div>
		</div>

	</fieldset>

		<div class="form-actions eb-form-actions">
			<div class="controls">
        		<button type="submit" name="transfer" class="btn btn-danger btn-primary">&nbsp;&nbsp;&nbsp;<i class="icon-star icon-white"></i>&nbsp;&nbsp;&nbsp;potwierdź&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</button>
          	</div>
		</div>	

	</form:form>	

	<script>
	EB.OnAccount('sender');
	EB.OnAccount('receiver');
	</script>

</tiles:putAttribute>

</tiles:insertDefinition>