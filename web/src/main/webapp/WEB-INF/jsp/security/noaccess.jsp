<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<tiles:insertDefinition name="tiles-default" flush="true">
<tiles:putAttribute name="id" type="string">dbs-page-noaccess</tiles:putAttribute>
<tiles:putAttribute name="title" type="string">Brak dostępu</tiles:putAttribute>
<tiles:putAttribute name="css" type="string">
<link rel="stylesheet" href="css/compiled/user-profile.css" type="text/css" media="screen" />
<link rel="stylesheet" href="css/dbs/dbs-profile.css" type="text/css" media="screen" />
</tiles:putAttribute>
<tiles:putAttribute name="content" type="string">

        <div id="pad-wrapper" class="user-profile">
            <!-- header -->

            <div class="row profile">
                <!-- bio, new note & orders column -->
                <div class="col-md-9 bio">
                    <div class="profile-box">
                        <!-- biography -->
                        <div class="col-md-12 section">
                            <h6>Brak dostępu</h6> 
                            <p>Nie posiadasz dostępu do tej strony!<br/>Zweryfikuj poziom uprawnień lub skontaktuj się z administratorem.</p>
                        </div>
                    </div>
                </div>

                <!-- side address column -->
                <div class="col-md-3 col-xs-12 address pull-right">

                </div>
            </div>
        </div>
        
</tiles:putAttribute>
</tiles:insertDefinition>        