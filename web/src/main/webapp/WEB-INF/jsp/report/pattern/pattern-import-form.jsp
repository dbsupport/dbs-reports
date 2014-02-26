<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<tiles:insertDefinition name="tiles-wizard" flush="true">
<tiles:putAttribute name="id" type="string">dbs-page-report-pattern-import</tiles:putAttribute>
<tiles:putAttribute name="title" type="string">Import szablonu raportu</tiles:putAttribute>
<tiles:putAttribute name="steps" type="string">Wczytaj plik;Zapisz;Sprawdź formatkę</tiles:putAttribute>
<tiles:putAttribute name="step" type="string">3</tiles:putAttribute>
<tiles:putAttribute name="css" type="string">
<link rel="stylesheet" href="css/compiled/new-user.css" type="text/css" media="screen" />
<link rel="stylesheet" href="css/compiled/form-wizard.css" type="text/css" media="screen" />
<link rel="stylesheet" href="css/lib/bootstrap.datepicker.css" type="text/css" >
<link rel="stylesheet" href="css/dbs/dbs-wizard.css" type="text/css" media="screen" />
</tiles:putAttribute>
<tiles:putAttribute name="js" type="string">
<script src="js/bootstrap.datepicker.js"></script>
<script src="js/locales/bootstrap-datepicker.pl.js"></script>
<script src="js/dbs/dbs-datepicker.js"></script>
</tiles:putAttribute>
<tiles:putAttribute name="content" type="string">

                    	<form:form method="post" modelAttribute="reportGenerationForm" action="report/pattern/import/form" class="dbs-form">
	                    	<input type="hidden" name="page" value="2">
                    	
                            <c:if test="${reportGenerationForm.fieldfull}">
                            <c:forEach var="field" items="${reportGenerationForm.fields}" varStatus="fstatus">
								<tiles:insertDefinition name="${field.tile}">
								<tiles:putAttribute name="name" type="string">fields[${fstatus.index}]</tiles:putAttribute>
                            	<tiles:putAttribute name="label" type="string">${field.label}</tiles:putAttribute>
                            	<tiles:putAttribute name="value" type="string">${field.value}</tiles:putAttribute>
                            	<tiles:putAttribute name="format" type="string">${field.format}</tiles:putAttribute>
                            	<tiles:putAttribute name="tooltip" type="string">${field.tooltip}</tiles:putAttribute>
                            	<c:set var="validators" value="${field.validators}" scope="request"/>
                            	</tiles:insertDefinition>
                            </c:forEach>
                            </c:if>                    		
                    		
                            <div class="wizard-actions">
		                        <button type="button" class="btn-glow primary btn-prev" onclick="location.href='report/pattern/import/summary'"><i class="icon-chevron-left"></i>&nbsp;Powrót</button><span>&nbsp;</span>
		                        <button type="submit" class="btn-glow success btn-finish" style="display: inline-block;">Sprawdź</button><span>&nbsp;</span>
                            </div>
                        </form:form>
        
</tiles:putAttribute>
</tiles:insertDefinition>        