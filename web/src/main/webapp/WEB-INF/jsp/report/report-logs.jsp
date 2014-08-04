<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<c:if test="${!empty report.logs}">
	                    <div class="pop-dialog full pop-dialog-log">
	                        <div class="pointer">
	                            <div class="arrow"></div>
	                            <div class="arrow_border"></div>
	                        </div>
	                        <div class="body">                        
	                            <div >
	                                <!-- a class="close-icon"><i class="icon-remove-sign"></i></a-->
	                                <div class="items">
	                                    <c:forEach items="${report.logs}" var="log" varStatus="festatus">
	                                    <div class="item<c:if test="${(festatus.index mod 2) eq 0}"> grey</c:if>">
	                                        <span class="title ${log.type}">
	                                        <c:choose>
	                                        <c:when test="${log.type eq 'INFO'}"><i class="icon-tasks"></i> &nbsp;info</c:when>
	                                        <c:when test="${log.type eq 'WARNING'}"><i class="icon-warning-sign"></i> &nbsp;uwaga</c:when>
	                                        <c:otherwise><i class="icon-bolt"></i> &nbsp;błąd</c:otherwise>
	                                        </c:choose>
	                                       
	                                        </span>
	                                        <span class="time"><i class="icon-time"></i>&nbsp;<fmt:formatDate value="${log.date}" type="both" pattern="dd-MM-yyyy HH:mm:ss" /></span><br/>
	                                        <span class="msg"><c:out value="${log.msg}"/></span>
	                                        
	                                    </div>
	                                    </c:forEach>
	                                </div>
	                            </div>
	                        </div>
	                    </div>
</c:if>
  