<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<tiles:insertDefinition name="tiles-default" flush="true">
<tiles:putAttribute name="id" type="string">dbs-page-profile-profile<c:if test="${profile.id eq sprofile.id}">-current</c:if></tiles:putAttribute>
<tiles:putAttribute name="title" type="string">profil użytkownika</tiles:putAttribute>
<tiles:putAttribute name="css" type="string">
<link rel="stylesheet" href="css/compiled/user-profile.css" type="text/css" media="screen" />
<link rel="stylesheet" href="css/dbs/dbs-profile.css" type="text/css" media="screen" />
<link rel="stylesheet" href="css/dbs/dbs-wizard.css" type="text/css" media="screen" />
</tiles:putAttribute>
<tiles:putAttribute name="js" type="string">
<script src="js/dbs/dbs-profile.js"></script>
</tiles:putAttribute> 
<tiles:putAttribute name="content" type="string">

        <div id="pad-wrapper" class="user-profile">
            <!-- header -->
            <div class="row header">
                <div class="col-md-12">
                	<div class="">
                   	<c:if test="${!empty profile.photo}">
						<img src="profile/photo/${profile.photo.id}" class="img-circle avatar" />
                   	</c:if>
					<c:if test="${empty profile.photo}">
                    	<img src="img/no-img-personal.png" class="img-circle avatar" />
                    </c:if>
                    </div>
                    <div class="col-md-5">
                    
                    <h3 class="name">
                    <c:choose>
                    <c:when test="${profile.global}">${profile.description}</c:when>
                    <c:otherwise>${profile.name}</c:otherwise>
                    </c:choose>
                    </h3>
                    
                    <br/>
                    <span class="area">
                    <c:out value="${profile.login}"/>
                    </span>                    
                    
                    <c:if test="${profile.accepted ne true}">
                    <br/>
                    <span class="area inactive">profil nieaktywny</span>
                    </c:if>                
                    
                    <c:if test="${!empty profile.authoritiesAsString}">
                    <br/>
                    <span class="area">
                    <c:out value="${profile.authoritiesAsString}"/>
                    </span>
                    </c:if>
                    
                    <c:if test="${!empty profile.accessesAsString}">
                    <br/>
                    <span class="area">
                    <c:out value="${profile.accessesAsString}"/>
                    </span>
                    </c:if>
                    
                    <c:if test="${profile.global}">
                    <br/>
                    <span class="area">
                    Profil HR<c:if test="${!empty profile.clientAuthoritiesAsString}">: <c:out value="${profile.clientAuthoritiesAsString}"/></c:if>
                    </span>
                    
                    </c:if>
                    </div>
                </div>
                
                
                <div class="col-md-5 pull-right">
                	<sec:authorize access="hasAnyRole('Admin,Management')">
                	<c:if test="${profile.id ne sprofile.id}">
	                	<c:if test="${profile.accepted ne true}">
	                	<a class="btn-flat icon pull-right accept" href="profile/accept/${profile.id}" data-toggle="tooltip" title="Aktywuj profil" data-placement="top"><i class="icon-asterisk"></i></a>
	                	</c:if>
	                	<c:if test="${profile.accepted eq true}">
	                	<a class="btn-flat icon pull-right unaccept" href="profile/unaccept/${profile.id}" data-toggle="tooltip" title="Dezaktywuj profil" data-placement="top"><i class="icon-ban-circle"></i></a>
	                	</c:if>
	                	<a class="btn-flat icon pull-right delete-user" href="profile/delete/${profile.id}" data-toggle="tooltip" title="Usuń profil" data-placement="top"><i class="icon-trash"></i></a>
                	</c:if>
                	
	                <a class="btn-flat icon large pull-right edit" href="profile/edit/${profile.id}" data-toggle="tooltip" title="Edytuj profil">Edytuj ten profil</a>
	                </sec:authorize>
                </div>
                
             </div>

            <div class="row profile">
                <!-- bio, new note & orders column -->
                <div class="col-md-9 bio">
                    <div class="profile-box">
                    
                        <!-- new comment form -->
                        <div class="col-md-12 section comment">
                            <h6>Krótka notatka do profilu</h6>
                            <p>
                            <c:choose>
                            <c:when test="${not empty profile.note}">Notatkę napisał: ${profile.note.editor.name}</c:when>
                            <c:otherwise>Dodaj krótką notatkę by o czymś nie zapomnieć.</c:otherwise>
                            </c:choose>
                            </p>
                            
                            <form:form method="post" modelAttribute="profileForm" action="profile/note" class="">
	                    	<spring:bind path="note">
	                    	<c:set var="classes"><c:choose><c:when test="${status.error}">error</c:when></c:choose></c:set>
                            <div class="field-box ${classes}">
							        <form:textarea path="note" cssClass="form-control" rows="4" cols="20" placeholder="Notatka"/>
	                                <c:if test="${status.error}"><span class="alert-msg"><i class="icon-remove-sign"></i> <c:out value="${status.errorMessage}" escapeXml="false"/></span></c:if>
	                        </div>                           
                            </spring:bind>                            
                            
                            <div class="col-md-12 submit-box pull-right">
                                <input type="submit" class="btn-glow primary" value="Dodaj notatkę">
                            </div>
                            </form:form>
                            
                        </div>                    

						<c:if test="${!empty reports}">
                        <h3>Niezarchiwizowane raporty (${fn:length(reports)}/${maxtemp})</h3>
			            <div class="row">
			                <div class="col-md-11">
			                    <table class="table table-hover">
			                        <thead>
			                            <tr>
			                                <th class="col-md-3">Nazwa pliku
			                                </th>
			                                <th class="col-md-2">Data wygenerowania
			                                    <span class="line"></span>
			                                </th>
			                                <th class="col-md-2">
			                                    <span class="line"></span>Definicja
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
			                            <td class="align-right">
				                            <ul class="actions">
				                                <li class="last"><a href="report/archives/archive/${report.id}"><i class="tool" title="Przenieś do archiwum"></i>&nbsp;</a></li>
				                                <li class="last"><a href="#" class="report-delete" data-url="profile/report/archives/delete/${report.id}"><i class="table-delete" title="Usuń"></i>&nbsp;</a></li>
				                            </ul>
			                            </td>			                            
			                        </tr>
			                        </c:forEach>
			                        </tbody>
			                    </table>
			                </div>                
			            </div>
			            </c:if>


                    </div>
                </div>

                <!-- side address column -->
                <div class="col-md-3 col-xs-12 address pull-right">
                    <h6>Adres</h6>
                    <!-- iframe width="300" height="133" frameborder="0" scrolling="no" marginheight="0" marginwidth="0" src="https://maps.google.com.mx/?ie=UTF8&amp;t=m&amp;ll=19.715081,-155.071421&amp;spn=0.010746,0.025749&amp;z=14&amp;output=embed"></iframe-->
                    <ul>
                        <li>2301 East Lamar Blvd. Suite 140. </li>
                        <li>City, Arlington. United States,</li>
                        <li>Zip Code, TX 76006.</li>
                        <li class="ico-li">
                            <i class="ico-phone"></i>
                            1817 274 2933
                        </li>
                         <li class="ico-li">
                            <i class="ico-mail"></i>
                            <a href="#">alejandra@detail.com</a>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
        
</tiles:putAttribute>
</tiles:insertDefinition>        