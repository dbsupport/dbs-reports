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
                                <th class="col-md-2 sortable"><span class="line"></span>
                                    Imię/Nazwisko
                                </th>
                                <th class="col-md-2 sortable">
                                    Login
                                </th>
                                <th class="col-md-2 sortable"><span class="line"></span>
                                    Email
                                </th>
                                <th class="col-md-2 sortable"><span class="line"></span>
                                    Telefon
                                </th>
                                <th class="col-md-2"><span class="line"></span>
                                    Adres
                                </th>
                                <th class="col-md-3 align-right">
                                    <span class="line"></span>&nbsp;
                                </th>                                
                            </tr>
                        </thead>
                        <tbody>

						<c:forEach items="${profiles}" var="profile" varStatus="rstatus">                        
                        <!-- row -->
                        <tr class="first">
                             <td>
                                <img src="img/contact-img.png" class="img-circle avatar hidden-phone" />
                                <a href="profile/${profile.id}" class="name">${profile.name}</a>
			                    <span class="subtext">
			                    <c:forEach var="authority" items="${profile.authorities}" varStatus="rstatus">
			                    <spring:message code="${authority.authority}" text="${authority.authority}"/>
			                    </c:forEach>
			                    
			                    <c:if test="${profile.global}">
			                    <br/>
			                    <c:forEach var="authority" items="${profile.hrauthorities}" varStatus="rstatus">
			                    <spring:message code="${authority.name}" text="${authority.name}"/>
			                    </c:forEach>
			                    </c:if>
			                    
			                    <br/>
			                    <c:forEach var="access" items="${profile.accesses}" varStatus="rstatus">
			                    <spring:message code="${access.name}" text="${access.name}"/>
			                    </c:forEach>
			                    
			                    </span>                                
                            </td>
                            <td>
                               ${profile.login}
                            </td>
                            <td>
                                 ${profile.email}
                            </td>
                            <td>
                                ${profile.phone}
                            </td>
                            <td>
                            	<c:if test="${!empty profile.address}">
                                <c:out value="${profile.address.street}"/><span>&nbsp;</span>
                                ${profile.address.city}<span>&nbsp;</span>
                                ${profile.address.state}<span>&nbsp;</span>
                                ${profile.address.zipcode}
                                </c:if>
                            </td>
                            
                            
                            <td>
                                    <ul class="actions">
                                        <li><a href="profile/edit/${profile.id}"><i class="table-edit" title="Edycja profilu"></i></a></li>
                                        <li><i class="table-settings" title=""></i></li>
                                        <li class="last"><i class="table-delete" title="Usunięcie profilu"></i></li>
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