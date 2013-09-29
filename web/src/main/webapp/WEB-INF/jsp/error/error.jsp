<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<c:set var="env" scope="page"><spring:message code="project.environment"/></c:set>

<tiles:insertDefinition name="tiles-error" flush="true">
<tiles:putAttribute name="id" type="string">dbs-page-error</tiles:putAttribute>
<tiles:putAttribute name="title" type="string">Ups, problem...</tiles:putAttribute>
<tiles:putAttribute name="css" type="string"></tiles:putAttribute>
<tiles:putAttribute name="content" type="string">

        <div class="error-alerts-wrapper" id="pad-wrapper">
            <div class="row">
                <div class="col-md-9 personal-info">
                    <div class="alert alert-danger">
                        <i class="icon-remove-sign"></i>Ups, problem... <c:out value="${exceptionId}"/><br/>
 						<c:if test="${env ne 'dev' and env ne 'tes'}">Zgłoś błąd administratorowi podając powyższy kod błędu!</c:if>                
                    </div>
                    
					<c:if test="${env eq 'dev' or env eq 'tes'}">
                    <h5 class="personal-title">Szczegóły:</h5>
					<c:out value="${exception}"></c:out>
					</c:if>

                </div>
            </div>            
        </div>
        
</tiles:putAttribute>
</tiles:insertDefinition>             