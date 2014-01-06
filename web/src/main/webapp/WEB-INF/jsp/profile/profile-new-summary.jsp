<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<tiles:insertDefinition name="tiles-wizard" flush="true">
<tiles:putAttribute name="id" type="string">dbs-page-profile-new</tiles:putAttribute>
<tiles:putAttribute name="title" type="string">Nowy profil</tiles:putAttribute>
<tiles:putAttribute name="steps" type="string">Wprowadź personalia;Nadaj uprawnienia;Zapisz</tiles:putAttribute>
<tiles:putAttribute name="step" type="string">3</tiles:putAttribute>
<tiles:putAttribute name="css" type="string">
<link rel="stylesheet" href="css/compiled/ui-elements.css" type="text/css" media="screen" />
<link rel="stylesheet" href="css/compiled/new-user.css" type="text/css" media="screen" />
<link rel="stylesheet" href="css/compiled/form-wizard.css" type="text/css" media="screen" />
<link rel="stylesheet" href="css/dbs/dbs-wizard.css" type="text/css" media="screen" />
<link rel="stylesheet" href="css/dbs/dbs-profile.css" type="text/css" media="screen" />
</tiles:putAttribute>
<tiles:putAttribute name="js" type="string">
<script src="js/dbs/dbs-profile-new-summary.js"></script>
</tiles:putAttribute>
<tiles:putAttribute name="content" type="string">


<form:form method="post" modelAttribute="profileNewForm" action="profile/new/summary" class="" enctype="multipart/form-data">
	<input type="hidden" name="page" value="3">
	<c:if test="${profileNewForm.photo}">
		<div class="field-box">
       		<div class="col-md-1">
           		<img src="profile/new/photo" class="avatar img-circle">
           	</div>
       </div>
	</c:if>
    <div class="field-box">
        <label>Login</label>
        <input class="form-control inline-input" type="text" readonly="readonly" value="${profileNewForm.login}"/>
    </div>
    <div class="field-box">
        <label>Imię/Nazwisko</label>
        <input class="form-control inline-input" type="text" readonly="readonly" value="${profileNewForm.firstName} ${profileNewForm.lastName}"/>
    </div>
    <div class="field-box">
        <label>Email</label>
        <input class="form-control inline-input" type="text" readonly="readonly" value="${profileNewForm.email}"/>
    </div>
    <div class="field-box">
        <label>Telefon</label>
        <input class="form-control inline-input" type="text" readonly="readonly" value="${profileNewForm.phone}"/>
    </div>
    <div class="field-box">
        <label>Adres</label>
       	<input class="form-control inline-input" type="text" readonly="readonly" value="${profileNewForm.street} ${profileNewForm.zipCode} ${profileNewForm.city} ${profileNewForm.state}"/>
    </div>
    <div class="field-box">
        <label>Uprawnienia dostępu</label>
        <c:set var="authorities">
        <c:forEach items="${profileNewForm.authorities}" var="authority" varStatus="status"><spring:message code="${authority.name}" text="${authority.name}"></spring:message><c:if test="${!status.last}">, </c:if></c:forEach>
        </c:set>
       	<input class="form-control inline-input" type="text" readonly="readonly" value="${authorities}"/>
    </div>
    <div class="field-box">
        <label>Uprawnienia raportowania</label>
        <c:set var="accesses">
        <c:forEach items="${profileNewForm.accesses}" var="access" varStatus="status"><spring:message code="${access.name}" text="${access.name}"></spring:message><c:if test="${!status.last}">, </c:if></c:forEach>
        </c:set>
       	<input class="form-control inline-input" type="text" readonly="readonly" value="${accesses}"/>
    </div>
	<spring:bind path="accepted">
	<c:set var="classes"><c:choose><c:when test="${status.error}">error</c:when></c:choose></c:set>
    <div class="field-box ${classes}">
    <label>Aktywny</label>
    	<form:hidden path="accepted"/>
		<div class="slider-frame primary">
		    <span data-on-text="TAK" data-off-text="NIE" class="slider-button"></span>
		</div>    
        <c:if test="${status.error}"><span class="alert-msg"><i class="icon-remove-sign"></i> <c:out value="${status.errorMessage}" escapeXml="false"/></span></c:if>
    </div>
    </spring:bind>    
                    
	<div class="wizard-actions">
		<button type="button" class="btn-glow primary btn-prev" onclick="location.href='profile/new/access'"><i class="icon-chevron-left"></i>&nbsp;Popraw</button><span>&nbsp;</span>
		<button type="submit" class="btn-glow success btn-finish" style="display: inline-block;" data-last="Zapisz">Zapisz profil!</button><span>&nbsp;</span>                            
	</div>

</form:form>

        
</tiles:putAttribute>
</tiles:insertDefinition>        