<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<tiles:insertDefinition name="tiles-browser-tables" flush="true">
<tiles:putAttribute name="id" type="string">dbs-page-activedirectory-list</tiles:putAttribute>
<tiles:putAttribute name="title" type="string">Lista profili Active Directory</tiles:putAttribute>
<tiles:putAttribute name="css" type="string">
<link rel="stylesheet" href="css/compiled/form-wizard.css" type="text/css" media="screen" />
<link rel="stylesheet" href="css/dbs/dbs-activedirectory-list.css" type="text/css" media="screen" />
<link rel="stylesheet" href="css/lib/bootstrap.datepicker.css" type="text/css" >
</tiles:putAttribute>
<tiles:putAttribute name="js" type="string">
<script src="js/bootstrap.datepicker.js"></script>
<script src="js/locales/bootstrap-datepicker.pl.js"></script>
<script src="js/dbs/dbs-datepicker.js"></script>

<script src="js/jquery.dataTables.js"></script>
<script src="js/dbs/dbs-activedirectory-list.js"></script>
</tiles:putAttribute>

<tiles:putAttribute name="form" type="string">
                <form:form method="post" modelAttribute="activeDirectoryListForm" action="activedirectory/list" class="dbs-form">
                    <input type="hidden" name="action" value=""/>
                    <form:input path="value" placeholder="Wyszukaj..." onblur="this.form.submit();"/>
                </form:form>
</tiles:putAttribute>

