<%@ include file="/WEB-INF/jsp/tiles/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<tiles:insertDefinition name="main" flush="true">
<tiles:putAttribute name="pageid" type="string">eb-page-transfer</tiles:putAttribute>
<tiles:putAttribute name="title" type="string">zlecenie przelewu</tiles:putAttribute>
<tiles:putAttribute name="content" type="string">

	<form:form method="post" commandName="transferForm" action="transfer/confirm/transfer.ebk" class="form-horizontal" onsubmit="return EB.OnSubmit(this);">
	<fieldset>
		<legend>potwierdzenie</legend>
		
		<div class="control-group eb-control-group-next">
			<label for="sender-name" class="control-label">nadawca</label>
			<div class="controls">
			<textarea id="sender-name" class="input-xlarge uneditable-textarea" disabled="disabled"><sec:authentication property="principal.firstname"/> <sec:authentication property="principal.surename"/></textarea>
			</div>
		</div>
			
		<div class="control-group">
			<form:label path="sender" cssClass="control-label">z konta</form:label>
			<div class="controls">
			<span class="input-xlarge uneditable-input"><c:out value="${sender.nrb}"/></span>
			</div>
		</div>
		
		<div class="control-group eb-control-group-next">
			<label class="control-label">odbiorca</label>
			<div class="controls">
			<textarea class="input-xlarge uneditable-textarea" disabled="disabled"><c:out value="${receiver.description}"/></textarea>
			</div>
		</div>
		
		<div class="control-group eb-control-group">
			<form:label path="receiver" cssClass="control-label">na konto</form:label>
			<div class="controls">
			<span class="input-xlarge uneditable-input"><c:out value="${receiver.nrb}"/></span>
			</div>
		</div>	
		
		<div class="control-group eb-control-group-next">
			<form:label path="title" cssClass="control-label">tytułem</form:label>
			<div class="controls">
			<textarea class="input-xlarge uneditable-textarea" disabled="disabled"><c:out value="${transferForm.title}"/></textarea>
			</div>
		</div>
		
		<div class="control-group eb-control-group">
			<form:label path="fullAmount" cssClass="control-label">kwota</form:label>
			<div class="controls">
			<span class="input-xlarge uneditable-input"><c:out value="${transferForm.fullAmount}"/> PLN</span>
			</div>
		</div>
		
		<div class="control-group eb-control-group-next">
			<label class="control-label">data wprowadzenia</label>
			<div class="controls">
			<span class="input-small uneditable-input">
			<fmt:formatDate value="${transferForm.date}" type="date" pattern="dd-MMM-yyyy" />
			</span>
			</div>
		</div>

	</fieldset>

		<div class="form-actions eb-form-actions">
			<div class="controls">
				<a href="transfer/entry/transfer.ebk" class="btn" title="popraw" onclick="EB.OnWait();">&nbsp;&nbsp;&nbsp;popraw&nbsp;&nbsp;&nbsp;</a>
        		<button type="submit" name="transfer" class="btn btn-danger btn-primary">&nbsp;&nbsp;&nbsp;<i class="icon-star icon-white"></i>&nbsp;&nbsp;&nbsp;zleć&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</button>
          	</div>
		</div>	

	</form:form>	


</tiles:putAttribute>

</tiles:insertDefinition>