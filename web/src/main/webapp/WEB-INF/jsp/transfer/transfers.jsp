<%@ include file="/WEB-INF/jsp/tiles/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>
<%@ page  import="pl.eurobank.platnosciforum.transfer.web.form.PeriodType" %>
<tiles:insertDefinition name="main" flush="true">
<tiles:putAttribute name="pageid" type="string">eb-page-transfers</tiles:putAttribute>
<tiles:putAttribute name="title" type="string">historia operacji</tiles:putAttribute>
<tiles:putAttribute name="content" type="string">

	<form:form method="post" commandName="transfersForm" action="transfer/transfers.ebk" class="form-horizontal" onsubmit="return EB.OnSubmit(this);">
	<input type="hidden" name="page" value=""/>
	<fieldset>
		<!-- legend>pokaż operacje</legend-->
		<div class="control-group eb-control-group eb-required">
			<form:label path="account" cssClass="control-label">pokaż historię konta</form:label>
			<div class="controls">
			<div class="input-append">	
				<form:select path="account" cssClass="input-xlarge eb-nrb" cssErrorClass="input-xlarge eb-error" onchange="EB.OnAccount('account');">
					<form:option value="" label="--- wybierz ---"/>
					<c:forEach items="${accounts}" var="account">
					<form:option value="${account.nrb}" label="${account.nrbFormatted}" title="{'owner':'${account.description}', 'assets':'${account.assets}'}"/>
					</c:forEach>		
				</form:select>
				<span class="add-on" id="account-assets">0.00 PLN</span>
			</div>
			<form:errors path="account" cssClass="help-inline"/>
			</div>
		</div>
		
		<%-- div class="control-group eb-control-group-next">
			<form:label path="type" cssClass="control-label">rodzaj operacji</form:label>
			<div class="controls">
				<form:select path="type" cssClass="input-xlarge" cssErrorClass="input-xlarge eb-error">
					<form:option value="" label="--- wszystkie ---"/>
					<form:options items="${types}"/>
				</form:select>
				<form:errors path="type" cssClass="help-inline"/>
			</div>		
		</div--%>
		
		<div class="control-group eb-control-group eb-required">
		<form:hidden path="periodFilter"/>
		<!-- label class="control-label">z okresu</label-->
		<div class="tabbable controls">	
			  <ul class="nav nav-tabs" id="tabs1">
			    <li<c:if test="${transfersForm.periodFilter eq 1}"> class="active"</c:if>><a href="#tab1" data-toggle="tab">z ostatnich</a></li>
			    <li<c:if test="${transfersForm.periodFilter eq 2}"> class="active"</c:if>><a href="#tab2" data-toggle="tab">wg dat</a></li>
			  </ul>
			  <div class="tab-content">
				    <div class="tab-pane<c:if test="${transfersForm.periodFilter eq 1}"> active</c:if>" id="tab1">
						<form:input path="period" maxlength="2" cssClass="input-mini"/>
						<form:errors path="period" cssClass="help-inline"/>
						<form:errors path="period" cssClass="help-inline"/>
						<form:select path="periodUnit" cssClass="input-medium" cssErrorClass="input-medium eb-error">
							<form:option value="" label="--- wybierz ---"/>
							<form:options items="${units}"/>
						</form:select>
						<form:errors path="periodUnit" cssClass="help-inline"/>
				    </div>
				    <div class="tab-pane<c:if test="${transfersForm.periodFilter eq 2}"> active</c:if>" id="tab2">
						<div class="input-prepend input-append date" id="datepicker1" data-date="" data-date-format="dd-mm-yyyy" data-date-language="pl">
							<span class="add-on">od</span>
							<form:input path="dateFrom" size="10" cssClass="input-small" cssErrorClass="input-small eb-error" readonly="false"/>
							<span class="add-on"><i class="icon-calendar"></i></span>
					  	</div>
						<div class="input-prepend input-append date" id="datepicker2" data-date="" data-date-format="dd-mm-yyyy" data-date-language="pl">
							<span class="add-on">do</span>
							<form:input path="dateTo" size="10" cssClass="input-small" cssErrorClass="input-small eb-error" readonly="false"/>
							<span class="add-on"><i class="icon-calendar"></i></span>
					  	</div>				  	
					  	<form:errors path="dateFrom" cssClass="help-inline"/>
				    	<form:errors path="dateTo" cssClass="help-inline"/>
				    </div>
			  </div>
				<script>
					$('a[data-toggle="tab"]').on('shown', function (e) { $('#periodFilter').val(e.target.hash=='#tab1'?'1':'2'); });
					$('#datepicker1').datepicker({autoclose:true});
					$('#datepicker2').datepicker({autoclose:true});
				</script>
		</div>		
		</div>
		
		<%--div class="control-group eb-control-group-next">
			<form:label path="amountFrom" cssClass="control-label">na kwotę od</form:label>
			<div class="controls">
			<div class="input-append">
				<form:input path="amountFrom" maxlength="13" cssErrorClass="input-small eb-error" cssClass="input-small" />
				<span class="add-on">PLN</span>
			</div>
			<span>do</span>
			<div class="input-append">
				<form:input path="amountTo" maxlength="13" cssErrorClass="input-small eb-error" cssClass="input-small" />
				<span class="add-on">PLN</span>
			</div>
			<form:errors path="amountFrom" cssClass="help-inline" />
			<form:errors path="amountTo" cssClass="help-inline" />
			</div>
		</div--%>
		
		<%-- div class="control-group eb-control-group">
			<form:label path="title" cssClass="control-label">zawierające w tytule</form:label>
			<div class="controls">
			<form:input path="title" cssErrorClass="eb-error" cssClass="input-xlarge"/>
			<form:errors path="title" cssClass="help-inline" />
			</div>		
		</div--%>
				
	</fieldset>
	
	<div class="form-actions eb-form-actions">
		<div class="controls">
       		<button type="submit" name="transfer" class="btn btn-danger btn-primary">&nbsp;&nbsp;&nbsp;<i class="icon-search icon-white"></i>&nbsp;&nbsp;&nbsp;szukaj&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</button>
         </div>
	</div>		
	
	</form:form>
	
	<c:if test="${!empty transfers}">
	<table class="table table-striped table-hover">
		<thead>
		<tr>
			<th class="c1"><a>data<br/>księgowania</a></th>
			<th class="c2"><a>data<br/>realizacji</a></th>
			<th class="c3"><a>tytuł operacji</a></th>
			<th class="c4"><a>kwota<br/>(PLN)</a></th>
			<th class="c5"><a>saldo<br/>(PLN)</a></th>
		</tr>
		</thead>
		
		<tbody  class="eb-tbody">
		<c:forEach var="transfer" items="${transfers}" varStatus="status">
			<tr>
				<td><fmt:formatDate value="${transfer.bookingDate}" type="date" pattern="dd-MMM-yyyy" /></td>
				<td><fmt:formatDate value="${transfer.date}" type="date" pattern="dd-MMM-yyyy" /></td>
				<td><c:out value="${transfer.title}"/>
				<br/><span class="label label-inverse"><c:out value="${transfer.receiverNrb}"/></span>
				<br/><span class="label label-info"><c:out value="${transfer.typeName}"/></span></td>
				<td class="c4"><c:out value="${transfer.amountSigned}"/></td>
				<td class="c5"><c:out value="${transfer.balance}"/></td>
			</tr>
		</c:forEach>
		</tbody>			
	</table>

	<ul class="pager">
		<c:if test="${transfersForm.pager.hasPrev ne true}">
		<li class="previous disabled"><a>&larr; Starsze</a></li>
		</c:if>
		<c:if test="${transfersForm.pager.hasPrev eq true}">
		<li class="previous"><a class="btn" onclick="OnPage('backward');">&larr; Starsze</a></li>
		</c:if>
		
		<c:if test="${transfersForm.pager.hasNext ne true}">
		<li class="next disabled"><a>Nowsze &rarr;</a></li>
		</c:if>
		<c:if test="${transfersForm.pager.hasNext eq true}">
		<li class="next"><a class="btn" onclick="OnPage('forward');">Nowsze &rarr;</a></li>
		</c:if>
  	</ul>

	<div class="btn-group eb-btn-group">
		  <button id="eb-btn-export" class="btn btn-danger dropdown-toggle" data-toggle="dropdown">&nbsp;&nbsp;&nbsp;<i class="icon-print icon-white"></i>&nbsp;&nbsp;&nbsp;wyeksportuj&nbsp;&nbsp;&nbsp;<span class="caret"></span></button>
		  <ul class="dropdown-menu">
		    <li><a id="eb-btn-export-pdf" href="transfer/export/PDF/export.ebk" onclick="OnExport(this.id);">PDF</a></li>
		    <li><a id="eb-btn-export-csv" href="transfer/export/CSV/export.ebk" onclick="OnExport(this.id);">CSV</a></li>
		  </ul>
	</div>	
	</c:if>
	
	<script>
	EB.OnAccount('account');
	function OnExport(id) {
		EB.DisableFor($('#eb-btn-export'), 5000);
		if(!$.support.opacity) $('#'+id).attr('target','_blank');
	}
	function OnPage(value) {
		EB.OnWait();
		document.forms.transfersForm.page.value = value;
		document.forms.transfersForm.submit();
	}
	</script>	

</tiles:putAttribute>

</tiles:insertDefinition>