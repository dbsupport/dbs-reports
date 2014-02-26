<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<tiles:insertDefinition name="tiles-default" flush="true">
<tiles:putAttribute name="id" type="string">dbs-page-profile-encoding</tiles:putAttribute>
<tiles:putAttribute name="title" type="string">Encoding</tiles:putAttribute>
<tiles:putAttribute name="css" type="string">
<link rel="stylesheet" href="css/compiled/user-profile.css" type="text/css" media="screen" />
<link rel="stylesheet" href="css/dbs/dbs-profile.css" type="text/css" media="screen" />
<link rel="stylesheet" href="css/dbs/dbs-wizard.css" type="text/css" media="screen" />
</tiles:putAttribute>
<tiles:putAttribute name="js" type="string">
<script src="js/dbs/dbs-profile.js"></script>
</tiles:putAttribute> 
<tiles:putAttribute name="content" type="string">

        <div id="pad-wrapper" class="user-profile">
            <!-- header -->
            <div class="row header">
                <div class="col-md-12">
                
            		<form:form method="post" modelAttribute="profileEncodingForm" action="profile/encoding" class="dbs-form">
            		
					<div class="field-box">
                            <label>Kodowania wejściowe (;)</label>            		
                    		<spring:bind path="inencodings">
                    		<div class="field-box">
                                <form:input path="inencodings" cssClass="form-control" placeholder="inencoding"/>
                            </div>
                            </spring:bind>
                   	</div>            		
					<div class="field-box">
                            <label>Kodowanie wyjściowe (;)</label>            		
                    		<spring:bind path="outencodings">
                    		<div class="field-box">
                                <form:input path="outencodings" cssClass="form-control" placeholder="outencoding"/>
                            </div>
                            </spring:bind>
                   	</div>  
					<div class="field-box">
                            <label>Uuid</label>            		
                    		<spring:bind path="uuid">
                    		<div class="field-box">
                                <form:input path="uuid" cssClass="form-control" placeholder="uuid"/>
                            </div>
                            </spring:bind>
                   	</div>                    	          		
            		
	
					    
                    <div class="wizard-actions">
                    	<button type="button" class="btn-glow" onclick="location.href='profile/encoding/reset/in'"><i class="icon-refresh"></i>&nbsp;Resetuj wejściowe</button><span>&nbsp;</span>
                    	<button type="button" class="btn-glow" onclick="location.href='profile/encoding/reset/out'"><i class="icon-refresh"></i>&nbsp;Resetuj wyjściowe</button><span>&nbsp;</span>
                    	<button type="submit" class="btn-glow success btn-finish" style="display: inline-block;">Testuj</button><span>&nbsp;</span>
                    </div> 					    
					    
					</form:form>
                
                </div>
                
                    <table class="table table-hover">
                        <thead>
                            <tr>
                                <th class="col-md-3 sortable">
                                <tiles:insertDefinition name="tiles-sorter"><span class="line"></span>
                                Kodowanie Wejściowe
            					</tiles:insertDefinition>                                  
                                </th>
                                <th class="col-md-2 sortable"><span class="line"></span>
                                Kodowanie Wejściowe
                                </th>
                                <th class="col-md-2 sortable"><span class="line"></span>Wartość
                                </th>
                            </tr>
                        </thead>
                        <tbody>

						<c:forEach items="${profiles}" var="profile" varStatus="rstatus">                        
                        <!-- row -->
                        <tr>
                       
                            <td>${profile.inencoding}</td>
                            <td>${profile.outencoding}</td>                             
                            <td>${profile.value}</td>
                        </tr>
                        </c:forEach>
                        
                        </tbody>
                    </table>                
                
                
                
             </div>

        </div>
        
</tiles:putAttribute>
</tiles:insertDefinition>        