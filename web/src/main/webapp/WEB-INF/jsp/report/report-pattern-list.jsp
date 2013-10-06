<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<tiles:insertDefinition name="tiles-default" flush="true">
<tiles:putAttribute name="id" type="string">dbs-page-report-pattern-list</tiles:putAttribute>
<tiles:putAttribute name="title" type="string">lista dostępnych definicji raportów</tiles:putAttribute>
<tiles:putAttribute name="css" type="string">
<link rel="stylesheet" href="css/compiled/user-list.css" type="text/css" media="screen" />
<link rel="stylesheet" href="css/dbs/dbs-profile-list.css" type="text/css" media="screen" />
</tiles:putAttribute>
<tiles:putAttribute name="content" type="string">

        <div id="pad-wrapper" class="users-list">
            <div class="row header">
                <h3>Dostępne definicje raportów</h3>
                
                <div class="col-md-10 col-sm-12 col-xs-12 pull-right">
                    <input type="text" class="col-md-5 search" placeholder="Wpisz nazwę raportu...">
                    
                    <!-- custom popup filter -->
                    <!-- styles are located in css/elements.css -->
                    <!-- script that enables this dropdown is located in js/theme.js -->
                    <div class="ui-dropdown">
                        <div class="head" data-toggle="tooltip" title="Kliknij mnie!">
                            Wyfiltruj
                            <i class="arrow-down"></i>
                        </div>  
                        <div class="dialog">
                            <div class="pointer">
                                <div class="arrow"></div>
                                <div class="arrow_border"></div>
                            </div>
                            <div class="body">
                                <p class="title">
                                    Pokaż wg:
                                </p>
                                <div class="form">
                                    <select>
                                        <option>Nazwa</option>
                                        <option>Wersja</option>
                                        <option>Data importu</option>
                                        <option>Autor</option>
                                    </select>
                                    <select>
                                        <option>równa się</option>
                                        <option>nie równa się</option>
                                        <option>większe niż</option>
                                        <option>zaczyna się</option>
                                        <option>zawiera</option>
                                    </select>
                                    <input type="text" class="form-control" />
                                    <a class="btn-flat small">Dodaj filtr</a>
                                </div>
                            </div>
                        </div>
                    </div>

                    <a href="report/pattern/import" class="btn-flat success pull-right">
                        <span>&#43;</span>
                        ZAIMPORTUJ DEFINICJĘ RAPORTU
                    </a>
                </div>
            </div>

			<c:if test="${!empty patterns}">
            <!-- Patterns table -->
            <div class="row">
                <div class="col-md-12">
                    <table class="table table-hover">
                        <thead>
                            <tr>
                                <th class="col-md-3 sortable">
                                Nazwa
                                </th>
                                <th class="col-md-1 sortable">
                                    <span class="line"></span>Wersja
                                </th>
                                <th class="col-md-2 sortable">
                                    <span class="line"></span>Data importu
                                </th>
                                <th class="col-md-2 sortable">
                                    <span class="line"></span>Autor
                                </th>
                                <th class="col-md-2 sortable">
                                    <span class="line"></span>Role
                                </th>
                                <th class="col-md-1 sortable align-right">
                                    <span class="line"></span>
                                </th>                                
                            </tr>
                        </thead>
                        
                        <tbody>
                        <c:forEach var="pattern" items="${patterns}" varStatus="status">
                        <!-- row -->
                        <tr class="first">
                            <td>
                                <a href="#"><c:out value="${pattern.name}"/></a>
                            </td>
                            <td>
                                <c:out value="${pattern.version}"/>
                            </td>
                            <td>
                            	<fmt:formatDate value="${pattern.uploadDate}" type="both" pattern="dd-MM-yyyy HH:mm:ss" />
                            </td>
                            <td>
                                <c:out value="${pattern.author}"/>
                            </td>                            
                            <td>
                            	<c:out value="${pattern.roles}"/>
                            </td>
                            <td class="align-right">
	                            <ul class="actions">
	                                <li><a href="report/pattern/execute/${pattern.id}"><i class="table-edit" title="Generowanie raportu"></i></a></li>
	                                <li><a href="report/pattern/details/${pattern.id}"><i class="table-settings" title=""></i></a></li>
	                                <li class="last"><a href="report/pattern/delete/${pattern.id}"><i class="table-delete" title="Usunięcie definicji"></i></a></li>
	                            </ul>
                            </td>                            
                        </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>                
            </div>
            <ul class="pagination pull-right">
                <li><a href="#">&#8249;</a></li>
                <li class="active"><a href="#">1</a></li>
                <li><a href="#">2</a></li>
                <li><a href="#">3</a></li>
                <li><a href="#">4</a></li>
                <li><a href="#">5</a></li>
                <li><a href="#">&#8250;</a></li>
            </ul>
            </c:if>
            
        </div>
        
</tiles:putAttribute>
</tiles:insertDefinition>        