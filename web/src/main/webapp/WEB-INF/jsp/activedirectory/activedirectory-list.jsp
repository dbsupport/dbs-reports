<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<tiles:insertDefinition name="tiles-browser" flush="true">
<tiles:putAttribute name="id" type="string">dbs-page-activedirectory-list</tiles:putAttribute>
<tiles:putAttribute name="title" type="string">Lista profili Active Directory</tiles:putAttribute>
<tiles:putAttribute name="css" type="string">

<link href="css/lib/jquery.dataTables.css" type="text/css" rel="stylesheet" />

</tiles:putAttribute>
<tiles:putAttribute name="js" type="string">
<script src="js/jquery.dataTables.js"></script>
<script src="js/dbs/dbs-activedirectory-list.js"></script>
</tiles:putAttribute>

<tiles:putAttribute name="form" type="string">
                <form:form method="post" modelAttribute="activeDirectoryListForm" action="activedirectory/list" class="dbs-form">
               		<form:input path="value" cssClass="col-md-5 search" placeholder="Wyszukaj..." onblur="this.form.submit();"/>
                </form:form>
</tiles:putAttribute>

<tiles:putAttribute name="content" type="string">

                    <table class="datatables-page" id="example">
                        <thead>
                            <tr>
                                <th tabindex="0" rowspan="1" colspan="1"><input type="checkbox">&nbsp;#</th>
                                <th tabindex="0" rowspan="1" colspan="2">Imię</th>
                                <th tabindex="0" rowspan="1" colspan="3">Nazwisko</th>
                                <th tabindex="0" rowspan="1" colspan="4">Lokalizacja</th>
                                <th tabindex="0" rowspan="1" colspan="5">Data zwolnienia</th>
                            </tr>
                        </thead>
                        <tfoot>
                            <tr>
                                <th tabindex="0" rowspan="1" colspan="1"><input type="checkbox">&nbsp;#</th>
                                <th tabindex="0" rowspan="1" colspan="2">Imię</th>
                                <th tabindex="0" rowspan="1" colspan="3">Nazwisko</th>
                                <th tabindex="0" rowspan="1" colspan="4">Lokalizacja</th>
                                <th tabindex="0" rowspan="1" colspan="5">Data zwolnienia</th>
                            </tr>
                        </tfoot> 
                                                
                        <tbody>
                        <tr>
                                <td>Trident</td>
                                <td>Internet
                                     Explorer 4.0</td>
                                <td>Win 95+</td>
                                <td class="center"> 4</td>
                                <td class="center">X</td>
                            </tr>
                            <tr>
                                <td>Trident</td>
                                <td>Internet
                                     Explorer 4.0</td>
                                <td>Win 95+</td>
                                <td class="center"> 4</td>
                                <td class="center">X</td>
                            </tr>
                        </tbody>

                       
                        
                    </table>
        
                    <tiles:insertDefinition name="tiles-pager">
            			<c:set var="pager" value="${activeDirectoryListForm.filter.pager}" scope="request"/>
            		</tiles:insertDefinition>  
        
</tiles:putAttribute>
</tiles:insertDefinition>        