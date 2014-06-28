<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<tiles:insertDefinition name="tiles-browser-tables" flush="true">
<tiles:putAttribute name="id" type="string">dbs-page-report-unarchived</tiles:putAttribute>
<tiles:putAttribute name="title" type="string">Twoje</span> niezarchiwizowane raporty (${reportsunarchivedform.filter.pager.dataSize}/${max})</tiles:putAttribute>
<tiles:putAttribute name="css" type="string">
<link rel="stylesheet" href="css/compiled/form-wizard.css" type="text/css" media="screen" />
<link rel="stylesheet" href="css/dbs/dbs-activedirectory-list.css" type="text/css" media="screen" />

</tiles:putAttribute>
<tiles:putAttribute name="js" type="string">
<script src="js/jquery.dataTables.js"></script>
<script src="js/dbs/dbs-tables-checker.js"></script>
</tiles:putAttribute>

<tiles:putAttribute name="form" type="string">
                <form:form method="post" modelAttribute="reportsunarchivedform" action="report/unarchived/list" class="dbs-form">
                    <input type="hidden" name="action" value=""/>
                    
                <div class="row filter-block">
                    <div class="pull-right">
                        <div class="ui-select">
		                    <spring:bind path="phase">
		                    <c:set var="classes"><c:choose><c:when test="${status.error}">error</c:when></c:choose></c:set>     
		                        <form:select path="phase" onchange="this.form.submit()">
		                            <form:options items="${phases}" itemLabel="label"/>
		                        </form:select>
		                        <c:if test="${status.error}"><span class="alert-msg"><i class="icon-remove-sign"></i> <c:out value="${status.errorMessage}" escapeXml="false"/></span></c:if>
		                    </spring:bind>
                        </div>
                        
                        <div class="ui-select">
                            <spring:bind path="status">
                            <c:set var="classes"><c:choose><c:when test="${status.error}">error</c:when></c:choose></c:set>     
                                <form:select path="status" onchange="this.form.submit()">
                                    <form:options items="${statuses}" itemLabel="label"/>
                                </form:select>
                                <c:if test="${status.error}"><span class="alert-msg"><i class="icon-remove-sign"></i> <c:out value="${status.errorMessage}" escapeXml="false"/></span></c:if>
                            </spring:bind>
                        </div>
                        
                        <form:input path="value" placeholder="Wyszukaj..." onblur="this.form.submit();"/>
                    </div>
                </div>                    
                    

                </form:form>
</tiles:putAttribute>

<tiles:putAttribute name="custom-alerts" type="string">
<spring:hasBindErrors name="reportsunarchivedform">
    <c:forEach items="${errors.globalErrors}" var="error">
                <div class="alert alert-danger">
                    <i class="icon-remove-sign"></i>
                    <spring:message code="${error.code}" text="${error.defaultMessage}" htmlEscape="false"/>
                </div>    
    </c:forEach>
</spring:hasBindErrors>    
</tiles:putAttribute>

