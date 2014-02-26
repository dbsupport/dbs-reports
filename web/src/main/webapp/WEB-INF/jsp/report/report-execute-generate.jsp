<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<tiles:insertDefinition name="tiles-wizard" flush="true">
<tiles:putAttribute name="id" type="string">dbs-page-report-execute-generate</tiles:putAttribute>
<tiles:putAttribute name="title" type="string">Generowanie raportu</tiles:putAttribute>
<tiles:putAttribute name="steps" type="string">Ustaw parametry raportu;Wygeneruj raport;Obejrzyj raport</tiles:putAttribute>
<tiles:putAttribute name="step" type="string">2</tiles:putAttribute>
<tiles:putAttribute name="css" type="string">
<link rel="stylesheet" href="css/compiled/new-user.css" type="text/css" media="screen" />
<link rel="stylesheet" href="css/compiled/form-wizard.css" type="text/css" media="screen" />
<link rel="stylesheet" href="css/dbs/dbs-wizard.css" type="text/css" media="screen" />
</tiles:putAttribute>
<tiles:putAttribute name="content" type="string">

                    	<form:form method="post" modelAttribute="reportGenerationForm" action="report/execute/generate" class="dbs-form">
                    		<input type="hidden" name="page" value="2">
                    		
                            <div class="field-box">
                            	<label>Definicja:</label>
                            	<input class="form-control inline-input" type="text" readonly="readonly" value="${reportGenerationForm.pattern.name} ${reportGenerationForm.pattern.version}"/>
                            </div>
                            
                    	 	<div class="field-box">
                            	<label>Format:</label>
	                            	<input class="form-control inline-input" type="text" readonly="readonly" value="${reportGenerationForm.format.patternExtension}"/>
                        	</div>                        	

                    	 	<div class="field-box">
                            	<label>Nazwa:</label>
	                            	<input class="form-control inline-input" type="text" readonly="readonly" value="${reportGenerationForm.fullname}"/>
                        	</div> 
                    		
                            <c:if test="${reportGenerationForm.fieldfull}">
                            <c:forEach var="field" items="${reportGenerationForm.fields}" varStatus="fstatus">
								<tiles:insertDefinition name="${field.tile}">
								<tiles:putAttribute name="name" type="string">fields[${fstatus.index}]</tiles:putAttribute>
                            	<tiles:putAttribute name="label" type="string">${field.label}</tiles:putAttribute>
                            	<tiles:putAttribute name="value" type="string">${field.value}</tiles:putAttribute>
                            	<tiles:putAttribute name="format" type="string">${field.format}</tiles:putAttribute>
                            	<tiles:putAttribute name="tooltip" type="string">${field.tooltip}</tiles:putAttribute>
                            	<tiles:putAttribute name="inputclass" type="string">form-control inline-input</tiles:putAttribute>
                            	<tiles:putAttribute name="attributes" type="string">readonly="readonly"</tiles:putAttribute>
                            	</tiles:insertDefinition>
                            </c:forEach>
                            </c:if>                     		

                    		
                            <div class="wizard-actions">
                            	<button type="button" class="btn-glow primary btn-prev" onclick="location.href='report/execute/form'"><i class="icon-chevron-left"></i>&nbsp;Popraw</button><span>&nbsp;</span>
		                        <button type="submit" class="btn-glow success btn-finish" style="display: inline-block;">Generuj!</button><span>&nbsp;</span>
                            </div>
                        </form:form>
        
</tiles:putAttribute>
</tiles:insertDefinition>        