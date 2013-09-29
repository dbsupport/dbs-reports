<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<tiles:insertDefinition name="tiles-default" flush="true">
<tiles:putAttribute name="id" type="string">dbs-page-profile-list</tiles:putAttribute>
<tiles:putAttribute name="title" type="string">lista profili użytkowników</tiles:putAttribute>
<tiles:putAttribute name="css" type="string">
<link rel="stylesheet" href="css/compiled/user-list.css" type="text/css" media="screen" />
<link rel="stylesheet" href="css/dbs/dbs-profile-list.css" type="text/css" media="screen" />
</tiles:putAttribute>
<tiles:putAttribute name="content" type="string">

        <div id="pad-wrapper" class="users-list">
            <div class="row header">
                <h3>Profile użytkowników</h3>
                <div class="col-md-10 col-sm-12 col-xs-12 pull-right">
                    <input type="text" class="col-md-5 search" placeholder="Wpisz użytkownika...">
                    
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
                                        <option>Imię/Nazwisko</option>
                                        <option>Email</option>
                                        <option>Data założenia</option>
                                        <option>Ostatnie logowanie</option>
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

                    <a href="profile/new" class="btn-flat success pull-right">
                        <span>&#43;</span>
                        NOWY PROFIL
                    </a>
                </div>
            </div>

            <!-- Users table -->
            <div class="row">
                <div class="col-md-12">
                    <table class="table table-hover">
                        <thead>
                            <tr>
                                <th class="col-md-1">
                                    &nbsp;
                                </th>                            
                                <th class="col-md-4 sortable">
                                    Imię/Nazwisko
                                </th>
                                <th class="col-md-3 sortable">
                                    <span class="line"></span>Data założenia
                                </th>
                                <th class="col-md-2 sortable">
                                    <span class="line"></span>Ilość raportów
                                </th>
                                <th class="col-md-3 sortable">
                                    <span class="line"></span>Email
                                </th>
                                <th class="col-md-3 align-right">
                                    <span class="line"></span>&nbsp;
                                </th>                                
                            </tr>
                        </thead>
                        <tbody>
                        <!-- row -->
                        <tr class="first">
                            <td>
                                <input type="checkbox">
                             </td>
                             <td>
                                <img src="img/contact-img.png" class="img-circle avatar hidden-phone" />
                                <a href="profile/profile/1" class="name">Alejandra Galvan Castillo</a>
                                <span class="subtext">Graphic Design</span>
                            </td>
                            <td>
                                Mar 13, 2012
                            </td>
                            <td>
                                 4,500.00
                            </td>
                            <td>
                                <a href="profile">alejandra@canvas.com</a>
                            </td>
                            <td>
                                    <ul class="actions">
                                        <li><a href="profile/edit/1"><i class="table-edit" title="Edycja profilu"></i></a></li>
                                        <li><i class="table-settings" title=""></i></li>
                                        <li class="last"><i class="table-delete" title="Usunięcie profilu"></i></li>
                                    </ul>
                             </td>                            
                        </tr>
                        <!-- row -->
                        <tr>
                            <td>
                                <input type="checkbox">
                             </td>                        
                            <td>
                                <img src="img/contact-img2.png" class="img-circle avatar hidden-phone" />
                                <a href="user-profile.html" class="name">Alejandra Galvan Castillo</a>
                                <span class="subtext">Graphic Design</span>
                            </td>
                            <td>
                                Jun 03, 2012
                            </td>
                            <td>
                                 549.99
                            </td>
                            <td>
                                <a href="#">alejandra@canvas.com</a>
                            </td>
                            <td>
                                    <ul class="actions">
                                        <li><i class="table-edit"></i></li>
                                        <li><i class="table-settings"></i></li>
                                        <li class="last"><i class="table-delete"></i></li>
                                    </ul>
                             </td>                              
                        </tr>
                        <!-- row -->
                        <tr>
                            <td>
                                <input type="checkbox">
                             </td>                        
                            <td>
                                <img src="img/contact-img.png" class="img-circle avatar hidden-phone" />
                                <a href="user-profile.html" class="name">Alejandra Galvan Castillo</a>
                                <span class="subtext">Graphic Design</span>
                            </td>
                            <td>
                                Mar 01, 2013
                            </td>
                            <td>
                                 30.00
                            </td>
                            <td>
                                <a href="#">alejandra@canvas.com</a>
                            </td>
                            <td>
                                    <ul class="actions">
                                        <li><i class="table-edit"></i></li>
                                        <li><i class="table-settings"></i></li>
                                        <li class="last"><i class="table-delete"></i></li>
                                    </ul>
                             </td>                              
                        </tr>
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