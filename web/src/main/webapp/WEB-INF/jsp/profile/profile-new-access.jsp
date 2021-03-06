<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<tiles:insertDefinition name="tiles-wizard" flush="true">
<tiles:putAttribute name="id" type="string">dbs-page-profile-new</tiles:putAttribute>
<tiles:putAttribute name="title" type="string">Nowy profil</tiles:putAttribute>
<tiles:putAttribute name="steps" type="string">Wprowadź personalia;Nadaj uprawnienia;Zapisz</tiles:putAttribute>
<tiles:putAttribute name="step" type="string">2</tiles:putAttribute>
<tiles:putAttribute name="css" type="string">
<link rel="stylesheet" href="css/compiled/new-user.css" type="text/css" media="screen" />
<link rel="stylesheet" href="css/compiled/form-wizard.css" type="text/css" media="screen" />

<link rel="stylesheet" type="text/css" href="css/bootstrap/bootstrap-select.min.css">
<link rel="stylesheet" href="css/dbs/dbs-wizard.css" type="text/css" media="screen" />
<link rel="stylesheet" href="css/dbs/dbs-profile-new.css" type="text/css" media="screen" />
</tiles:putAttribute>
<tiles:putAttribute name="js" type="string">
<script type="text/javascript" src="js/bootstrap-select.min.js"></script>
<script src="js/dbs/dbs-multiselect.js"></script>
</tiles:putAttribute>

<tiles:putAttribute name="content" type="string">

<form:form method="post" modelAttribute="profileNewForm" action="profile/new/access" class="dbs-form" enctype="multipart/form-data">
	<input type="hidden" name="page" value="2"/>
	
	<spring:bind path="authorities">
    <c:set var="classes"><c:choose><c:when test="${status.error}">error</c:when></c:choose></c:set>    	
   	<div class="field-box ${classes}">
    	<label>Uprawnienia dostępu</label>
        <form:select multiple="multiple" path="authorities" cssClass="selectpicker" data-width="400px" data-size="20" title="- wybierz -">
			<form:options items="${authorities}" itemLabel="name" itemValue="id"/>
		</form:select>
		<c:if test="${status.error}"><span class="alert-msg"><i class="icon-remove-sign"></i> <c:out value="${status.errorMessage}" escapeXml="false"/></span></c:if>
	</div>
	</spring:bind>

	<spring:bind path="accesses">
    <c:set var="classes"><c:choose><c:when test="${status.error}">error</c:when></c:choose></c:set>    	
   	<div class="field-box ${classes}">    	
    	<label>Uprawnienia raportowe</label>
    	<form:select multiple="multiple" path="accesses" cssClass="selectpicker" data-width="400px" data-size="20" title="- wybierz -">
			<form:options items="${accesses}" itemLabel="name" itemValue="id"/>
		</form:select>
		<c:if test="${status.error}"><span class="alert-msg"><i class="icon-remove-sign"></i> <c:out value="${status.errorMessage}" escapeXml="false"/></span></c:if>
	</div>
	</spring:bind>

    <sec:authorize access="hasAnyRole('Admin')">
        <c:if test="${!empty groups}">
            <spring:bind path="groups">
                <c:set var="classes"><c:choose><c:when test="${status.error}">error</c:when></c:choose></c:set>
                <div class="field-box ${classes}">
                    <label>Grupy profilowe<br/><small>Uwaga!<br/>Uprawnienia grupy nadpisują<br/>uprawnienia raportowe</small></label>
                    <form:select multiple="multiple" path="groups" cssClass="selectpicker" data-width="400px" data-size="20" title="- wybierz -">
                        <form:options items="${groups}" itemLabel="name" itemValue="id"/>
                    </form:select>
                    <c:if test="${status.error}"><span class="alert-msg"><i class="icon-remove-sign"></i> <c:out value="${status.errorMessage}" escapeXml="false"/></span></c:if>
                </div>
            </spring:bind>
        </c:if>
    </sec:authorize>

	<div class="wizard-actions">
   		<button type="button" class="btn-glow primary btn-prev" onclick="location.href='profile/new/personal'"><i class="icon-chevron-left"></i>&nbsp;Powrót</button><span>&nbsp;</span>
		<button type="submit" class="btn-glow primary btn-next" data-last="Finish">Dalej&nbsp;&nbsp;<i class="icon-chevron-right"></i></button><span>&nbsp;</span>
  	</div>
</form:form>
        
</tiles:putAttribute>
</tiles:insertDefinition>        