<tiles:putAttribute name="content" type="string">
<div class="form-wrapper">
    <form:form method="post" modelAttribute="activeDirectoryListForm" action="activedirectory/list" class="dbs-form">
        <input type="hidden" name="action" value=""/>
        
                    <table cellpadding="0" cellspacing="0" border="0" class="dataTable" id="">
                        <thead>
                            <tr>
                                <th><input type="checkbox" class="checker-ctrl"/></th>
                                
                                <tiles:insertDefinition name="tiles-sorter-tables">
                                <tiles:putAttribute name="name" type="string">number</tiles:putAttribute>
                                <tiles:putAttribute name="label" type="string">numer</tiles:putAttribute>
                                <c:set var="sorter" value="${activeDirectoryListForm.filter.sorter}" scope="request"/>
                                </tiles:insertDefinition>
                                
                                <tiles:insertDefinition name="tiles-sorter-tables">
                                <tiles:putAttribute name="name" type="string">locationCode</tiles:putAttribute>
                                <tiles:putAttribute name="label" type="string">Kod lokalizacj</tiles:putAttribute>
                                <c:set var="sorter" value="${activeDirectoryListForm.filter.sorter}" scope="request"/>
                                </tiles:insertDefinition>
                                
                                <tiles:insertDefinition name="tiles-sorter-tables">
                                <tiles:putAttribute name="name" type="string">locationName</tiles:putAttribute>
                                <tiles:putAttribute name="label" type="string">Nazwa lokalizacji</tiles:putAttribute>
                                <c:set var="sorter" value="${activeDirectoryListForm.filter.sorter}" scope="request"/>
                                </tiles:insertDefinition>
                                
                                <tiles:insertDefinition name="tiles-sorter-tables">
                                <tiles:putAttribute name="name" type="string">firstName</tiles:putAttribute>
                                <tiles:putAttribute name="label" type="string">Imię</tiles:putAttribute>
                                <c:set var="sorter" value="${activeDirectoryListForm.filter.sorter}" scope="request"/>
                                </tiles:insertDefinition>
                                
                                <tiles:insertDefinition name="tiles-sorter-tables">
                                <tiles:putAttribute name="name" type="string">lastName</tiles:putAttribute>
                                <tiles:putAttribute name="label" type="string">Nazwisko</tiles:putAttribute>
                                <c:set var="sorter" value="${activeDirectoryListForm.filter.sorter}" scope="request"/>
                                </tiles:insertDefinition>                                                                   

                                <tiles:insertDefinition name="tiles-sorter-tables">
                                <tiles:putAttribute name="name" type="string">unitCode</tiles:putAttribute>
                                <tiles:putAttribute name="label" type="string">Kod jednostki organizacyjnej</tiles:putAttribute>
                                <c:set var="sorter" value="${activeDirectoryListForm.filter.sorter}" scope="request"/>
                                </tiles:insertDefinition>                                                                   

                                <tiles:insertDefinition name="tiles-sorter-tables">
                                <tiles:putAttribute name="name" type="string">unitName</tiles:putAttribute>
                                <tiles:putAttribute name="label" type="string">Nazwa jednostki organizacyjnej</tiles:putAttribute>
                                <c:set var="sorter" value="${activeDirectoryListForm.filter.sorter}" scope="request"/>
                                </tiles:insertDefinition>                                                                   

                                <tiles:insertDefinition name="tiles-sorter-tables">
                                <tiles:putAttribute name="name" type="string">employmentDate</tiles:putAttribute>
                                <tiles:putAttribute name="label" type="string">Data zatrudnienia</tiles:putAttribute>
                                <c:set var="sorter" value="${activeDirectoryListForm.filter.sorter}" scope="request"/>
                                </tiles:insertDefinition>                                                                   

                                <tiles:insertDefinition name="tiles-sorter-tables">
                                <tiles:putAttribute name="name" type="string">dismissalDate</tiles:putAttribute>
                                <tiles:putAttribute name="label" type="string">Data zwolnienia</tiles:putAttribute>
                                <c:set var="sorter" value="${activeDirectoryListForm.filter.sorter}" scope="request"/>
                                </tiles:insertDefinition>  
                            </tr>
                        </thead>
                        <tfoot>
                            <tr>
                                <th><input type="checkbox" class="checker-ctrl"/></th>
                                
                                <tiles:insertDefinition name="tiles-sorter-tables">
                                <tiles:putAttribute name="name" type="string">number</tiles:putAttribute>
                                <tiles:putAttribute name="label" type="string">numer</tiles:putAttribute>
                                <c:set var="sorter" value="${activeDirectoryListForm.filter.sorter}" scope="request"/>
                                </tiles:insertDefinition>
                                
                                <tiles:insertDefinition name="tiles-sorter-tables">
                                <tiles:putAttribute name="name" type="string">locationCode</tiles:putAttribute>
                                <tiles:putAttribute name="label" type="string">Kod lokalizacj</tiles:putAttribute>
                                <c:set var="sorter" value="${activeDirectoryListForm.filter.sorter}" scope="request"/>
                                </tiles:insertDefinition>
                                
                                <tiles:insertDefinition name="tiles-sorter-tables">
                                <tiles:putAttribute name="name" type="string">locationName</tiles:putAttribute>
                                <tiles:putAttribute name="label" type="string">Nazwa lokalizacji</tiles:putAttribute>
                                <c:set var="sorter" value="${activeDirectoryListForm.filter.sorter}" scope="request"/>
                                </tiles:insertDefinition>
                                
                                <tiles:insertDefinition name="tiles-sorter-tables">
                                <tiles:putAttribute name="name" type="string">firstName</tiles:putAttribute>
                                <tiles:putAttribute name="label" type="string">Imię</tiles:putAttribute>
                                <c:set var="sorter" value="${activeDirectoryListForm.filter.sorter}" scope="request"/>
                                </tiles:insertDefinition>
                                
                                <tiles:insertDefinition name="tiles-sorter-tables">
                                <tiles:putAttribute name="name" type="string">lastName</tiles:putAttribute>
                                <tiles:putAttribute name="label" type="string">Nazwisko</tiles:putAttribute>
                                <c:set var="sorter" value="${activeDirectoryListForm.filter.sorter}" scope="request"/>
                                </tiles:insertDefinition>                                                                   

                                <tiles:insertDefinition name="tiles-sorter-tables">
                                <tiles:putAttribute name="name" type="string">unitCode</tiles:putAttribute>
                                <tiles:putAttribute name="label" type="string">Kod jednostki organizacyjnej</tiles:putAttribute>
                                <c:set var="sorter" value="${activeDirectoryListForm.filter.sorter}" scope="request"/>
                                </tiles:insertDefinition>                                                                   

                                <tiles:insertDefinition name="tiles-sorter-tables">
                                <tiles:putAttribute name="name" type="string">unitName</tiles:putAttribute>
                                <tiles:putAttribute name="label" type="string">Nazwa jednostki organizacyjnej</tiles:putAttribute>
                                <c:set var="sorter" value="${activeDirectoryListForm.filter.sorter}" scope="request"/>
                                </tiles:insertDefinition>                                                                   

                                <tiles:insertDefinition name="tiles-sorter-tables">
                                <tiles:putAttribute name="name" type="string">employmentDate</tiles:putAttribute>
                                <tiles:putAttribute name="label" type="string">Data zatrudnienia</tiles:putAttribute>
                                <c:set var="sorter" value="${activeDirectoryListForm.filter.sorter}" scope="request"/>
                                </tiles:insertDefinition>                                                                   

                                <tiles:insertDefinition name="tiles-sorter-tables">
                                <tiles:putAttribute name="name" type="string">dismissalDate</tiles:putAttribute>
                                <tiles:putAttribute name="label" type="string">Data zwolnienia</tiles:putAttribute>
                                <c:set var="sorter" value="${activeDirectoryListForm.filter.sorter}" scope="request"/>
                                </tiles:insertDefinition>                                                                   
                            </tr>
                        </tfoot> 
                                                
                        <tbody>
                        <c:forEach items="${profiles}" varStatus="rstatus" var="profile">
                        <c:set var="trclass" scope="page"><c:choose><c:when test="${rstatus.index mod 2 eq 0}">odd</c:when><c:otherwise>even</c:otherwise></c:choose></c:set>
                        <tr class="${trclass}">
                                <td>&nbsp;&nbsp;&nbsp;<form:checkbox path="id" value="${profile.number}"/></td>
                                <td>${profile.number}</td>
                                <td>${profile.locationCode}</td>
                                <td>${profile.locationName}</td>
                                <td>${profile.firstName}</td>
                                <td>${profile.lastName}</td>
                                <td>${profile.unitCode}</td>
                                <td>${profile.unitName}</td>
                                <td><fmt:formatDate value="${profile.employmentDate}" type="both" pattern="dd-MM-yyyy" /></td>
                                <td><fmt:formatDate value="${profile.dismissalDate}" type="both" pattern="dd-MM-yyyy" /></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                    
                    <tiles:insertDefinition name="tiles-pager-tables">
            			<c:set var="pager" value="${activeDirectoryListForm.filter.pager}" scope="request"/>
            		</tiles:insertDefinition>
            		
            		<br/><br/><br/>
            		  
					<spring:bind path="date">
					<div class="field-box<c:choose><c:when test="${status.error}"> error</c:when></c:choose>">
					<label>
                    <button type="submit" class="btn-glow success btn-finish" data-last="Finish" onclick="this.form.action.value='INSERT'">Wstaw datę&nbsp;&nbsp;</button><span>&nbsp;</span>
                    </label>
					    <div class="col-md-1">
					    <form:input path="date" class="form-control input-datepicker" data-date-format="yyyy-mm-dd" placeholder="Podaj datę"/>
					    <c:if test="${status.error}"><span class="alert-msg"><i class="icon-remove-sign"></i> <c:out value="${status.errorMessage}" escapeXml="false"/></span></c:if>
					    </div>
					</div>
					</spring:bind>                    
    </form:form>
</div>                        
        
</tiles:putAttribute>
</tiles:insertDefinition>        