<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<tiles:insertDefinition name="tiles-wizard" flush="true">
<tiles:putAttribute name="id" type="string">dbs-page-parameter-edit</tiles:putAttribute>
<tiles:putAttribute name="title" type="string">Edycja parametrów aplikacji</tiles:putAttribute>
<tiles:putAttribute name="css" type="string">
<link rel="stylesheet" href="css/compiled/new-user.css" type="text/css" media="screen" />
<link rel="stylesheet" href="css/compiled/form-wizard.css" type="text/css" media="screen" />
<link rel="stylesheet" href="css/dbs/dbs-wizard.css" type="text/css" media="screen" />
</tiles:putAttribute>

<tiles:putAttribute name="js" type="string">
</tiles:putAttribute>

<tiles:putAttribute name="content" type="string">

                    <div class="col-md-8 column">
						<h4>Parametry bazy danych</h4>
						<br/><br/> 
                    	<form:form method="post" modelAttribute="parametersdbeditform" action="param/edit/db" class="dbs-form" enctype="multipart/form-data">
							<c:forEach items="${parametersdbeditform.params}" var="param" varStatus="rstatus"> 
		                    	<spring:bind path="params[${rstatus.index}]">
		                    	<c:set var="classes"><c:choose><c:when test="${status.error}">error</c:when></c:choose></c:set>
		                    	<div class="field-box ${classes}">
			                        <label>${parametersdbeditform.params[rstatus.index].key}</label>
			                        <c:choose>
			                        <c:when test="${parametersdbeditform.params[rstatus.index].type eq 'PASSWD'}">
			                        	<form:password path="params[${rstatus.index}].value" cssClass="form-control" showPassword="true" placeholder="Wartość"/>
			                        </c:when>
			                        <c:when test="${parametersdbeditform.params[rstatus.index].type eq 'FILE'}">
                                        <input type="file" name="params[${rstatus.index}].file" class="form-control" />
                                        <c:if test="${parametersdbeditform.params[rstatus.index].valued}"><span class="alert-msg">${parametersdbeditform.params[rstatus.index].desc}</span></c:if>
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
                    </div>
                        
                    <div class="col-md-8 column">                     
						<br/><br/><br/><br/><br/>   
						<h4>Parametry aplikacyjne</h4>
						<br/><br/>                     
                        <form:form method="post" modelAttribute="parametersappeditform" action="param/edit/app" class="" enctype="multipart/form-data">
                            
                            <c:forEach items="${parametersappeditform.params}" var="param" varStatus="rstatus"> 
                                <spring:bind path="params[${rstatus.index}]">
                                <c:set var="classes"><c:choose><c:when test="${status.error}">error</c:when></c:choose></c:set>
                                <div class="field-box ${classes}">
                                    <label>${parametersappeditform.params[rstatus.index].key}</label>
                                    <c:choose>
                                    <c:when test="${parametersappeditform.params[rstatus.index].type eq 'PASSWD'}">
                                        <form:password path="params[${rstatus.index}].value" cssClass="form-control" showPassword="true" placeholder="Wartość"/>
                                    </c:when>
                                    <c:when test="${parametersappeditform.params[rstatus.index].type eq 'FILE'}">
	                                    <input type="file" name="params[${rstatus.index}].file" class="form-control" />
	                                    <c:if test="${parametersappeditform.params[rstatus.index].valued}"><span class="alert-msg">${parametersappeditform.params[rstatus.index].desc}</span></c:if>
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
                                <button type="submit" class="btn-glow success btn-finish" style="display: inline-block;">Zapisz!</button><span>&nbsp;</span>
                            </div>
                        </form:form>                        
                    </div>
</tiles:putAttribute>
</tiles:insertDefinition>        