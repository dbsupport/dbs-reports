<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<tiles:insertDefinition name="tiles-default" flush="true">
<tiles:putAttribute name="id" type="string">dbs-page-report-pattern-import</tiles:putAttribute>
<tiles:putAttribute name="title" type="string">import szablonu raportu</tiles:putAttribute>
<tiles:putAttribute name="css" type="string">
<link rel="stylesheet" href="css/compiled/new-user.css" type="text/css" media="screen" />
<link rel="stylesheet" href="css/compiled/form-wizard.css" type="text/css" media="screen" />
</tiles:putAttribute>
<tiles:putAttribute name="content" type="string">

        <div id="pad-wrapper" class="new-user">
            <div class="row header">
                <div class="col-md-12">
                    <h3>Importuj nowy szablon raportu</h3>
                </div>                
            </div>
            
            <div class="row form-wrapper">
                <!-- left column -->
                <div class="col-md-9 with-sidebar">
                
                    <div id="fuelux-wizard" class="wizard row">
                        <ul class="wizard-steps">
                            <li data-target="#step1" class="active">
                                <span class="step">1</span>
                                <span class="title">Wybierz szablon</span>
                            </li>
                            <li data-target="#step2">
                                <span class="step">2</span>
                                <span class="title">Zapisz szablon</span>
                            </li>
                        </ul>                            
                    </div>                
                
                
                    <div class="container">
                    	<form:form method="post" modelAttribute="reportPatternImportForm" action="report/pattern/import/read" class="new_user_form" enctype="multipart/form-data">
                    		<input type="hidden" name="page" value="1"/>
                    		<spring:bind path="file">
                    		<c:if test="${status.error}"><c:set var="classes">error</c:set></c:if>
                            <div class="col-md-12 field-box ${classes}">
	                        	<label>Szablon:</label>
                                <input type="file" name="file" class="form-control" accept=".zip,.7z"/>
                                <c:if test="${status.error}"><span class="alert-msg"><i class="icon-remove-sign"></i> <c:out value="${status.errorMessage}" escapeXml="false"/></span></c:if>
                            </div>
                            </spring:bind>
                            
                            <div class="col-md-11 field-box actions">
		                        <button type="submit" class="btn-glow primary btn-next" data-last="Finish">Dalej&nbsp;&nbsp;<i class="icon-chevron-right"></i></button><span>&nbsp;</span>
		                        <!-- button type="button" class="btn-glow success btn-finish">
		                            Zapisz
		                        </button-->                            
                            </div>
                        </form:form>
                    </div>
                </div>

                <!-- side right column -->
                <div class="col-md-3 form-sidebar pull-right">
                    <h6>Sidebar text for instructions</h6>
                    <p>Add multiple users at once</p>
                    <p>Choose one of the following file types:</p>
                    <ul>
                        <li><a href="#">Upload a vCard file</a></li>
                        <li><a href="#">Import from a CSV file</a></li>
                        <li><a href="#">Import from an Excel file</a></li>
                    </ul>
                </div>
            </div>
        </div>
        
</tiles:putAttribute>
</tiles:insertDefinition>        