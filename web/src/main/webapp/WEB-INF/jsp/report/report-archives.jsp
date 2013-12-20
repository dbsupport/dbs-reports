<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<tiles:insertDefinition name="tiles-default" flush="true">
<tiles:putAttribute name="id" type="string">dbs-page-report-archives</tiles:putAttribute>
<tiles:putAttribute name="title" type="string">lista raportów archiwalnych</tiles:putAttribute>
<tiles:putAttribute name="css" type="string">
<link rel="stylesheet" href="css/compiled/user-list.css" type="text/css" media="screen" />
<link rel="stylesheet" href="css/dbs/dbs-profile-list.css" type="text/css" media="screen" />
</tiles:putAttribute>
<tiles:putAttribute name="content" type="string">

        <div id="pad-wrapper" class="users-list">
            <div class="row header">
                <h3>Raporty archiwalne</h3>
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
                                        <option>Typ</option>
                                        <option>Data wygenerowania</option>
                                        <option>Właściciel</option>
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

            <!-- Users table -->
            <div class="row">
                <div class="col-md-12">
                    <table class="table table-hover">
                        <thead>
                            <tr>
                                <th class="col-md-3 sortable">
                                    Nazwa pliku
                                </th>
                                <th class="col-md-3 sortable">
                                    <span class="line"></span>Definicja
                                </th>
                                <th class="col-md-2 sortable">
                                    <span class="line"></span>Data wygenerowania
                                </th>
                                <th class="col-md-4 sortable">
                                    <span class="line"></span>Wygenerował
                                </th>
                                <th class="col-md-3 sortable align-right">
                                    <span class="line"></span>
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
                                <a href="report/pattern/details/${report.pattern.id}">${report.pattern.name} ${report.pattern.version}</a>
                            </td>
                            <td>
                            	<fmt:formatDate value="${report.generationDate}" type="both" pattern="dd-MM-yyyy HH:mm:ss" />
                            </td>
                            <td class="align-left">
                                <img src="img/contact-img.png" class="img-circle avatar hidden-phone" />
                                <a href="profile/" class="name">${report.creator.name}</a>
                                <span class="subtext">...</span>
                            </td>
                            <td class="align-right">
	                            <ul class="actions">
	                                <li><a href="report/view/${report.id}"><i class="table-edit" title="Podgląd"></i></a></li>
	                                <li><a href="report/pattern/details/${pattern.id}"><i class="table-settings" title="Wyślij email'em"></i></a></li>
	                                <li class="last"><a href="report/delete/${report.id}"><i class="table-delete" title="Usuń raport"></i></a></li>
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
            <!-- end users table -->
        </div>
        
</tiles:putAttribute>
</tiles:insertDefinition>        