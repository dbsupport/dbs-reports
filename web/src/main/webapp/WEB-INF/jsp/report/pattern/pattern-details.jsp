<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<tiles:insertDefinition name="tiles-default" flush="true">
<tiles:putAttribute name="id" type="string">dbs-page-report-pattern-details</tiles:putAttribute>
<tiles:putAttribute name="title" type="string">definicja raportu</tiles:putAttribute>
<tiles:putAttribute name="css" type="string">
<link rel="stylesheet" href="css/compiled/form-wizard.css" type="text/css" media="screen" />
<link rel="stylesheet" href="css/bootstrap/bootstrap.datepicker.css" type="text/css" >
<link rel="stylesheet" href="css/bootstrap/bootstrap-datetimepicker.min.css" type="text/css" >
<link rel="stylesheet" href="css/dbs/dbs-wizard.css" type="text/css" media="screen" />
<link rel="stylesheet" href="css/compiled/user-profile.css" type="text/css" media="screen" />
<link rel="stylesheet" type="text/css" href="css/bootstrap/bootstrap-select.min.css">
<link rel="stylesheet" href="css/dbs/dbs-profile.css" type="text/css" media="screen" />
</tiles:putAttribute>
<tiles:putAttribute name="js" type="string">
<script src="js/bootstrap.datepicker.js"></script>
<script src="js/bootstrap-datetimepicker.min.js"></script>
<script src="js/locales/bootstrap-datepicker.pl.js"></script>
<script src="js/locales/bootstrap-datetimepicker.pl.js"></script>
<script src="js/dbs/dbs-datepicker.js"></script>
<script type="text/javascript" src="js/bootstrap-select.min.js"></script>
<script src="js/dbs/dbs-multiselect.js"></script>
<script src="js/dbs/dbs-pattern-details.js"></script>
</tiles:putAttribute> 
<tiles:putAttribute name="content" type="string">

        <div id="pad-wrapper" class="user-profile">
            <!-- header -->
            <div class="row header">
                <div class="col-md-1">
                  	<c:choose>
                  	<c:when test="${!empty pattern.creator.photo and pattern.creator.active eq true}"><img src="profile/photo/${pattern.creator.photo.id}" class="img-circle avatar" /></c:when>
                  	<c:otherwise><img src="img/no-img-personal.png" class="img-circle avatar" /></c:otherwise>
                  	</c:choose>
                </div>
                <div class="col-md-5">
                	<h3 class="name">${pattern.name}</h3>
                    <span class="area">${pattern.version}</span><br/>
                    <span class="area">${pattern.accessesAsString}</span><br/>
                    <span class="area">${pattern.author}</span><br/>
                </div>
                
                <div class="col-md-5 pull-right">
                	<sec:authorize access="hasAnyRole('Admin')">
                	<a href="report/pattern/download/${pattern.id}" class="btn-flat success pull-right delete-user"><i class="icon-download-alt"></i>Pobierz</a>
                	</sec:authorize>
                	<a href="report/execute/${pattern.id}" class="btn-flat success pull-right delete-user"><span>&#43;</span>GENERUJ RAPORT</a>
                	<sec:authorize access="hasAnyRole('Admin')">
	                <a href="#" data-url="report/pattern/delete/${pattern.id}" class="btn-flat icon pull-right delete-user pattern-delete" data-toggle="tooltip" title="Skasuj definicję" data-placement="top"><i class="icon-trash"></i></a>
	                </sec:authorize>
	                <a href="report/archived/${pattern.id}" class="btn-flat icon large pull-right edit">Raporty tej definicji</a>
                </div>
            </div>

            <div class="row profile">
                <div class="col-md-9 bio">
                    <div class="profile-box">
                        <div class="section">
                        	<div class="area">Plik: ${pattern.filename}</div>
                            <div class="area">Formaty: ${pattern.formatsAsString}</div>
                            <div class="area">Data importu: <fmt:formatDate value="${pattern.uploadDate}"/></div>
                            <div class="area">Zaimportował: <c:choose><c:when test="${pattern.creator.global}">${pattern.creator.description}</c:when><c:otherwise>${pattern.creator.name}</c:otherwise></c:choose></div>
                        </div>
					</div>
					
					<c:if test="${!empty pattern.form}">
					<div class="profile-box">
                            <div class="row form-wrapper section">
                            <form:form method="post" modelAttribute="reportGenerationForm" action="report/pattern/details" class="dbs-form">
	                    	
	                            <c:if test="${reportGenerationForm.fieldfull}">
	                            <c:forEach var="field" items="${reportGenerationForm.fields}" varStatus="fstatus">
									<tiles:insertDefinition name="${field.tile}">
                                    <c:set var="field" value="${field}" scope="request" />
                                    <c:set var="fieldname" value="fields[${fstatus.index}]" scope="request" />
	                            	</tiles:insertDefinition>
	                            </c:forEach>
	                            </c:if>                    		
	                    		
	                            <div class="wizard-actions">
			                        <button type="submit" class="btn-glow success btn-finish" style="display: inline-block;">Sprawdź formularz</button>
	                            </div>
                        	</form:form>
                            </div>
					</div>
					</c:if>
					
					<div class="profile-box">
						<div class="section">
                        	<h6>Manifest</h6>
                            <div class="">
                                <textarea class="form-control" rows="8" readonly="readonly"><c:out value="${pattern.manifest.text}"/></textarea>
                            </div>
                        </div>
                    </div>
                </div>


                <div class="col-md-3 col-xs-12 address pull-right">
                    <h6>&nbsp;</h6>
                    <ul>
                        <li>&nbsp;</li>
                    </ul>
                </div>
            </div>
        </div>
        
</tiles:putAttribute>
</tiles:insertDefinition>        