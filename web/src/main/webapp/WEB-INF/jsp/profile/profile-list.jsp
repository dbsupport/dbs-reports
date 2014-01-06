<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<tiles:insertDefinition name="tiles-browser" flush="true">
<tiles:putAttribute name="id" type="string">dbs-page-profile-list</tiles:putAttribute>
<tiles:putAttribute name="title" type="string">Lista profili użytkowników</tiles:putAttribute>
<tiles:putAttribute name="css" type="string">
<link rel="stylesheet" href="css/compiled/user-list.css" type="text/css" media="screen" />
<link rel="stylesheet" href="css/dbs/dbs-browser.css" type="text/css" media="screen" />
</tiles:putAttribute>
<tiles:putAttribute name="js" type="string">
<script src="js/dbs/dbs-profile-list.js"></script>
</tiles:putAttribute>

<tiles:putAttribute name="form" type="string">
                <form:form method="post" modelAttribute="profileListForm" action="profile/list" class="">
               		<form:input path="name" cssClass="col-md-5 search" placeholder="Wyszukaj..." onblur="this.form.submit();"/>
                </form:form>
                
                <div class="col-md-5 pull-right">
                    <a href="profile/new" class="btn-flat success pull-right"><span>&#43;</span>DODAJ NOWY PROFIL</a>
				</div>
</tiles:putAttribute>

<tiles:putAttribute name="content" type="string">

                    <table class="table table-hover">
                        <thead>
                            <tr>
                                <th class="col-md-3 sortable">
                                <tiles:insertDefinition name="tiles-sorter">
                                	<tiles:putAttribute name="name" type="string">lastname</tiles:putAttribute>
                                	<tiles:putAttribute name="label" type="string">Imię/Nazwisko</tiles:putAttribute>
            					</tiles:insertDefinition>                                  
                                </th>
                                <th class="col-md-2 sortable"><span class="line"></span>
                                <tiles:insertDefinition name="tiles-sorter">
                                	<tiles:putAttribute name="name" type="string">login</tiles:putAttribute>
                                	<tiles:putAttribute name="label" type="string">Login</tiles:putAttribute>
            					</tiles:insertDefinition>                                  
                                </th>
                                <th class="col-md-2 sortable"><span class="line"></span>
                                <tiles:insertDefinition name="tiles-sorter">
                                	<tiles:putAttribute name="name" type="string">email</tiles:putAttribute>
                                	<tiles:putAttribute name="label" type="string">Email</tiles:putAttribute>
            					</tiles:insertDefinition>                                  
                                </th>
                                <%-- th class="col-md-2 sortable"><span class="line"></span>
                                    Telefon
                                </th>
                                <th class="col-md-2"><span class="line"></span>
                                    Adres
                                </th>
                                <th class="align-right">
                                    <span class="line"></span>&nbsp;
                                </th--%>                                
                            </tr>
                        </thead>
                        <tbody>

						<c:forEach items="${profiles}" var="profile" varStatus="rstatus">                        
                        <!-- row -->
                        <tr <c:if test="${rstatus.first}">class="first"</c:if>>
                       
                             <td>
                                <c:if test="${profile.accepted ne true}">
			                    <div class="subtext">profil nieaktywny</div>
			                    </c:if>
                             
                             	<div class="">
                             	<c:if test="${!empty profile.photo}">
                                <img src="profile/photo/${profile.photo.id}" class="img-circle avatar" />
                             	</c:if>
                             	<c:if test="${empty profile.photo}">
                             	<img src="img/no-img-personal.png" class="img-circle avatar" />
                             	</c:if>
                             	<c:set var="nameclass">name<c:choose><c:when test="${profile.accepted ne true}"> inactive</c:when></c:choose></c:set>
                             	<a href="profile/${profile.id}" class="${nameclass}">
				                    <c:choose>
				                    <c:when test="${profile.global}">${profile.description}</c:when>
				                    <c:otherwise>${profile.name}</c:otherwise>
				                    </c:choose>
			                    </a>
                                </div>
                                
                                <c:if test="${!empty profile.authoritiesAsString}">
			                    <div class="subtext"><c:out value="${profile.authoritiesAsString}"/></div>
			                    </c:if>
			                    
			                    <c:if test="${!empty profile.accessesAsString}">
			                    <div class="subtext"><c:out value="${profile.accessesAsString}"/></div>                                
			                    </c:if>
			                    
			                    <c:if test="${profile.global}">
			                    <div class="subtext">Profil HR</div>
			                    </c:if>
			                    
                            </td>
                            <td>
                               ${profile.login}
                            </td>                             
                            <td>
                                 ${profile.email}
                            </td>
                            <%-- td>
                                ${profile.phone}
                            </td>
                            <td>
                            	<c:if test="${!empty profile.address}">
                                ${profile.address.street}<span>&nbsp;</span>
                                ${profile.address.zipcode}<span>&nbsp;</span>
                                ${profile.address.city}<span>&nbsp;</span>
                                ${profile.address.state}<span>&nbsp;</span>
                                </c:if>
                            </td--%>
                            
                            
                            <td>
                                    <ul class="actions">
                                        <li><a href="profile/edit/${profile.id}"><i class="table-edit" title="Edycja profilu"></i></a></li>
                                        <li class="last"><a href="#" class="profile-delete" data-url="profile/delete/${profile.id}"><i class="table-delete" title="Usunięcie profilu"></i></a></li>
                                    </ul>
                             </td>                            
                        </tr>
                        </c:forEach>
                        
                        </tbody>
                    </table>
        
                    <tiles:insertDefinition name="tiles-pager">
            			<c:set var="pager" value="${profileListForm.filter.pager}" scope="request"/>
            		</tiles:insertDefinition>  
        
</tiles:putAttribute>
</tiles:insertDefinition>        