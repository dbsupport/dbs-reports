<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<tiles:insertDefinition name="tiles-wizard" flush="true">
<tiles:putAttribute name="id" type="string">dbs-page-profile-new</tiles:putAttribute>
<tiles:putAttribute name="title" type="string">Nowy profil</tiles:putAttribute>
<tiles:putAttribute name="steps" type="string">Wprowadź personalia;Nadaj uprawnienia;Zapisz</tiles:putAttribute>
<tiles:putAttribute name="step" type="string">1</tiles:putAttribute>
<tiles:putAttribute name="css" type="string">
<link rel="stylesheet" href="css/compiled/new-user.css" type="text/css" media="screen" />
<link rel="stylesheet" href="css/compiled/form-wizard.css" type="text/css" media="screen" />
<link rel="stylesheet" href="css/dbs/dbs-wizard.css" type="text/css" media="screen" />
<link rel="stylesheet" href="css/dbs/dbs-profile.css" type="text/css" media="screen" />

<link rel="stylesheet" href="css/dbs/dbs-profile-new.css" type="text/css" media="screen" />
</tiles:putAttribute>
<tiles:putAttribute name="js" type="string">
<script src="js/ui/jquery.ui.widget.js"></script>
<script src="js/jquery.iframe-transport.js"></script>
<script src="js/jquery.fileupload.js"></script>
<script src="js/dbs/dbs-profile-new-personal.js"></script>
</tiles:putAttribute>
        
