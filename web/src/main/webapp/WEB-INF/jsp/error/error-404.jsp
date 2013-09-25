<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<c:set var="env" scope="page"><spring:message code="project.environment"/></c:set>

<tiles:insertDefinition name="tiles-error" flush="true">
<tiles:putAttribute name="id" type="string">dbs-page-error-404</tiles:putAttribute>
<tiles:putAttribute name="title" type="string">Ups, problem...</tiles:putAttribute>
<tiles:putAttribute name="css" type="string"></tiles:putAttribute>
<tiles:putAttribute name="content" type="string">

        <div class="error-alerts-wrapper" id="pad-wrapper">
            <div class="row">
                <!-- avatar column -->
                <div class="col-md-3 col-md-offset-1 avatar-box">
                    <div class="personal-image">
                        <img src="img/dbs/error.png" class="avatar img-circle">
                    </div>
                </div>

                <!-- edit form column -->
                <div class="col-md-6 personal-info">
                    <div class="alert alert-danger">
                        <i class="icon-remove-sign"></i>Ups, problem... ten adres nie istnieje! (404)<br/>
 						Zgłoś błąd administratorowi podając powyższy kod błędu!                
                    </div>
                </div>
            </div>            
        </div>
        
</tiles:putAttribute>
</tiles:insertDefinition>             