<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<c:set var="aform" value="${requestScope[param.form]}"/>

<form:form method="post" modelAttribute="${param.form}" action="${param.action}" enctype="multipart/form-data">
    <c:forEach items="${aform.params}" var="parameter" varStatus="rstatus">
        <spring:bind path="params[${rstatus.index}]">
            <c:set var="classes"><c:choose><c:when test="${status.error}">error</c:when></c:choose></c:set>
            <div class="field-box ${classes}">
                <label>${parameter.key}</label>
                <c:choose>
                    <c:when test="${parameter.type eq 'PASSWD'}">
                        <form:password path="params[${rstatus.index}].value" cssClass="form-control" showPassword="true" placeholder="Wartość"/>
                    </c:when>
                    <c:when test="${parameter.type eq 'FILE'}">
                        <input type="file" name="params[${rstatus.index}].file" class="form-control" />
                        <c:if test="${parameter.valued}"><span class="alert-msg">${parameter.desc}</span></c:if>
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
