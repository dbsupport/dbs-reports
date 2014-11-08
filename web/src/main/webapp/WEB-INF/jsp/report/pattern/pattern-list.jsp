<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>


<tiles:insertDefinition name="tiles-browser-tables" flush="true">
<tiles:putAttribute name="id" type="string">dbs-page-report-pattern-list</tiles:putAttribute>
<tiles:putAttribute name="title" type="string">Lista <c:if test="${current}"> <span class="yours">Twoich</span> </c:if>definicji raportów</tiles:putAttribute>
<tiles:putAttribute name="css" type="string">
<link rel="stylesheet" href="css/compiled/form-wizard.css" type="text/css" media="screen" />
</tiles:putAttribute>

<tiles:putAttribute name="js" type="string">
<script src="js/jquery.dataTables.js"></script>
<script src="js/dbs/dbs-pattern-list.js"></script>
</tiles:putAttribute>

<tiles:putAttribute name="form" type="string">
                <form:form method="post" modelAttribute="patternListForm" class="dbs-form">
                    
                <div class="row filter-block">
                    <div class="pull-right">
                        <div class="ui-select">
                            <spring:bind path="access">
                            <c:set var="classes"><c:choose><c:when test="${status.error}">error</c:when></c:choose></c:set>     
                                <form:select path="access" onchange="this.form.submit()">
                                    <form:option value="">- wszystkie -</form:option>
                                    <form:options items="${accesses}" itemLabel="name" itemValue="name"/>
                                </form:select>
                                <c:if test="${status.error}"><span class="alert-msg"><i class="icon-remove-sign"></i> <c:out value="${status.errorMessage}" escapeXml="false"/></span></c:if>
                            </spring:bind>
                        </div>                    
                    
	                    <form:input path="value" placeholder="Wyszukaj..." onblur="this.form.submit();"/>
	                    
	                    <sec:authorize access="hasAnyRole('Admin')">
	                    <span>&nbsp;</span><a href="report/pattern/import" class="btn-glow primary btn-next"><i class="icon-upload"></i>&nbsp;Zaimportuj definicję</a>
	                    </sec:authorize>
                    </div>
                    
                </div>                    

                </form:form>

</tiles:putAttribute>

<tiles:putAttribute name="content" type="string">
<div class="form-wrapper">
    <form:form method="post" modelAttribute="patternListForm" class="dbs-form">
        
                        <table cellpadding="0" cellspacing="0" border="0" class="dataTable" id="">
                        <thead>
                            <tr>
                                <%--th><input type="checkbox" class="checker-ctrl"/></th--%>
                                
                                <tiles:insertDefinition name="tiles-sorter-tables">
                                <tiles:putAttribute name="name" type="string">name</tiles:putAttribute>
                                <tiles:putAttribute name="label" type="string">Nazwa</tiles:putAttribute>
                                <c:set var="sorter" value="${patternListForm.filter.sorter}" scope="request"/>
                                </tiles:insertDefinition>
                                
                                <tiles:insertDefinition name="tiles-sorter-tables">
                                <tiles:putAttribute name="name" type="string">version</tiles:putAttribute>
                                <tiles:putAttribute name="label" type="string">Wersja</tiles:putAttribute>
                                <c:set var="sorter" value="${patternListForm.filter.sorter}" scope="request"/>
                                </tiles:insertDefinition>
                                
								<th>
								    <a class="" href="#">Dostępy</a>
								</th>
								
                                <th>
                                    <a class="" href="#">&nbsp;</a>
                                </th>
                                
                            </tr>
                        </thead>
                        <tfoot>
                            <tr>
                                <tiles:insertDefinition name="tiles-sorter-tables">
                                <tiles:putAttribute name="name" type="string">name</tiles:putAttribute>
                                <tiles:putAttribute name="label" type="string">Nazwa</tiles:putAttribute>
                                <c:set var="sorter" value="${patternListForm.filter.sorter}" scope="request"/>
                                </tiles:insertDefinition>
                                
                                <tiles:insertDefinition name="tiles-sorter-tables">
                                <tiles:putAttribute name="name" type="string">version</tiles:putAttribute>
                                <tiles:putAttribute name="label" type="string">Wersja</tiles:putAttribute>
                                <c:set var="sorter" value="${patternListForm.filter.sorter}" scope="request"/>
                                </tiles:insertDefinition>
                                
                                <th>
                                    <a class="" href="#">Dostępy</a>
                                </th>
                                
                                <th>
                                    <a class="" href="#">&nbsp;</a>
                                </th> 
                                
                            </tr>
                        </tfoot> 


                        <tbody>
                        
                        <c:forEach var="pattern" items="${patterns}" varStatus="rstatus">
                        <c:set var="trclass" scope="page"><c:choose><c:when test="${rstatus.index mod 2 eq 0}">odd</c:when><c:otherwise>even</c:otherwise></c:choose></c:set>
                        <tr class="${trclass}">
                            <td>
                            <a href="report/pattern/details/${pattern.id}"><c:out value="${pattern.name}"/></a>
                            </td>
                            <td>
                            <c:out value="${pattern.version}"/>
                            </td>
                            <td>
                                ${pattern.accessesAsString}
                            </td>
                            <td>
                            <a href="report/execute/${pattern.id}" class="btn-glow success btn-finish"><i class="icon-cogs"></i>&nbsp;Generuj&nbsp;</a>
                            <sec:authorize access="hasAnyRole('Admin')"><a href="report/pattern/download/${pattern.id}" class="btn-glow primary btn-next"><i class="icon-download"></i>&nbsp;Pobierz</a></sec:authorize>
                            <sec:authorize access="hasAnyRole('Admin')"><a href="#" class="btn-glow inverse pattern-delete" data-url="report/pattern/delete/${pattern.id}"><i class="icon-remove"></i>&nbsp;Usuń</a></sec:authorize>
                            </td>                             
                        </tr>
                        </c:forEach>
                        
                        </tbody>
                        </table>
                    
                    <tiles:insertDefinition name="tiles-pager-tables">
                        <c:set var="pager" value="${patternListForm.filter.pager}" scope="request"/>
                    </tiles:insertDefinition>                            
                            
    </form:form>
</div>   
            
</tiles:putAttribute>
</tiles:insertDefinition>        