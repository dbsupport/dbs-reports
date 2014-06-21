<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<tiles:insertDefinition name="tiles-tables" flush="true">
<tiles:putAttribute name="id" type="string">dbs-page-report-execute-summary</tiles:putAttribute>
<tiles:putAttribute name="title" type="string">Zakończenie generacji raportu</tiles:putAttribute>
<tiles:putAttribute name="steps" type="string">Ustaw parametry raportu;Wygeneruj raport;Obejrzyj raport</tiles:putAttribute>
<tiles:putAttribute name="step" type="string">3</tiles:putAttribute>
<tiles:putAttribute name="css" type="string">
<link rel="stylesheet" href="css/compiled/new-user.css" type="text/css" media="screen" />
<link rel="stylesheet" href="css/compiled/form-wizard.css" type="text/css" media="screen" />
<link rel="stylesheet" href="css/dbs/dbs-wizard.css" type="text/css" media="screen" />
</tiles:putAttribute>
<tiles:putAttribute name="js" type="string">
<script src="js/dbs/dbs-report-execute-summary.js"></script>
<script src="js/dbs/dbs-tables-checker.js"></script>
</tiles:putAttribute> 
<tiles:putAttribute name="content" type="string">

                <%@ include file="/WEB-INF/jsp/report/report-unarchived-ordered.jsp" %>
			            
						<form:form method="post" modelAttribute="reportGenerationForm" action="report/execute/summary" class="dbs-form">
                    		<input type="hidden" name="page" value="3">
                    		
	                        <div class="wizard-actions">
	                         	<button type="button" class="btn-glow primary btn-prev" onclick="location.href='report/execute/${pattern.id}'">Zacznij od nowa</button><span>&nbsp;</span>
	                        </div>
                        </form:form>
			                                	
        
</tiles:putAttribute>
</tiles:insertDefinition>        