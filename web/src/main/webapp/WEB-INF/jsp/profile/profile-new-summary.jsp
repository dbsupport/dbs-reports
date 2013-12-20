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
                
                    <div id="fuelux-wizard" class="wizard row">
                        <ul class="wizard-steps">
                            <li data-target="#step1">
                                <span class="step">1</span>
                                <span class="title">Wprowadź dane profilu</span>
                            </li>
                            <li data-target="#step2" class="active">
                                <span class="step">2</span>
                                <span class="title">Zapisz profil</span>
                            </li>
                        </ul>                            
                    </div>                 
                
                    <div class="step-content">
                    <div class="step-pane active" id="step2">
                    <div class="row form-wrapper">
                    <div class="col-md-12">                   
                
                    	<form:form method="post" modelAttribute="profileNewForm" action="profile/new/summary" class="new_user_form" enctype="multipart/form-data">
							<input type="hidden" name="page" value="2">
                            <div class="col-md-12 field-box">
                                <label>Login:</label>
                                <input class="form-control inline-input" type="text" readonly="readonly" value="${profileNewForm.login}"/>
                            </div>
                            <div class="col-md-12 field-box">
                                <label>Imię:</label>
                                <input class="form-control inline-input" type="text" readonly="readonly" value="${profileNewForm.firstName}"/>
                            </div>
                            <div class="col-md-12 field-box">
                                <label>Nazwisko:</label>
                                <input class="form-control inline-input" type="text" readonly="readonly" value="${profileNewForm.lastName}"/>
                            </div>
                            <div class="col-md-12 field-box">
                                <label>Email:</label>
                                <input class="form-control inline-input" type="text" readonly="readonly" value="${profileNewForm.email}"/>
                            </div>
                            <div class="col-md-12 field-box">
                                <label>Telefon:</label>
                                <input class="form-control inline-input" type="text" readonly="readonly" value="${profileNewForm.phone}"/>
                            </div>
                            <div class="col-md-12 field-box">
                                <label>Adres:</label>
                                <div class="address-fields">
                                	<input class="form-control inline-input" type="text" readonly="readonly" value="${profileNewForm.street}"/>
                                    <input class="small form-control inline-input" type="text" readonly="readonly" value="${profileNewForm.city}"/>
                                    <input class="small form-control inline-input" type="text" readonly="readonly" value="${profileNewForm.state}"/>
                                    <input class="small form-control inline-input" type="text" readonly="readonly" value="${profileNewForm.zipCode}"/>
                                </div>
                            </div>
                            <div class="col-md-12 field-box textarea personal-image">
                            
                            </div>
                            <div class="col-md-11 field-box actions">
		                        <button type="button" class="btn-glow primary btn-prev" onclick="location.href='profile/new/form'"><i class="icon-chevron-left"></i>&nbsp;Popraw</button>
		                        <button type="submit" class="btn-glow success btn-finish">Zapisz profil!</button>                            
                            </div>

                        </form:form>
                    </div>
                    </div>
                    </div>
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