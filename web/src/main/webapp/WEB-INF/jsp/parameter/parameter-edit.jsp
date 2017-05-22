<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<tiles:insertDefinition name="tiles-wizard" flush="true">
<tiles:putAttribute name="id" type="string">dbs-page-parameter-edit</tiles:putAttribute>
<tiles:putAttribute name="title" type="string">Edycja parametrów aplikacji</tiles:putAttribute>
<tiles:putAttribute name="css" type="string">
<link rel="stylesheet" href="css/compiled/new-user.css" type="text/css" media="screen" />
<link rel="stylesheet" href="css/compiled/form-wizard.css" type="text/css" media="screen" />
<link rel="stylesheet" href="css/dbs/dbs-wizard.css" type="text/css" media="screen" />
</tiles:putAttribute>

<tiles:putAttribute name="js" type="string">
</tiles:putAttribute>

<tiles:putAttribute name="content" type="string">

    <div class="row">
                    <div class="col-md-8 column">
						<h4>Parametry bazy danych</h4>
						<br/><br/>

                        <jsp:include page="parameter-edit-form.jsp" >
                            <jsp:param name="form" value="parametersdbeditform"/>
                            <jsp:param name="action" value="param/edit/db"/>
                        </jsp:include>

                    </div>


                    <div class="col-md-8 column">
                        <br/><br/><br/><br/><br/>
                        <h4>Parametry FTP</h4>
                        <br/><br/>

                        <jsp:include page="parameter-edit-form.jsp" >
                            <jsp:param name="form" value="parametersftpeditform"/>
                            <jsp:param name="action" value="param/edit/ftp"/>
                        </jsp:include>

                    </div>
    </div>

                    <div class="col-md-8 column">
                        <br/><br/><br/><br/><br/>
                        <h4>Parametry SSH</h4>
                        <br/><br/>

                        <jsp:include page="parameter-edit-form.jsp" >
                            <jsp:param name="form" value="parametersssheditform"/>
                            <jsp:param name="action" value="param/edit/ssh"/>
                        </jsp:include>

                    </div>

                    <div class="col-md-8 column">
                        <br/><br/><br/><br/><br/>
                        <h4>Parametry aplikacyjne</h4>
                        <br/><br/>

                        <jsp:include page="parameter-edit-form.jsp" >
                            <jsp:param name="form" value="parametersappeditform"/>
                            <jsp:param name="action" value="param/edit/app"/>
                        </jsp:include>

                    </div>

</tiles:putAttribute>
</tiles:insertDefinition>        