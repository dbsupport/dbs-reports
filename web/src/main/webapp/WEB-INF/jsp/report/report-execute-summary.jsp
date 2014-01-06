<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<tiles:insertDefinition name="tiles-wizard" flush="true">
<tiles:putAttribute name="id" type="string">dbs-page-report-execute-summary</tiles:putAttribute>
<tiles:putAttribute name="title" type="string">Generowanie raportu</tiles:putAttribute>
<tiles:putAttribute name="steps" type="string">Ustaw parametry raportu;Wygeneruj raport;Obejrzyj raport</tiles:putAttribute>
<tiles:putAttribute name="step" type="string">3</tiles:putAttribute>
<tiles:putAttribute name="css" type="string">
<link rel="stylesheet" href="css/compiled/new-user.css" type="text/css" media="screen" />
<link rel="stylesheet" href="css/compiled/form-wizard.css" type="text/css" media="screen" />
<link rel="stylesheet" href="css/dbs/dbs-wizard.css" type="text/css" media="screen" />
</tiles:putAttribute>
<tiles:putAttribute name="content" type="string">

						<c:if test="${!empty reports}">
                        <h3>Niezarchiwizowane raporty (${fn:length(reports)}/${maxtemp})</h3>
			            <div class="row">
			                <div class="col-md-11">
			                    <table class="table table-hover">
			                        <thead>
			                            <tr>
			                                <th class="col-md-3">Nazwa pliku
			                                </th>
			                                <th class="col-md-2">Data wygenerowania
			                                    <span class="line"></span>
			                                </th>
			                                <th class="col-md-2">
			                                    <span class="line"></span>Definicja
			                                </th>
			                                <th class="align-right">
			                                    <span class="line"></span>&nbsp;
			                                </th>   			                                
			                            </tr>
			                        </thead>
			                        <tbody>
			                        <c:forEach items="${reports}" var="report">
			                        <!-- row -->
			                        <tr class="first">
			                            <td>
			                            	<a href="report/archives/display/${report.id}" title="Podgląd">${report.name}</a>
			                                
			                            </td>
			                            <td>
			                            	<fmt:formatDate value="${report.generationDate}" type="both" pattern="dd-MM-yyyy HH:mm:ss" />
			                            </td>
			                            <td>
			                            <c:choose>
			                            <c:when test="${report.pattern.active eq true}"><a href="report/pattern/details/${report.pattern.id}">${report.pattern.name} ${report.pattern.version}</a></c:when>
			                            <c:otherwise>${report.pattern.name} ${report.pattern.version}</c:otherwise>
			                            </c:choose>
			                            </td>
			                            <td class="align-right">
				                            <ul class="actions">
				                                <li class="last"><a href="report/archives/archive/${report.id}"><i class="tool" title="Przenieś do archiwum"></i>&nbsp;</a></li>
				                                <li class="last"><a href="report/archives/delete/${report.id}"><i class="table-delete" title="Usuń"></i>&nbsp;</a></li>
				                            </ul>
			                            </td>			                            
			                        </tr>
			                        </c:forEach>
			                        </tbody>
			                    </table>
			                </div>                
			            </div>
			            </c:if>
			            
			            
						<form:form method="post" modelAttribute="reportGenerationForm" action="report/execute/summary" class="">
                    		<input type="hidden" name="page" value="3">
                    		
	                        <div class="wizard-actions">
	                         	<button type="button" class="btn-glow primary btn-prev" onclick="location.href='report/execute/${reportGenerationForm.pattern.id}'">Zacznij od nowa</button><span>&nbsp;</span>
	                        </div>
                        </form:form>
			                                	
        
</tiles:putAttribute>
</tiles:insertDefinition>        