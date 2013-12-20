<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<tiles:insertDefinition name="tiles-default" flush="true">
<tiles:putAttribute name="id" type="string">dbs-page-report-execute-generate</tiles:putAttribute>
<tiles:putAttribute name="title" type="string">generowanie raportu</tiles:putAttribute>
<tiles:putAttribute name="css" type="string">
<link rel="stylesheet" href="css/compiled/new-user.css" type="text/css" media="screen" />
<link rel="stylesheet" href="css/compiled/form-wizard.css" type="text/css" media="screen" />
</tiles:putAttribute>
<tiles:putAttribute name="content" type="string">

        <div id="pad-wrapper" class="new-user">
            <div class="row header">
                <div class="col-md-12">
                    <h3>Generuj raport</h3>
                </div>                
            </div>
            
            <div class="row form-wrapper">
                <!-- left column -->
                <div class="col-md-9 with-sidebar">
                
                    <div id="fuelux-wizard" class="wizard row">
                        <ul class="wizard-steps">
                            <li data-target="#step1">
                                <span class="step">1</span>
                                <span class="title">Ustaw parametry raportu</span>
                            </li>
                            <li data-target="#step2" class="active">
                                <span class="step">2</span>
                                <span class="title">Wygeneruj raport</span>
                            </li>
                            <li data-target="#step3">
                                <span class="step">3</span>
                                <span class="title">Obejrzyj raport</span>
                            </li>                            
                        </ul>                            
                    </div>                  
                
                
                    <div class="step-content">
                    <div class="step-pane active" id="step2">
                    <div class="row form-wrapper">
                    <div class="col-md-12">                
                    	<form:form method="post" modelAttribute="reportGenerationForm" action="report/execute/generate" class="new_user_form">
                    		<input type="hidden" name="page" value="2">
                    	 	<div class="field-box">
                            	<label>Nazwa szablonu:</label>
                            	<div class="col-md-12">
                                	<input class="form-control inline-input" type="text" readonly="readonly" value="${reportGenerationForm.pattern.name}">
                            	</div>
                        	</div>
                    	 	<div class="field-box">
                            	<label>Wersja szablonu:</label>
                            	<div class="col-md-12">
                                	<input class="form-control inline-input" type="text" readonly="readonly" value="${reportGenerationForm.pattern.version}">
                            	</div>
                        	</div>
                    	
                            <div class="col-md-11 field-box actions">
                            	<button type="button" class="btn-glow primary btn-prev" onclick="location.href='report/execute/form'"><i class="icon-chevron-left"></i>&nbsp;Popraw</button><span>&nbsp;</span>
		                        <button type="submit" class="btn-glow success btn-finish">Generuj!</i></button><span>&nbsp;</span>
		                        <!-- button type="submit" class="btn-glow success btn-finish">Generuj!</button-->                            
                            </div>
                        </form:form>
	                </div>
	                </div>
	                </div>
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