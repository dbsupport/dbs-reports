<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<tiles:insertDefinition name="tiles-wizard" flush="true">
<tiles:putAttribute name="id" type="string">dbs-page-parameter-edit</tiles:putAttribute>
<tiles:putAttribute name="title" type="string">Edycja parametrów bazy danych</tiles:putAttribute>
<tiles:putAttribute name="css" type="string">
<link rel="stylesheet" href="css/compiled/new-user.css" type="text/css" media="screen" />
<link rel="stylesheet" href="css/compiled/form-wizard.css" type="text/css" media="screen" />
<link rel="stylesheet" href="css/dbs/dbs-wizard.css" type="text/css" media="screen" />
</tiles:putAttribute>

<tiles:putAttribute name="content" type="string">

                    	<form:form method="post" modelAttribute="paramEditForm" action="param/edit" class="dbs-form">

							<c:forEach items="${paramEditForm.params}" var="param" varStatus="rstatus"> 
		                    	<spring:bind path="params[${rstatus.index}]">
		                    	<c:set var="classes"><c:choose><c:when test="${status.error}">error</c:when></c:choose></c:set>
								<div class="field-box ${classes}">
			                        <label>${paramEditForm.params[rstatus.index].key}</label>
			                        <c:choose>
			                        <c:when test="${paramEditForm.params[rstatus.index].type eq 'PASSWD'}">
			                        	<form:password path="params[${rstatus.index}].value" cssClass="form-control" showPassword="true" placeholder="Wartość"/>
			                        </c:when>
			                        <c:otherwise>
			                        	<form:input path="params[${rstatus.index}].value" cssClass="form-control" placeholder="Wartość"/>
			                        </c:otherwise>
			                        </c:choose>
		                            <c:if test="${status.error}"><span class="alert-msg"><i class="icon-remove-sign"></i> <c:out value="${status.errorMessage}" escapeXml="false"/></span></c:if>
		                        </div>
		                        </spring:bind>
		                    </c:forEach>
	                        
                            <div class="wizard-actions">
                            	<button type="submit" class="btn-glow success btn-finish" style="display: inline-block;">Zapisz i testuj!</button><span>&nbsp;</span>
                            </div>
                        </form:form>
        
</tiles:putAttribute>
</tiles:insertDefinition>        