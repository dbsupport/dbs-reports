<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<tiles:insertDefinition name="tiles-browser" flush="true">
<tiles:putAttribute name="id" type="string">dbs-page-report-archives</tiles:putAttribute>
<tiles:putAttribute name="title" type="string">Lista raportów archiwalnych</tiles:putAttribute>
<tiles:putAttribute name="css" type="string">
<link rel="stylesheet" href="css/compiled/user-list.css" type="text/css" media="screen" />
<link rel="stylesheet" href="css/dbs/dbs-browser.css" type="text/css" media="screen" />
</tiles:putAttribute>

<tiles:putAttribute name="form" type="string">
                <form:form method="post" modelAttribute="reportArchivesForm" action="report/archives" class="">
               		<form:input path="name" cssClass="col-md-5 search" placeholder="Wyszukaj..." onblur="this.form.submit();"/>
                </form:form>
                
                <div class="col-md-5 pull-right">
                    <a href="report/pattern/import" class="btn-flat success pull-right"><span>&#43;</span>ZAIMPORTUJ NOWA DEFINICJĘ</a>
				</div>
</tiles:putAttribute>

<tiles:putAttribute name="content" type="string">

                    <table class="table table-hover">
                        <thead>
                            <tr>
                                <th class="col-md-3 sortable">
                                <tiles:insertDefinition name="tiles-sorter">
                                	<tiles:putAttribute name="name" type="string">name</tiles:putAttribute>
                                	<tiles:putAttribute name="label" type="string">Nazwa pliku</tiles:putAttribute>
            					</tiles:insertDefinition>  
                                </th>
                                <th class="col-md-2 sortable">
                                <tiles:insertDefinition name="tiles-sorter">
                                	<tiles:putAttribute name="name" type="string">generationDate</tiles:putAttribute>
                                	<tiles:putAttribute name="label" type="string">Data wygenerowania</tiles:putAttribute>
            					</tiles:insertDefinition>                                  
                                    <span class="line"></span>
                                </th>
                                <th class="col-md-2 sortable">
                                    <span class="line"></span>Definicja
                                </th>
                                <th class="col-md-3 sortable">
                                    <span class="line"></span>Wygenerował
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
                                ${report.name}
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
                            <td class="align-left">
                             	<c:if test="${!empty report.creator.photo}">
                                <img src="profile/photo/${report.creator.photo.id}" class="img-circle avatar" />
                             	</c:if>
                             	<c:if test="${empty report.creator.photo}">
                             	<img src="img/no-img-personal.png" class="img-circle avatar" />
                             	</c:if>
                                <a href="profile/${report.creator.id}" class="name">${report.creator.name}</a>
                            </td>
                            <td class="align-right">
	                            <ul class="actions">
	                                <li><a href="report/archives/display/${report.id}"><i class="table-edit" title="Podgląd"></i></a></li>
	                                <li><a href="report/archives/email/${pattern.id}"><i class="table-settings" title="Wyślij email'em"></i></a></li>
	                                <li class="last"><a href="report/archives/delete/${report.id}"><i class="table-delete" title="Usuń raport"></i></a></li>
	                            </ul>
                            </td>                             
                        </tr>
                        </c:forEach>
                        </tbody>
                    </table>
            
            <tiles:insertDefinition name="tiles-pager">
            	<c:set var="pager" value="${reportArchivesForm.filter.pager}" scope="request"/>
            </tiles:insertDefinition>            
            
</tiles:putAttribute>
</tiles:insertDefinition>        