<tiles:putAttribute name="content" type="string">
<div class="form-wrapper">
    <form:form method="post" modelAttribute="reportsunarchivedform" action="report/unarchived/list" class="dbs-form">
        <input type="hidden" name="action" value=""/>
        
                            <table cellpadding="0" cellspacing="0" border="0" class="dataTable" id="">
                        <thead>
                            <tr>
                                <th><input type="checkbox" class="checker-ctrl"/></th>
                                
                                <tiles:insertDefinition name="tiles-sorter-tables">
                                <tiles:putAttribute name="name" type="string">name</tiles:putAttribute>
                                <tiles:putAttribute name="label" type="string">Nazwa</tiles:putAttribute>
                                <c:set var="sorter" value="${reportsunarchivedform.filter.sorter}" scope="request"/>
                                </tiles:insertDefinition>
                                
                                <tiles:insertDefinition name="tiles-sorter-tables">
                                <tiles:putAttribute name="name" type="string">phase</tiles:putAttribute>
                                <tiles:putAttribute name="label" type="string">Stan</tiles:putAttribute>
                                <c:set var="sorter" value="${reportsunarchivedform.filter.sorter}" scope="request"/>
                                </tiles:insertDefinition>                            
                                
                                
                                <tiles:insertDefinition name="tiles-sorter-tables">
                                <tiles:putAttribute name="name" type="string">status</tiles:putAttribute>
                                <tiles:putAttribute name="label" type="string">Status</tiles:putAttribute>
                                <c:set var="sorter" value="${reportsunarchivedform.filter.sorter}" scope="request"/>
                                </tiles:insertDefinition>                            
                                                            
                                
                                <tiles:insertDefinition name="tiles-sorter-tables">
                                <tiles:putAttribute name="name" type="string">generationDate</tiles:putAttribute>
                                <tiles:putAttribute name="label" type="string">Data wygenerowania</tiles:putAttribute>
                                <c:set var="sorter" value="${reportsunarchivedform.filter.sorter}" scope="request"/>
                                </tiles:insertDefinition>
                                
                                <tiles:insertDefinition name="tiles-sorter-tables">
                                <tiles:putAttribute name="name" type="string">locationName</tiles:putAttribute>
                                <tiles:putAttribute name="label" type="string">Definicja</tiles:putAttribute>
                                <c:set var="sorter" value="${reportsunarchivedform.filter.sorter}" scope="request"/>
                                </tiles:insertDefinition>
                                
                                <th tabindex="0" class="">
                                    <a class="sorter-ctrl">&nbsp;</a>
                                </th>                                
                                
                            </tr>
                        </thead>
                        <tfoot>
                            <tr>
                                <th><input type="checkbox" class="checker-ctrl"/></th>
                                
                                <tiles:insertDefinition name="tiles-sorter-tables">
                                <tiles:putAttribute name="name" type="string">name</tiles:putAttribute>
                                <tiles:putAttribute name="label" type="string">Nazwa</tiles:putAttribute>
                                <c:set var="sorter" value="${reportsunarchivedform.filter.sorter}" scope="request"/>
                                </tiles:insertDefinition>
                                                         
                                
                                <tiles:insertDefinition name="tiles-sorter-tables">
                                <tiles:putAttribute name="name" type="string">phase</tiles:putAttribute>
                                <tiles:putAttribute name="label" type="string">Stan</tiles:putAttribute>
                                <c:set var="sorter" value="${reportsunarchivedform.filter.sorter}" scope="request"/>
                                </tiles:insertDefinition>                            
                                
                                
                                <tiles:insertDefinition name="tiles-sorter-tables">
                                <tiles:putAttribute name="name" type="string">status</tiles:putAttribute>
                                <tiles:putAttribute name="label" type="string">Status</tiles:putAttribute>
                                <c:set var="sorter" value="${reportsunarchivedform.filter.sorter}" scope="request"/>
                                </tiles:insertDefinition>                            
                                
                                
                                <tiles:insertDefinition name="tiles-sorter-tables">
                                <tiles:putAttribute name="name" type="string">generationDate</tiles:putAttribute>
                                <tiles:putAttribute name="label" type="string">Data wygenerowania</tiles:putAttribute>
                                <c:set var="sorter" value="${reportsunarchivedform.filter.sorter}" scope="request"/>
                                </tiles:insertDefinition>
                                
                                <tiles:insertDefinition name="tiles-sorter-tables">
                                <tiles:putAttribute name="name" type="string">locationName</tiles:putAttribute>
                                <tiles:putAttribute name="label" type="string">Definicja</tiles:putAttribute>
                                <c:set var="sorter" value="${reportsunarchivedform.filter.sorter}" scope="request"/>
                                </tiles:insertDefinition>  
                                
                                <th tabindex="0" class="">
                                    <a class="sorter-ctrl">&nbsp;</a>
                                </th> 
                            </tr>
                        </tfoot> 
                                                
                        <tbody>
                        <c:forEach items="${reports}" varStatus="rstatus" var="report">
                        <c:set var="trclass" scope="page"><c:choose><c:when test="${rstatus.index mod 2 eq 0}">odd</c:when><c:otherwise>even</c:otherwise></c:choose></c:set>
                        <tr class="${trclass}">
                                <td>&nbsp;&nbsp;&nbsp;<form:checkbox path="id" value="${report.id}"/></td>
                                <td>
                                <c:choose>
                                <c:when test="${report.status == 'OK'}">
                                    <a href="report/unarchived/${report.id}/download" title="Podgląd">${report.name}</a>
                                </c:when>
                                <c:otherwise><c:out value="${report.name}"/></c:otherwise>
                                </c:choose>
                                </td>
                                
                                <td>
                                <c:choose>
                                <c:when test="${report.phase.status == 'READY'}">
                                <span class="label label-info">&nbsp;&nbsp;&nbsp;&nbsp;gotowy&nbsp;&nbsp;&nbsp;&nbsp;</span>
                                </c:when>
                                <c:otherwise><span class="label label-success">niezarchiwizowany</span></c:otherwise>
                                </c:choose>
                                </td>
                                
                                
                                <td>
                                <c:choose>
                                <c:when test="${report.status == 'FAILED'}">
                                
                                
                                <span class="label label-danger">błędny</span>
                                <!-- a href="#" class="trigger">błędny</a>
                                
                                
                                
                <div class="pop-dialog">
                    <div class="pointer right">
                        <div class="arrow"></div>
                        <div class="arrow_border"></div>
                    </div>
                    <div class="body">
                        <a href="#" class="close-icon"><i class="icon-remove-sign"></i></a>
                        <div class="messages">
                            <a href="#" class="item">
                                <img src="img/contact-img.png" class="display">
                                <div class="name">Alejandra Galván</div>
                                <div class="msg">
                                    There are many variations of available, but the majority have suffered alterations.
                                </div>
                                <span class="time"><i class="icon-time"></i> 13 min.</span>
                            </a>
                            <div class="footer">
                                <a href="#" class="logout">View all messages</a>
                            </div>
                        </div>
                    </div>
                </div-->                                
                                
                                
                                </c:when>
                                <c:otherwise><span class="label label-success">poprawny</c:otherwise>
                                </c:choose>
                                </td>
                                
                                <td><fmt:formatDate value="${report.generationDate}" type="both" pattern="dd-MM-yyyy HH:mm:ss" /></td>
                                <td>${report.pattern.name} ${report.pattern.version}</td>
                                
                                <td>
                                <c:if test="${report.status eq 'OK' and (report.phase.status eq 'READY')}">
				                    <a class="btn-flat primary btn-next" href="report/unarchived/confirm/${report.id}">
				                    <i class="icon-ok"></i>
				                    Zaakceptuj&nbsp;&nbsp;</a><span>&nbsp;</span>
				                </c:if>
				                
                                <c:if test="${report.status eq 'OK' and (report.phase.status eq 'READY' or report.phase.status eq 'TRANSIENT')}">
                                    <a class="btn-flat success btn-finish" href="report/unarchived/archive/${report.id}">
                                    <i class="icon-folder-open"></i>
                                    Archiwizuj&nbsp;&nbsp;</a><span>&nbsp;</span>
                                </c:if>
                    
				                
				                    <a class="btn-flat inverse" href="report/unarchived/delete/${report.id}">
				                    <i class="icon-remove"></i>
				                    Usuń&nbsp;&nbsp;</a>                                
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                    
                    <tiles:insertDefinition name="tiles-pager-tables">
                        <c:set var="pager" value="${reportsunarchivedform.filter.pager}" scope="request"/>
                    </tiles:insertDefinition>
                    
                    
                      
                    <div class="field-box">
                    <c:if test="${(reportsunarchivedform.status eq 'ALL' or  reportsunarchivedform.status eq 'OK') and (reportsunarchivedform.phase eq 'ALL' or reportsunarchivedform.phase eq 'READY')}">
                    <button type="submit" class="btn-glow primary btn-next" data-last="Finish" onclick="this.form.action.value='CONFIRM'">
                    <i class="icon-ok"></i>
                    Zaakceptuj&nbsp;&nbsp;</button><span>&nbsp;</span>
                    </c:if>                    
                    
                    <c:if test="${reportsunarchivedform.status eq 'ALL' or  reportsunarchivedform.status eq 'OK'}">
                    <button type="submit" class="btn-glow success btn-finish" data-last="Finish" onclick="this.form.action.value='ARCHIVE'">
                    <i class="icon-folder-open"></i>
                    Archiwizuj&nbsp;&nbsp;</button><span>&nbsp;</span>
                    </c:if>

                    <button type="submit" class="btn-glow inverse" data-last="Finish" onclick="this.form.action.value='REMOVE'">
                    <i class="icon-remove"></i>
                    Usuń&nbsp;&nbsp;</button><span>&nbsp;</span>
                    </div>
                   
    </form:form>
</div>                        
        
</tiles:putAttribute>
</tiles:insertDefinition>        