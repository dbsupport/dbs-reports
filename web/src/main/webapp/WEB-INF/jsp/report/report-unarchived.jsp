<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<tiles:insertDefinition name="tiles-browser-tables" flush="true">
<tiles:putAttribute name="id" type="string">dbs-page-report-unarchived</tiles:putAttribute>
<tiles:putAttribute name="title" type="string">Twoje raporty (${reportsunarchivedform.filter.pager.dataSize}/${max})</tiles:putAttribute>
<tiles:putAttribute name="css" type="string">
<link rel="stylesheet" href="css/compiled/form-wizard.css" type="text/css" media="screen" />
<link rel="stylesheet" href="css/dbs/dbs-activedirectory-list.css" type="text/css" media="screen" />
<link rel="stylesheet" href="css/dbs/dbs-report-logs.css" type="text/css" media="screen" />
</tiles:putAttribute>


<tiles:putAttribute name="js" type="string">
<script src="js/jquery.dataTables.js"></script>
<script src="js/jquery.multiDownload.js"></script>
<script src="js/dbs/dbs-report-logs.js"></script>

<script>
$(document).on("notification-brandnew", function(e) {
    setTimeout(function () { $('#reportsunarchivedform').submit(); }, 2000);
});
$(document).ready(function() {
    $("input[type=checkbox].checker-ctrl").click(function() {
        var checked = $(this).is(':checked');
        $("input:checkbox[name='id']").each(function() {
            $(this).prop('checked', checked);
        });
    });

    $("input[type=checkbox].report").click(function() {
        var checked = $(this).is(':checked');
        //alert($(this).val());
        if (checked) {
        	$("a#report"+$(this).val()).addClass("ready2download");
        } else {
        	$("a#report"+$(this).val()).removeClass("ready2download");
        }
    });

	$('#downloadall').click(function (event) {
	    event.preventDefault();
	    $('.ready2download').multiDownload({delay: 500});
	});
});

</script>
</tiles:putAttribute>

<tiles:putAttribute name="form" type="string">
                <form:form method="post" modelAttribute="reportsunarchivedform" id="reportsunarchivedform" action="report/unarchived/list" class="dbs-form">
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
                                
                            </tr>
                        </tfoot> 
                                                
                        <tbody>
                        <c:forEach items="${reports}" varStatus="rstatus" var="report">
                        <c:set var="trclass" scope="page"><c:choose><c:when test="${rstatus.index mod 2 eq 0}">odd</c:when><c:otherwise>even</c:otherwise></c:choose></c:set>
                        <tr class="${trclass}">
                                <td>&nbsp;&nbsp;&nbsp;<form:checkbox path="id" value="${report.id}" class="report"/></td>
                                <td>
                                <c:choose>
                                <c:when test="${report.downloadable}">
                                    <a href="report/unarchived/${report.id}/download" title="Podgląd" id="report${report.id}">${report.name}</a>
                                </c:when>
                                <c:otherwise><c:out value="${report.name}"/></c:otherwise>
                                </c:choose>
                                </td>
                                
                                <td>
                                <c:choose>
                                <c:when test="${report.phase.ordered}">
                                    <div class="col-md-4">
                                        <span class="label label-warning report-phase-label">zlecony</span>
                                    </div>                                
                                </c:when>
                                <c:when test="${report.phase.status == 'READY'}">
                                    <div class="col-md-4 log">
                                    <a class="pop-dialog-log-trigger"><span class="label label-info report-phase-label">gotowy</span></a>
                                    <%@ include file="/WEB-INF/jsp/report/report-logs.jsp" %> 
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="col-md-4 log">
                                    <a class="pop-dialog-log-trigger"><span class="label label-success report-phase-label">niezarchiwizowany</span></a>
                                    <%@ include file="/WEB-INF/jsp/report/report-logs.jsp" %> 
                                    </div>                                
                                </c:otherwise>
                                </c:choose>
                                </td>
                                
                                <td>
                                <c:choose>
                                <c:when test="${report.status == 'FAILED'}"><span class="label label-danger report-phase-label">błędny</span></c:when>
                                <c:otherwise><span class="label label-success report-phase-label">poprawny</span></c:otherwise>
                                </c:choose>
                                </td>
                                
                                <td><fmt:formatDate value="${report.generationDate}" type="both" pattern="dd-MM-yyyy HH:mm:ss" /></td>
                                <td>
		                            <c:choose>
		                            <c:when test="${report.pattern.active eq true}"><a href="report/pattern/details/${report.pattern.id}">${report.pattern.name} ${report.pattern.version}</a></c:when>
		                            <c:otherwise>${report.pattern.name} ${report.pattern.version}</c:otherwise>
		                            </c:choose>
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
                    
                    <button type="button" class="btn-glow primary btn-next" data-last="Finish" id="downloadall">
                    <i class="icon-download"></i>
                    Pobierz&nbsp;&nbsp;</button><span>&nbsp;</span>                    
                    </div>
                   
    </form:form>
</div>                        
        
</tiles:putAttribute>
</tiles:insertDefinition>        