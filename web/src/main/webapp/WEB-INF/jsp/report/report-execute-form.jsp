<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<tiles:insertDefinition name="tiles-wizard" flush="true">
<tiles:putAttribute name="id" type="string">dbs-page-report-execute-form</tiles:putAttribute>
<tiles:putAttribute name="title" type="string">Generowanie raportu</tiles:putAttribute>
<tiles:putAttribute name="steps" type="string">Ustaw parametry raportu;Wygeneruj raport;Obejrzyj raport</tiles:putAttribute>
<tiles:putAttribute name="step" type="string">1</tiles:putAttribute>
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

                    	<form:form method="post" modelAttribute="reportGenerationForm" action="report/execute/form" class="">
                    		<input type="hidden" name="page" value="1">
                            <div class="field-box">
                            	<label>Definicja:</label>
                            	<input class="form-control inline-input" type="text" readonly="readonly" value="${reportGenerationForm.pattern.name} ${reportGenerationForm.pattern.version}"/>
                            </div>
                            
                    	 	<div class="field-box">
                            	<label>Format:</label>
	                            <div class="ui-select">
		                            <form:select path="format">
		                            	<form:options items="${reportGenerationForm.formats}" itemLabel="format.defaultExt" itemValue="ext"/>
		                            </form:select>
	                            </div>                            	
                        	</div>                        	
                        	
                    		<spring:bind path="name">
                    		<c:set var="classes"><c:choose><c:when test="${status.error}">error</c:when></c:choose></c:set>
                            <div class="field-box ${classes}">
                            	<label>Nazwa:</label>
                                <form:input path="name" cssClass="form-control" placeholder="Nazwa"/>
                                <c:if test="${status.error}"><span class="alert-msg"><i class="icon-remove-sign"></i> <c:out value="${status.errorMessage}" escapeXml="false"/></span></c:if>
                            </div>
                            </spring:bind>
                            
                            <c:if test="${reportGenerationForm.fieldfull}">
                            <c:forEach var="field" items="${reportGenerationForm.fields}" varStatus="fstatus">
								<tiles:insertDefinition name="${field.tile}">
								<tiles:putAttribute name="name" type="string">fields[${fstatus.index}]</tiles:putAttribute>
                            	<tiles:putAttribute name="label" type="string">${field.label}</tiles:putAttribute>
                            	<tiles:putAttribute name="value" type="string">${field.value}</tiles:putAttribute>
                            	<tiles:putAttribute name="format" type="string">${field.format}</tiles:putAttribute>
                            	</tiles:insertDefinition>
                            </c:forEach>
                            </c:if> 
                            
                                                    	
                    	
                            <div class="wizard-actions">
                            	<button type="button" class="btn-glow" onclick="location.href='report/execute/${reportGenerationForm.pattern.id}'"><i class="icon-refresh"></i>&nbsp;Wyczyść</button><span>&nbsp;</span>
                            	<button type="submit" class="btn-glow primary btn-next" data-last="Finish">Dalej&nbsp;&nbsp;<i class="icon-chevron-right"></i></button><span>&nbsp;</span>
                            </div>
                        </form:form>
        
</tiles:putAttribute>
</tiles:insertDefinition>        