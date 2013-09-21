<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<tiles:insertDefinition name="tiles-default" flush="true">
<tiles:putAttribute name="id" type="string">dbs-page-welcome</tiles:putAttribute>
<tiles:putAttribute name="title" type="string">witamy w serwisie</tiles:putAttribute>
<tiles:putAttribute name="content" type="string">

welcome
	
</tiles:putAttribute>
</tiles:insertDefinition>