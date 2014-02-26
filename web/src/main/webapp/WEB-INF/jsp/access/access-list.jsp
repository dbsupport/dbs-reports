<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<tiles:insertDefinition name="tiles-browser" flush="true">
<tiles:putAttribute name="id" type="string">dbs-page-access-list</tiles:putAttribute>
<tiles:putAttribute name="title" type="string">Lista wszystkich uprawnień raportowych</tiles:putAttribute>
<tiles:putAttribute name="css" type="string">
<link rel="stylesheet" href="css/compiled/user-list.css" type="text/css" media="screen" />
<link rel="stylesheet" href="css/dbs/dbs-browser.css" type="text/css" media="screen" />
</tiles:putAttribute>
<tiles:putAttribute name="js" type="string">
<script src="js/dbs/dbs-access-list.js"></script>
</tiles:putAttribute>

<tiles:putAttribute name="form" type="string">
                <form:form method="post" modelAttribute="accessListForm" action="access/list" class="dbs-form">
               		<form:input path="name" cssClass="col-md-5 search" placeholder="Wyszukaj..." onblur="this.form.submit();"/>
                </form:form>
                
                <div class="col-md-5 pull-right">
                	<sec:authorize access="hasAnyRole('Admin,Management')">
                    <a href="access/new" class="btn-flat success pull-right"><span>&#43;</span>DODAJ NOWE UPRAWNIENIE</a>
                    </sec:authorize>
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
                                <th class="col-md-5 sortable">
                                <tiles:insertDefinition name="tiles-sorter">
                                	<tiles:putAttribute name="name" type="string">description</tiles:putAttribute>
                                	<tiles:putAttribute name="label" type="string">Opis</tiles:putAttribute>
            					</tiles:insertDefinition>                                  
                                </th>                                
                                <th class="align-right">
                                    <span class="line"></span>&nbsp;
                                </th>                                
                            </tr>
                        </thead>
                        <tbody>

						<c:forEach items="${accesses}" var="access" varStatus="rstatus">                        
                        <!-- row -->
                        <tr <c:if test="${rstatus.first}">class="first"</c:if>>
                       
                            <td>
			                    <spring:message code="${access.name}" text="${access.name}"/>
                            </td>
                            <td>
                            	<c:choose>
	                            	<c:when test="${!empty access.description}">${access.description}</c:when>
	                            	<c:otherwise>&nbsp;</c:otherwise>
                            	</c:choose>
                            </td>
                            <td>
                                    <ul class="actions">
                                    	<sec:authorize access="hasAnyRole('Admin')"><li><a href="access/edit/${access.id}"><i class="table-edit" title="Edycja uprawnienia"></i></a></li></sec:authorize>
                                        <sec:authorize access="hasAnyRole('Admin')"><li class="last"><a href="#" class="access-delete" data-url="access/delete/${access.id}"><i class="table-delete" title="Usunięcie uprawnienia"></i></a></li></sec:authorize>
                                    </ul>
                             </td>                            
                        </tr>
                        </c:forEach>
                        
                        </tbody>
                    </table>
                    
		            <tiles:insertDefinition name="tiles-pager">
		            	<c:set var="pager" value="${accessListForm.filter.pager}" scope="request"/>
		            </tiles:insertDefinition>    
        
</tiles:putAttribute>
</tiles:insertDefinition>        