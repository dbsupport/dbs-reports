<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>


<c:if test="${!empty unarchiveds.reports}">
<h4><a href="report/unarchived">Zlecone raporty (${unarchiveds.all}/${unarchiveds.max})</a></h4>

<div class="row">
<div class="col-md-12">
                                 
                    <table cellpadding="0" cellspacing="0" border="0" class="dataTable" style="width:100%">
                        <thead>
                            <tr>
								<th tabindex="0" class="">
								    <a class="sorter-ctrl">Nazwa</a>
								</th>
								
                                <th tabindex="0" class="">
                                    <a class="sorter-ctrl">Stan</a>
                                </th>
								
                                <th tabindex="0" class="">
                                    <a class="sorter-ctrl">Data wygenerowania</a>
                                </th>                                
                                <th tabindex="0" class="">
                                    <a class="sorter-ctrl">Definicja</a>
                                </th>
                                                                
                                
                            </tr>
                        </thead>
                        
                        <tbody>
                        <c:forEach items="${unarchiveds.reports}" varStatus="rstatus" var="report">
                        <c:set var="trclass" scope="page"><c:choose><c:when test="${rstatus.index mod 2 eq 0}">odd</c:when><c:otherwise>even</c:otherwise></c:choose></c:set>
                        <tr class="${trclass}">
                                
                                <td>
                                <c:out value="${report.name}"/><br/>
                                <c:choose>
                                <c:when test="${report.status == 'FAILED'}"><span class="label label-danger">błędny</span></c:when>
                                </c:choose>
                                </td>
                                
                                <td>
                                <c:choose>
                                <c:when test="${report.phase.status == 'INIT' or report.phase.status == 'START'}"><span class="label label-warning">zlecony</span></c:when>
                                <c:when test="${report.phase.status == 'READY'}"><span class="label label-info">gotowy</span></c:when>
                                <c:otherwise><span class="label label-success">niezarchiwizowany</c:otherwise>
                                </c:choose>
                                </td>
                                
                                <td><fmt:formatDate value="${report.generationDate}" type="both" pattern="dd-MM-yyyy HH:mm:ss" /></td>
                                <td>
                                        <c:choose>
                                        <c:when test="${report.pattern.active eq true}"><a href="report/pattern/details/${report.pattern.id}">${report.pattern.name} ${report.pattern.version}</a></c:when>
                                        <c:otherwise>${report.pattern.name} ${report.pattern.version}</c:otherwise>
                                        </c:choose>                                
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>

        </div>                
    </div>
</c:if>
      