<tiles:putAttribute name="content" type="string">

                   	<form:form method="post" modelAttribute="profileNewForm" action="profile/new/personal" class="" enctype="multipart/form-data">
						<input type="hidden" name="page" value="1"/>
						
                   		<spring:bind path="login">
                   		<c:set var="classes"><c:choose><c:when test="${status.error}">error</c:when></c:choose></c:set>
                        <div class="field-box ${classes}">
	                        <label>Login</label>
                                <form:input path="login" cssClass="form-control" placeholder="Login"/>
                                <c:if test="${status.error}"><span class="alert-msg"><i class="icon-remove-sign"></i> <c:out value="${status.errorMessage}" escapeXml="false"/></span></c:if>
						</div>
                        </spring:bind>
						
                   		<spring:bind path="passwd">
                   		<c:set var="classes"><c:choose><c:when test="${status.error}">error</c:when></c:choose></c:set>
                        <div class="field-box ${classes}">
	                        <label>Hasło</label>
                                <input name="passwd" class="form-control" type="password" placeholder="Hasło"/>
                                <c:if test="${status.error}"><span class="alert-msg"><i class="icon-remove-sign"></i> <c:out value="${status.errorMessage}" escapeXml="false"/></span></c:if>
                        </div>
                        </spring:bind>

                    	
                   		<spring:bind path="firstName">
                   		<c:set var="classes"><c:choose><c:when test="${status.error}">error</c:when></c:choose></c:set>
                        <div class="field-box ${classes}">
                        <label>Imię</label>
                                <form:input path="firstName" cssClass="form-control" placeholder="Imię"/>
                                <c:if test="${status.error}"><span class="alert-msg"><i class="icon-remove-sign"></i> <c:out value="${status.errorMessage}" escapeXml="false"/></span></c:if>
                        </div>
                        </spring:bind>
	                            
                   		<spring:bind path="lastName">
                   		<c:set var="classes"><c:choose><c:when test="${status.error}">error</c:when></c:choose></c:set>
                        <div class="field-box ${classes}">
                            <label>Nazwisko</label>
                                <form:input path="lastName" cssClass="form-control" placeholder="Nazwisko"/>
                                <c:if test="${status.error}"><span class="alert-msg"><i class="icon-remove-sign"></i> <c:out value="${status.errorMessage}" escapeXml="false"/></span></c:if>
                        </div>
                        </spring:bind>
                           
                    	<spring:bind path="email">
                        <c:set var="classes"><c:choose><c:when test="${status.error}">error</c:when></c:choose></c:set>
                        <div class="field-box ${classes}">
	                        <label>Email</label>
                                <form:input path="email" cssClass="form-control" placeholder="Email"/>
                                <c:if test="${status.error}"><span class="alert-msg"><i class="icon-remove-sign"></i> <c:out value="${status.errorMessage}" escapeXml="false"/></span></c:if>
                        </div>
                        </spring:bind>        
                           
                		<spring:bind path="phone">
                		<c:set var="classes"><c:choose><c:when test="${status.error}">error</c:when></c:choose></c:set>
                        <div class="field-box ${classes}">
                     		<label>Telefon</label>
                            <form:input path="phone" cssClass="form-control" placeholder="Telefon"/>
                            <c:if test="${status.error}"><span class="alert-msg"><i class="icon-remove-sign"></i> <c:out value="${status.errorMessage}" escapeXml="false"/></span></c:if>
                        </div>
                        </spring:bind>

						<div class="field-box address-fields">
                            <label>Adres</label>
                            
                    		<spring:bind path="street">
                    		<c:set var="classes"><c:choose><c:when test="${status.error}">error</c:when></c:choose></c:set>
                    		<div class="field-box ${classes}">
                                <form:input path="street" cssClass="form-control" placeholder="Ulica"/>
                                <c:if test="${status.error}"><span class="alert-msg"><i class="icon-remove-sign"></i> <c:out value="${status.errorMessage}" escapeXml="false"/></span></c:if>
                            </div>
                            </spring:bind>
                                 
                    		<spring:bind path="city">
                    		<c:set var="classes"><c:choose><c:when test="${status.error}">error</c:when></c:choose></c:set>
                    		<div class="field-box ${classes}">
                                <form:input path="city" cssClass="small form-control" placeholder="Miasto"/>
                                <c:if test="${status.error}"><span class="alert-msg"><i class="icon-remove-sign"></i> <c:out value="${status.errorMessage}" escapeXml="false"/></span></c:if>
                            </div>
                            </spring:bind>
							</div>
							
						<div class="field-box address-fields">        
							<label>&nbsp;</label>
							                    		  
                    		<spring:bind path="state">
                    		<c:set var="classes"><c:choose><c:when test="${status.error}">error</c:when></c:choose></c:set>
                    		<div class="field-box ${classes}">
                                <form:input path="state" cssClass="small form-control" placeholder="Województwo"/>
                                <c:if test="${status.error}"><span class="alert-msg"><i class="icon-remove-sign"></i> <c:out value="${status.errorMessage}" escapeXml="false"/></span></c:if>
                            </div>
                            </spring:bind>		                            
                    		<spring:bind path="zipCode">
                    		<c:set var="classes"><c:choose><c:when test="${status.error}">error</c:when></c:choose></c:set>
                    		<div class="field-box ${classes}">
                                <form:input path="zipCode" cssClass="small form-control" placeholder="Kod pocztowy"/>
                                <c:if test="${status.error}"><span class="alert-msg"><i class="icon-remove-sign"></i> <c:out value="${status.errorMessage}" escapeXml="false"/></span></c:if>
                            </div>
                            </spring:bind>	
                      	</div>	                                                         
                           
	                	<div class="field-box" id="photo">
                        	<label>Zdjęcie</label>
					  		<input id="fileupload" type="file" name="files[]" data-url="profile/new/photo/upload" class="small form-control" accept=".jpg,.jpeg,.gif,.png"/>
						  		
                           	<div class="photo-img">
	                		<c:if test="${profileNewForm.photo}">
	                			<img src="profile/new/photo" class="avatar img-circle"/>
							</c:if>
	                		</div>
							<span class="alert-msg">
							<c:if test="${profileNewForm.photo}"><a href="profile/new/photo/delete" class="close-icon"><i class="icon-remove-sign"></i></a></c:if>
							</span>
	                    </div>
                           
                           <div class="wizard-actions">
                           	<button type="button" class="btn-glow" onclick="location.href='profile/new'"><i class="icon-refresh"></i>&nbsp;Wyczyść</button><span>&nbsp;</span>
	                        <button type="submit" class="btn-glow primary btn-next" data-last="Finish">Dalej&nbsp;&nbsp;<i class="icon-chevron-right"></i></button><span>&nbsp;</span>
                           </div>
                       </form:form>
        
</tiles:putAttribute>
</tiles:insertDefinition>        