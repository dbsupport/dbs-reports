<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<tiles:insertDefinition name="tiles-browser" flush="true">
<tiles:putAttribute name="id" type="string">dbs-page-report-pattern-list</tiles:putAttribute>
<tiles:putAttribute name="title" type="string">Lista dostępnych definicji raportów</tiles:putAttribute>
<tiles:putAttribute name="css" type="string">
<link rel="stylesheet" href="css/compiled/user-list.css" type="text/css" media="screen" />
<link rel="stylesheet" href="css/dbs/dbs-browser.css" type="text/css" media="screen" />
</tiles:putAttribute>
<tiles:putAttribute name="js" type="string">
<script src="js/dbs/dbs-pattern-list.js"></script>
</tiles:putAttribute> 

<tiles:putAttribute name="form" type="string">
                <form:form method="post" modelAttribute="patternListForm" action="report/pattern/list" class="">
               		<form:input path="name" cssClass="col-md-5 search" placeholder="Wyszukaj..." onblur="this.form.submit();"/>
                </form:form>

                <div class="col-md-5 pull-right">
                    <a href="report/pattern/import" class="btn-flat success pull-right"><span>&#43;</span>ZAIMPORTUJ DEFINICJĘ RAPORTU</a>
				</div>                
</tiles:putAttribute>

<tiles:putAttribute name="content" type="string">

                    <table class="table table-hover">
                        <thead>
                            <tr>
                                <th class="col-md-3 sortable">
                                <tiles:insertDefinition name="tiles-sorter">
                                	<tiles:putAttribute name="name" type="string">name</tiles:putAttribute>
                                	<tiles:putAttribute name="label" type="string">Nazwa</tiles:putAttribute>
            					</tiles:insertDefinition>
                                </th>
                                <th class="col-md-1 sortable">
                                <tiles:insertDefinition name="tiles-sorter">
                                	<tiles:putAttribute name="name" type="string">version</tiles:putAttribute>
                                	<tiles:putAttribute name="label" type="string">Wersja</tiles:putAttribute>
            					</tiles:insertDefinition>                                
                                <span class="line"></span>
                                </th>
                                <th class="col-md-3 sortable">
                                <tiles:insertDefinition name="tiles-sorter">
                                	<tiles:putAttribute name="name" type="string">uploadDate</tiles:putAttribute>
                                	<tiles:putAttribute name="label" type="string">Data importu</tiles:putAttribute>
            					</tiles:insertDefinition>
                                <span class="line"></span>
                                </th>
                                <th class="col-md-2 sortable">
                                    <span class="line"></span>Zaimportował
                                </th>
                                <th class="col-md-2 sortable">
                                    <span class="line"></span>Dostępy
                                </th>
                                <th class="align-right">
                                    <span class="line"></span>
                                </th>                                
                            </tr>
                        </thead>
                        
                        <tbody>
                        <c:forEach var="pattern" items="${patterns}" varStatus="status">
                        <!-- row -->
                        <tr class="first">
                            <td>
                                <a href="report/pattern/download/${pattern.id}"><c:out value="${pattern.name}"/></a>
                            </td>
                            <td>
                                <c:out value="${pattern.version}"/>
                            </td>
                            <td>
                            	<fmt:formatDate value="${pattern.uploadDate}" type="both" pattern="dd-MM-yyyy HH:mm:ss" />
                            </td>
                            <td>
                            	<c:choose>
                            	<c:when test="${!empty pattern.creator.photo and pattern.creator.active eq true}"><img src="profile/photo/${pattern.creator.photo.id}" class="img-circle avatar" /></c:when>
                            	<c:otherwise><img src="img/no-img-personal.png" class="img-circle avatar" /></c:otherwise>
                            	</c:choose>
                            	
                            	<c:choose>
                            	<c:when test="${pattern.creator.active eq true and pattern.creator.accepted eq true}"><a href="profile/${pattern.creator.id}" class="name"><c:choose><c:when test="${pattern.creator.global}">${pattern.creator.description}</c:when><c:otherwise>${pattern.creator.name}</c:otherwise></c:choose></a></c:when>
                            	<c:when test="${pattern.creator.active eq true and pattern.creator.accepted eq false}"><a href="profile/${pattern.creator.id}" class="name inactive"><c:choose><c:when test="${pattern.creator.global}">${pattern.creator.description}</c:when><c:otherwise>${pattern.creator.name}</c:otherwise></c:choose></a></c:when>
                            	<c:otherwise><a class="name deleted"><c:choose><c:when test="${pattern.creator.global}">${pattern.creator.description}</c:when><c:otherwise>${pattern.creator.name}</c:otherwise></c:choose></a></c:otherwise>
                            	</c:choose>
               
                            </td>                            
                            <td>
                            	${pattern.accessesAsString}
                            </td>
                            <td class="align-right">
	                            <ul class="actions">
	                                <li><a href="report/execute/${pattern.id}"><i class="table-settings" title="Generowanie raportu"></i></a></li>
	                                <li><a href="report/pattern/details/${pattern.id}"><i class="table-edit" title="Szczegóły definicji"></i></a></li>
	                                <li class="last"><a href="#" class="pattern-delete" data-url="report/pattern/delete/${pattern.id}"><i class="table-delete" title="Usunięcie definicji"></i></a></li>
	                            </ul>
                            </td>                            
                        </tr>
                        </c:forEach>
                        </tbody>
                    </table>
            
		            <tiles:insertDefinition name="tiles-pager">
		            	<c:set var="pager" value="${patternListForm.filter.pager}" scope="request"/>
		            </tiles:insertDefinition>
            
</tiles:putAttribute>
</tiles:insertDefinition>        