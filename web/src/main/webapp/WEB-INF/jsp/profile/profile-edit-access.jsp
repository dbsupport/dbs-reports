<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<tiles:insertDefinition name="tiles-wizard" flush="true">
<tiles:putAttribute name="id" type="string">dbs-page-profile-edit</tiles:putAttribute>
<tiles:putAttribute name="title" type="string">Edytuj profil</tiles:putAttribute>
<tiles:putAttribute name="steps" type="string">Edytuj personalia;Edytuj uprawnienia;Zapisz</tiles:putAttribute>
<tiles:putAttribute name="step" type="string">2</tiles:putAttribute>
<tiles:putAttribute name="css" type="string">
<link rel="stylesheet" href="css/compiled/new-user.css" type="text/css" media="screen" />
<link rel="stylesheet" href="css/compiled/form-wizard.css" type="text/css" media="screen" />
<link href="css/lib/select2.css" type="text/css" rel="stylesheet">
<link rel="stylesheet" href="css/dbs/dbs-wizard.css" type="text/css" media="screen" />
<link rel="stylesheet" href="css/dbs/dbs-profile-new.css" type="text/css" media="screen" />
</tiles:putAttribute>
<tiles:putAttribute name="js" type="string">
<script src="js/select2.min.js"></script>
<script src="js/dbs/dbs-multiselect.js"></script>
</tiles:putAttribute>
<tiles:putAttribute name="content" type="string">

<form:form method="post" modelAttribute="profileEditForm" action="profile/edit/access" class="" enctype="multipart/form-data">
	<input type="hidden" name="page" value="2"/>

	<spring:bind path="authorities">
    <c:set var="classes"><c:choose><c:when test="${status.error}">error</c:when></c:choose></c:set>    	
   	<div class="field-box ${classes}">
    	<label>Uprawnienia dostępu</label>
        <form:select multiple="multiple" path="authorities" cssClass="multiselect">
			<form:options items="${authorities}" itemLabel="name" itemValue="id"/>
		</form:select>
		<c:if test="${status.error}"><span class="alert-msg"><i class="icon-remove-sign"></i> <c:out value="${status.errorMessage}" escapeXml="false"/></span></c:if>
	</div>
	</spring:bind>

	<spring:bind path="accesses">
    <c:set var="classes"><c:choose><c:when test="${status.error}">error</c:when></c:choose></c:set>    	
   	<div class="field-box ${classes}">    	
    	<label>Uprawnienia raportowe</label>
    	<form:select multiple="multiple" path="accesses" cssClass="multiselect">
			<form:options items="${accesses}" itemLabel="name" itemValue="id"/>
		</form:select>
		<c:if test="${status.error}"><span class="alert-msg"><i class="icon-remove-sign"></i> <c:out value="${status.errorMessage}" escapeXml="false"/></span></c:if>
	</div>
	</spring:bind>
   
   	<div class="wizard-actions">
   		<button type="button" class="btn-glow primary btn-prev" onclick="location.href='profile/edit/personal'"><i class="icon-chevron-left"></i>&nbsp;Powrót</button><span>&nbsp;</span>
		<button type="submit" class="btn-glow primary btn-next" data-last="Dalej">Dalej&nbsp;&nbsp;<i class="icon-chevron-right"></i></button><span>&nbsp;</span>
  	</div>
</form:form>

        
</tiles:putAttribute>
</tiles:insertDefinition>        