<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<tiles:insertDefinition name="tiles-wizard" flush="true">
<tiles:putAttribute name="id" type="string">dbs-page-report-pattern-import</tiles:putAttribute>
<tiles:putAttribute name="title" type="string">Import nowej definicji raportu</tiles:putAttribute>
<tiles:putAttribute name="steps" type="string">Wczytaj plik;Zapisz</tiles:putAttribute>
<tiles:putAttribute name="step" type="string">1</tiles:putAttribute>
<tiles:putAttribute name="css" type="string">
<link rel="stylesheet" href="css/compiled/new-user.css" type="text/css" media="screen" />
<link rel="stylesheet" href="css/compiled/form-wizard.css" type="text/css" media="screen" />
<link rel="stylesheet" href="css/dbs/dbs-wizard.css" type="text/css" media="screen" />
</tiles:putAttribute>
<tiles:putAttribute name="content" type="string">

                    	<form:form method="post" modelAttribute="patternImportForm" action="report/pattern/import/read" class="" enctype="multipart/form-data">
                    		<input type="hidden" name="page" value="1"/>
                    		
                    		<spring:bind path="file">
                    		<c:set var="classes"><c:choose><c:when test="${status.error}">error</c:when></c:choose></c:set>
	                        <div class="field-box ${classes}"> 
	                        	<label>Szablon</label>
	                                <input type="file" name="file" class="form-control" accept=".zip,.7z"/>
	                                <c:if test="${status.error}"><span class="alert-msg"><i class="icon-remove-sign"></i> <c:out value="${status.errorMessage}" escapeXml="false"/></span></c:if>
                            </div>
                            </spring:bind>
                            
                            <div class="wizard-actions">
		                        <button type="submit" class="btn-glow primary btn-next" data-last="Finish">Dalej&nbsp;&nbsp;<i class="icon-chevron-right"></i></button><span>&nbsp;</span>
                            </div>
                        </form:form>

        
</tiles:putAttribute>
</tiles:insertDefinition>        