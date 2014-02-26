<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<tiles:insertDefinition name="tiles-browser" flush="true">
<tiles:putAttribute name="id" type="string">dbs-page-report-archives-temporary</tiles:putAttribute>
<tiles:putAttribute name="title" type="string"><span class="yours">Twoje</span> niezarchiwizowane raporty (${fn:length(reports)}/${maxtemp})</tiles:putAttribute>
<tiles:putAttribute name="css" type="string">
<link rel="stylesheet" href="css/compiled/user-list.css" type="text/css" media="screen" />
<link rel="stylesheet" href="css/dbs/dbs-browser.css" type="text/css" media="screen" />
</tiles:putAttribute>
<tiles:putAttribute name="js" type="string">
<script src="js/dbs/dbs-report-archives.js"></script>
</tiles:putAttribute> 

<tiles:putAttribute name="content" type="string">

                    <table class="table table-hover">
                        <thead>
                            <tr>
                                <th class="col-md-3 sortable"><span class="line"></span>Nazwa pliku</th>
                                <th class="col-md-2 sortable"><span class="line"></span>Data wygenerowania</th>
                                <th class="col-md-2 sortable"><span class="line"></span>Definicja</th>
                                <th class="align-right"><span class="line"></span>&nbsp;</th>                             
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
	                                <li ><a href="report/archives/archive/${report.id}"><i class="tool" title="Przenieś do archiwum"></i>&nbsp;</a></li>
	                                <li class="last"><a href="#" class="report-delete" data-url="report/archives/temporary/delete/${report.id}"><i class="table-delete" title="Usuń"></i>&nbsp;</a></li>
	                            </ul>
                            </td>                             
                        </tr>
                        </c:forEach>
                        </tbody>
                    </table>
            
            <tiles:insertDefinition name="tiles-pager">
            	<c:set var="pager" value="${reportArchivesTemporaryForm.filter.pager}" scope="request"/>
            </tiles:insertDefinition>            
            
</tiles:putAttribute>
</tiles:insertDefinition>        