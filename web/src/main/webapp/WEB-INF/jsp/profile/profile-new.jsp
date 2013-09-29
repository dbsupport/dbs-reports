<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<tiles:insertDefinition name="tiles-default" flush="true">
<tiles:putAttribute name="id" type="string">dbs-page-profile-new</tiles:putAttribute>
<tiles:putAttribute name="title" type="string">nowy profil</tiles:putAttribute>
<tiles:putAttribute name="css" type="string">
<link rel="stylesheet" href="css/compiled/new-user.css" type="text/css" media="screen" />
<link rel="stylesheet" href="css/compiled/form-wizard.css" type="text/css" media="screen" />
</tiles:putAttribute>
<tiles:putAttribute name="content" type="string">

        <div id="pad-wrapper" class="new-user">
            <div class="row header">
                <div class="col-md-12">
                    <h3>Dodaj profil użytkownika</h3>
                </div>                
            </div>
            
            <div class="row form-wrapper">
                <!-- left column -->
                <div class="col-md-9 with-sidebar">
                    <div class="container">
                    	<form:form method="post" commandName="profileNewForm" action="profile/new" class="new_user_form" enctype="multipart/form-data">
                    	
                    		<spring:bind path="firstName">
                    		<c:if test="${status.error}"><c:set var="classes">error</c:set></c:if>
                            <div class="col-md-12 field-box ${classes}">
                                <label>Imię:</label>
                                <form:input path="firstName" cssClass="form-control" />
                                <c:if test="${status.error}"><span class="alert-msg"><i class="icon-remove-sign"></i> <c:out value="${status.errorMessage}" escapeXml="false"/></span></c:if>
                            </div>
                            </spring:bind>
                            <div class="col-md-12 field-box">
                                <label>Nazwisko:</label>
                                <input class="form-control" type="text" />
                            </div>
                            <div class="col-md-12 field-box">
                                <label>Email:</label>
                                <input class="col-md-9 form-control" type="text" />
                            </div>
                            <div class="col-md-12 field-box">
                                <label>Telefon:</label>
                                <input class="col-md-9 form-control" type="text" />
                            </div>
                            <div class="col-md-12 field-box">
                                <label>Adres:</label>
                                <div class="address-fields">
                                    <input class="form-control" type="text" placeholder="Ulica" />
                                    <input class="small form-control" type="text" placeholder="Miasto" />
                                    <input class="small form-control" type="text" placeholder="Województwo" />
                                    <input class="small last form-control" type="text" placeholder="Kod pocztowy" />
                                </div>
                            </div>
                            <div class="col-md-12 field-box textarea personal-image">
	                        <label>Zdjęcie:</label>
	                        <div class="address-fields">
	                        	<input type="file" />
	                        </div>
                            </div>
                            <div class="col-md-12 field-box textarea">
                                <label>Notatki:</label>
                                <textarea class="col-md-9"></textarea>
                                <span class="charactersleft">Krótka notatka o profilu.</span>
                            </div>
                            <div class="col-md-11 field-box actions">
                            	<button type="submit" name="submit" class="btn-glow primary">Utwórz profil</button>
                                <span>&nbsp;</span>
                                <input type="reset" value="Anuluj" class="reset"/>
                            </div>
                        </form:form>
                    </div>
                </div>

                <!-- side right column -->
                <div class="col-md-3 form-sidebar pull-right">
                    <h6>Sidebar text for instructions</h6>
                    <p>Add multiple users at once</p>
                    <p>Choose one of the following file types:</p>
                    <ul>
                        <li><a href="#">Upload a vCard file</a></li>
                        <li><a href="#">Import from a CSV file</a></li>
                        <li><a href="#">Import from an Excel file</a></li>
                    </ul>
                </div>
            </div>
        </div>
        
</tiles:putAttribute>
</tiles:insertDefinition>        