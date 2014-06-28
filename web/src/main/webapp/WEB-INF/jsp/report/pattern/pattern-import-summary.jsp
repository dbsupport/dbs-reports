<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<tiles:insertDefinition name="tiles-wizard" flush="true">
<tiles:putAttribute name="id" type="string">dbs-page-report-pattern-import</tiles:putAttribute>
<tiles:putAttribute name="title" type="string">Import nowej definicji raportu</tiles:putAttribute>
<tiles:putAttribute name="steps" type="string">Wczytaj plik;Zapisz<c:if test="${!empty pattern.form}">;Sprawdź formatkę</c:if></tiles:putAttribute>
<tiles:putAttribute name="step" type="string">2</tiles:putAttribute>
<tiles:putAttribute name="css" type="string">
<link rel="stylesheet" href="css/compiled/new-user.css" type="text/css" media="screen" />
<link rel="stylesheet" href="css/compiled/form-wizard.css" type="text/css" media="screen" />
<link rel="stylesheet" href="css/dbs/dbs-wizard.css" type="text/css" media="screen" />
</tiles:putAttribute>
<tiles:putAttribute name="content" type="string">

                    	<form:form method="post" modelAttribute="patternImportForm" action="report/pattern/import/summary" class="dbs-form">
                    		<input type="hidden" name="page" value="2"/>
                    		
                    	 	<div class="field-box">
                            	<label>Nazwa szablonu:</label>
                                	<input class="form-control inline-input" type="text" readonly="readonly" value="${pattern.name}">
                        	</div>
                    	 	<div class="field-box">
                            	<label>Wersja szablonu:</label>
                                	<input class="form-control inline-input" type="text" readonly="readonly" value="${pattern.version}">
                        	</div>
                    	 	<div class="field-box">
                            	<label>Formaty:</label>
                                	<input class="form-control inline-input" type="text" readonly="readonly" value="${pattern.formatsAsString}">
                        	</div>
                    	 	<div class="field-box">
                            	<label>Autor:</label>
                                	<input class="form-control inline-input" type="text" readonly="readonly" value="${pattern.author}">
                        	</div>
                    	 	<div class="field-box">
                            	<label>Dostępny dla ról:</label>
                                	<input class="form-control inline-input" type="text" readonly="readonly" value="${pattern.accessesAsString}"/>
                        	</div>                        	
                    	 	<div class="field-box">
                            	<label>Silnik raportów:</label>
                                	<input class="form-control inline-input" type="text" readonly="readonly" value="<spring:message code="${pattern.factory}" text="${pattern.factory}"/>">
                        	</div>
	                        <div class="field-box">
	                            <label>Manifest:</label>
	                                <textarea class="form-control" rows="4" readonly="readonly"><c:out value="${pattern.manifest.text}"/></textarea>
	                        </div>                        	
                    	
                            <div class="wizard-actions">
		                        <button type="button" class="btn-glow primary btn-prev" onclick="location.href='report/pattern/import/read'"><i class="icon-chevron-left"></i>&nbsp;Popraw</button><span>&nbsp;</span>
		                        <c:if test="${!empty pattern.form}">
		                        <button type="button" class="btn-glow btn-next" onclick="location.href='report/pattern/import/form'">Sprawdź formatkę</button><span>&nbsp;</span>
		                        </c:if>
		                        <button type="submit" class="btn-glow success btn-finish" style="display: inline-block;">Zapisz definicję!</button><span>&nbsp;</span>                            
                            </div>
                        </form:form>
        
</tiles:putAttribute>
</tiles:insertDefinition>        