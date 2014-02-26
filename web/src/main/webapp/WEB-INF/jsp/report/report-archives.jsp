<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<tiles:insertDefinition name="tiles-browser" flush="true">
<tiles:putAttribute name="id" type="string">dbs-page-report-archives</tiles:putAttribute>
<tiles:putAttribute name="title" type="string">Lista <c:if test="${current}"> <span class="yours">Twoich</span> </c:if>raportów archiwalnych</tiles:putAttribute>
<tiles:putAttribute name="css" type="string">
<link rel="stylesheet" href="css/compiled/user-list.css" type="text/css" media="screen" />
<link rel="stylesheet" href="css/dbs/dbs-browser.css" type="text/css" media="screen" />
</tiles:putAttribute>
<tiles:putAttribute name="js" type="string">
<script src="js/dbs/dbs-report-archives.js"></script>
</tiles:putAttribute> 
<tiles:putAttribute name="form" type="string">
                <form:form method="post" modelAttribute="reportArchivesForm" action="report/archives" class="dbs-form">
               		<form:input path="name" cssClass="col-md-5 search" placeholder="Wyszukaj..." onblur="this.form.submit();"/>
                </form:form>
                
                <div class="col-md-5 pull-right">
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
                            <td class="align-left">
                            	<c:choose>
                            	<c:when test="${!empty report.creator.photo and report.creator.active eq true}"><img src="profile/photo/${report.creator.photo.id}" class="img-circle avatar" /></c:when>
                            	<c:otherwise><img src="img/no-img-personal.png" class="img-circle avatar" /></c:otherwise>
                            	</c:choose>
                            	
                            	<c:choose>
                            	<c:when test="${report.creator.active eq true and report.creator.accepted eq true}"><a href="profile/${report.creator.id}" class="name"><c:choose><c:when test="${report.creator.global}">${report.creator.description}</c:when><c:otherwise>${report.creator.name}</c:otherwise></c:choose></a></c:when>
                            	<c:when test="${report.creator.active eq true and report.creator.accepted eq false}"><a href="profile/${report.creator.id}" class="name inactive"><c:choose><c:when test="${report.creator.global}">${report.creator.description}</c:when><c:otherwise>${report.creator.name}</c:otherwise></c:choose></a></c:when>
                            	<c:otherwise><a class="name deleted"><c:choose><c:when test="${report.creator.global}">${report.creator.description}</c:when><c:otherwise>${report.creator.name}</c:otherwise></c:choose></a></c:otherwise>
                            	</c:choose>
                            </td>
                            <td class="align-right">
	                            <ul class="actions">
	                            <sec:authorize access="hasAnyRole('Admin,Management')">
	                            <li class="last"><a href="#" class="report-delete" data-url="report/archives/delete/${report.id}"><i class="table-delete" title="Usuń raport"></i></a></li>
	                            </sec:authorize>
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