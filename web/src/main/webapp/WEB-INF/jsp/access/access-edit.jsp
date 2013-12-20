<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<tiles:insertDefinition name="tiles-wizard" flush="true">
<tiles:putAttribute name="id" type="string">dbs-page-access-edit</tiles:putAttribute>
<tiles:putAttribute name="title" type="string">Edycja uprawnienia raportowego</tiles:putAttribute>
<tiles:putAttribute name="css" type="string">
<link rel="stylesheet" href="css/compiled/new-user.css" type="text/css" media="screen" />
<link rel="stylesheet" href="css/compiled/form-wizard.css" type="text/css" media="screen" />
<link rel="stylesheet" href="css/dbs/dbs-wizard.css" type="text/css" media="screen" />
</tiles:putAttribute>

<tiles:putAttribute name="content" type="string">

                    	<form:form method="post" modelAttribute="accessEditForm" action="access/edit" class="">

	                    	<spring:bind path="name">
	                    	<c:set var="classes"><c:choose><c:when test="${status.error}">error</c:when></c:choose></c:set>
							<div class="field-box ${classes}">
		                        <label>Nazwa</label>
	                                <form:input path="name" cssClass="form-control" placeholder="Nazwa"/>
	                                <c:if test="${status.error}"><span class="alert-msg"><i class="icon-remove-sign"></i> <c:out value="${status.errorMessage}" escapeXml="false"/></span></c:if>
	                        </div>
	                        </spring:bind>
	                        
							
	                    	<spring:bind path="description">
	                    	<c:set var="classes"><c:choose><c:when test="${status.error}">error</c:when></c:choose></c:set>
                            <div class="field-box ${classes}">
		                        <label>Opis</label>
							        <form:textarea path="description" cssClass="form-control" rows="4" cols="20" placeholder="Opis"/>
	                                <c:if test="${status.error}"><span class="alert-msg"><i class="icon-remove-sign"></i> <c:out value="${status.errorMessage}" escapeXml="false"/></span></c:if>
	                        </div>                           
                            </spring:bind>
	                                                   
                            
                            <div class="wizard-actions">
                            	<button type="button" class="btn-glow" onclick="location.href='access/edit'"><i class="icon-refresh"></i>&nbsp;Wyczyść</button><span>&nbsp;</span>
                            	<button type="submit" class="btn-glow success btn-finish" style="display: inline-block;">Zapisz uprawnienie!</button>  
                            </div>
                        </form:form>
        
</tiles:putAttribute>
</tiles:insertDefinition>        