<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<tiles:insertDefinition name="tiles-browser-tables" flush="true">
<tiles:putAttribute name="id" type="string">dbs-page-profile-group-list</tiles:putAttribute>
<tiles:putAttribute name="title" type="string">Grupy profilowe</tiles:putAttribute>
<tiles:putAttribute name="css" type="string">
    <link rel="stylesheet" type="text/css" href="css/bootstrap/bootstrap-select.min.css">
    <link rel="stylesheet" href="css/compiled/form-wizard.css" type="text/css" media="screen" />
    <link rel="stylesheet" href="css/dbs/dbs-profile-group-list.css" type="text/css" media="screen" />
</tiles:putAttribute>


<tiles:putAttribute name="js" type="string">
    <script type="text/javascript" src="js/bootstrap-select.min.js"></script>
    <script src="js/dbs/dbs-multiselect.js"></script>
</tiles:putAttribute>

<tiles:putAttribute name="form" type="string">
                <form:form method="post" modelAttribute="profilegrouplistform" id="profilegrouplistform" action="profile/group/list" class="dbs-form">
                    <input type="hidden" name="action" value=""/>
                <div class="row filter-block">
                    <div class="pull-right">
                        <%--<div class="ui-select">--%>
		                    <%--<spring:bind path="accesses">--%>
		                    <%--<c:set var="classes"><c:choose><c:when test="${status.error}">error</c:when></c:choose></c:set>--%>
                                <%--<form:select multiple="multiple" path="accesses" cssClass="selectpicker" data-width="100px" data-size="20" title="- wybierz -">--%>
                                    <%--<form:options items="${accesses}" itemLabel="name" itemValue="id"/>--%>
                                <%--</form:select>--%>
		                        <%--<c:if test="${status.error}"><span class="alert-msg"><i class="icon-remove-sign"></i> <c:out value="${status.errorMessage}" escapeXml="false"/></span></c:if>--%>
		                    <%--</spring:bind>--%>
                        <%--</div>--%>

                        <form:input path="name" placeholder="Wyszukaj..." onblur="this.form.submit();"/>
                    </div>
                    <div class="pull-right">
                        <a href="profile/group/new/init" class="btn-flat success"><span>&#43;</span>DODAJ NOWĄ GRUPĘ</a>
                    </div>
                </div>


                </form:form>

</tiles:putAttribute>



<tiles:putAttribute name="content" type="string">
<div class="form-wrapper">
    <form:form method="post" modelAttribute="profilegrouplistform" action="profile/group/list" class="dbs-form">
        <input type="hidden" name="action" value=""/>
                            <table cellpadding="0" cellspacing="0" border="0" class="dataTable" id="">
                        <thead>
                            <tr>
                                <th class="checker"><input type="checkbox" class="checker-ctrl"/></th>
                                
                                <tiles:insertDefinition name="tiles-sorter-tables">
                                <tiles:putAttribute name="name" type="string">name</tiles:putAttribute>
                                <tiles:putAttribute name="label" type="string">Nazwa</tiles:putAttribute>
                                <c:set var="sorter" value="${profilegrouplistform.filter.sorter}" scope="request"/>
                                </tiles:insertDefinition>
                                
                                <tiles:insertDefinition name="tiles-sorter-tables">
                                <tiles:putAttribute name="name" type="string">decription</tiles:putAttribute>
                                <tiles:putAttribute name="label" type="string">Opis</tiles:putAttribute>
                                <tiles:putAttribute name="class" type="string">sorting description</tiles:putAttribute>
                                </tiles:insertDefinition>


                                <td>
                                    Dostępy raportowe
                                </td>
>

                                <td>
                                    Profile
                                </td>

                            </tr>
                        </thead>
                        <tfoot>
                            <tr>
                                <th><input type="checkbox" class="checker-ctrl"/></th>

                                <tiles:insertDefinition name="tiles-sorter-tables">
                                    <tiles:putAttribute name="name" type="string">name</tiles:putAttribute>
                                    <tiles:putAttribute name="label" type="string">Nazwa</tiles:putAttribute>
                                    <c:set var="sorter" value="${profilegrouplistform.filter.sorter}" scope="request"/>
                                </tiles:insertDefinition>

                                <tiles:insertDefinition name="tiles-sorter-tables">
                                    <tiles:putAttribute name="name" type="string">description</tiles:putAttribute>
                                    <tiles:putAttribute name="label" type="string">Opis</tiles:putAttribute>
                                </tiles:insertDefinition>


                                <td>
                                    Dostępy raportowe
                                </td>

                                <td>
                                    Profile
                                </td>

                            </tr>
                        </tfoot> 
                                                
                        <tbody>
                        <c:set var="profilesmax" value="5" scope="page"/>
                        <c:forEach items="${groups}" varStatus="rstatus" var="group">
                        <c:set var="trclass" scope="page"><c:choose><c:when test="${rstatus.index mod 2 eq 0}">odd</c:when><c:otherwise>even</c:otherwise></c:choose></c:set>
                        <tr class="${trclass}">
                                <td>&nbsp;&nbsp;&nbsp;<form:checkbox path="id" value="${group.id}" class="report"/></td>
                                <td>
                                <a href="profile/group/${group.id}/edit/init" title="Edycja">${group.name}</a>
                                </td>

                                <td>
                                    ${group.description}
                                </td>

                                <td>
                                   ${group.accessesAsString}
                                </td>
                            <td>
                                <c:forEach items="${group.profiles}" varStatus="rstatus" var="profile" end="${profilesmax-1}">
                                    ${profile.login}
                                    <c:choose>
                                        <c:when test="${profile.global}">${profile.description}</c:when>
                                        <c:otherwise>${profile.name}</c:otherwise>
                                    </c:choose>

                                    <c:if test="${!rstatus.last}"><br/></c:if>
                                </c:forEach>

                                <c:if test="${fn:length(group.profiles)>profilesmax}">
                                    <br/><a onclick="$('#group${group.id}profiles').toggle();" style="cursor: pointer;">${fn:length(group.profiles)-profilesmax} z ${fn:length(group.profiles)} więcej..</a>
                                    <br/>

                                    <div id="group${group.id}profiles" style="display:none;">
                                        <c:forEach items="${group.profiles}" varStatus="rstatus" var="profile" begin="${profilesmax}">
                                            ${profile.login}
                                            <c:choose>
                                                <c:when test="${profile.global}">${profile.description}</c:when>
                                                <c:otherwise>${profile.name}</c:otherwise>
                                            </c:choose>

                                            <c:if test="${!rstatus.last}"><br/></c:if>
                                        </c:forEach>
                                    </div>
                                </c:if>

                            </td>
                                
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                    
                    <tiles:insertDefinition name="tiles-pager-tables">
                        <c:set var="pager" value="${profilegrouplistform.filter.pager}" scope="request"/>
                    </tiles:insertDefinition>

                    <c:if test="${!empty groups}">
                    <div class="field-box">

                        <button type="submit" class="btn-glow inverse" data-last="Finish" onclick="this.form.action.value='REMOVE'">
                            <i class="icon-remove"></i>
                            Usuń&nbsp;&nbsp;</button><span>&nbsp;</span>

                    </div>
                    </c:if>
    </form:form>
</div>                        
        
</tiles:putAttribute>
</tiles:insertDefinition>        