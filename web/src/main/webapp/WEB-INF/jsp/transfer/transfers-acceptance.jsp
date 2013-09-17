<%@ include file="/WEB-INF/jsp/tiles/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<tiles:insertDefinition name="main" flush="true">
<tiles:putAttribute name="pageid" type="string">eb-page-transfers-accept</tiles:putAttribute>
<tiles:putAttribute name="title" type="string">przelewy oczekujące na akceptację</tiles:putAttribute>
<tiles:putAttribute name="content" type="string">

	<form:form method="post" commandName="transfersAcceptanceForm" action="transfers/acceptance.ebk" class="form-horizontal" onsubmit="return EB.OnSubmit(this);">
	<fieldset>
		<!-- legend>pokaż operacje</legend-->
		<div class="control-group eb-control-group eb-required">
			<form:label path="account" cssClass="control-label">konto</form:label>
			<div class="controls">
			<div class="input-append">	
				<form:select path="account" cssClass="input-xlarge" cssErrorClass="input-xlarge eb-error" onchange="EB.OnAccount('account');">
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
			<th class="c1"><a>data<br/>rejestracji</a></th>
			<th class="c2"><a>tytuł operacji</a></th>
			<th class="c3"><a>kwota<br/>(PLN)</a></th>
			<th class="c4">&nbsp;</th>
		</tr>
		</thead>
		
		<tbody  class="eb-tbody">
		<sec:authentication var="operator" property="principal" />
		<c:forEach var="transfer" items="${transfers}" varStatus="status">
			<tr>
				<td class="c1"><fmt:formatDate value="${transfer.date}" type="date" pattern="dd-MMM-yyyy" /></td>
				<td><c:out value="${transfer.title}"/>
				<br/><span class="label label-inverse"><c:out value="${transfer.receiverNrb}"/></span>
				<!-- ${transfer.documentId} -->
				<c:if test="${transfer.overLimit}">
				<br/><span class="label label-danger">Operacja na kwotę powyżej <c:out value="${transfer.limit}"/> PLN. Wymagana podwójna akceptacja.</span>
				</c:if>
				</td>
				<td class="c3"><c:out value="${transfer.amountSigned}"/></td>
				
				<td>
				<c:if test="${transfer.anyAcceptator}">
				<c:forEach var="acceptator" items="${transfer.acceptators}" varStatus="status">
				<div class="btn-group">
					  <button class="btn disabled btn-danger dropdown-toggle eb-btn-accept"><c:out value="${acceptator}"/></button>
				</div><br/>
				</c:forEach>
				</c:if>
				
				<c:choose>
				<c:when test="${operator.firstHandAcceptator and transfer.acceptableByFirstHand}">
				<div class="btn-group">
					  <button class="btn btn-danger dropdown-toggle eb-btn-accept" data-toggle="dropdown">&nbsp;&nbsp;&nbsp;<i class="icon-ok icon-white"></i>&nbsp;&nbsp;&nbsp;zaakceptuj&nbsp;&nbsp;&nbsp;<span class="caret"></span></button>
					  <ul class="dropdown-menu">
					    <li><a href="transfers/${transfer.acceptance.documentDefId}/acceptance.ebk" title="zaakceptuj" onclick="EB.OnWait();"><sec:authentication property="principal.firstname"/> <sec:authentication property="principal.surename"/></a></li>
					  </ul>
				</div>	
				</c:when>
				<c:when test="${operator.secondHandAcceptator and transfer.acceptableBySecondHand}">
				<div class="btn-group">
					  <button class="btn btn-danger dropdown-toggle eb-btn-accept" data-toggle="dropdown"><i class="icon-ok icon-white"></i>&nbsp;&nbsp;&nbsp;zaakceptuj&nbsp;&nbsp;&nbsp;<span class="caret"></span></button>
					  <ul class="dropdown-menu">
					    <li><a href="transfers/${transfer.acceptance.documentDefId}/acceptance.ebk" title="zaakceptuj" onclick="EB.OnWait();"><sec:authentication property="principal.firstname"/> <sec:authentication property="principal.surename"/></a></li>
					  </ul>
				</div>	
				</c:when>
				<c:otherwise>
				<div class="btn-group">
					  <button class="btn disabled btn-danger dropdown-toggle eb-btn-accept"><i class="icon-ok icon-white"></i>&nbsp;&nbsp;&nbsp;zaakceptuj&nbsp;&nbsp;&nbsp;<span class="caret"></span></button>
				</div>					
				</c:otherwise>
				</c:choose>
				
				
				</td>
			</tr>
		</c:forEach>
		</tbody>			
	</table>
	
	</c:if>
	
	<script>
	EB.OnAccount('account');
	</script>

</tiles:putAttribute>

</tiles:insertDefinition>