<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<tiles:insertDefinition name="tiles-default" flush="true">
<tiles:putAttribute name="id" type="string">dbs-page-report-pattern-details</tiles:putAttribute>
<tiles:putAttribute name="title" type="string">definicja raportu</tiles:putAttribute>
<tiles:putAttribute name="css" type="string"><link rel="stylesheet" href="css/compiled/user-profile.css" type="text/css" media="screen" /></tiles:putAttribute>
<tiles:putAttribute name="content" type="string">

        <div id="pad-wrapper" class="user-profile">
            <!-- header -->
            <div class="row header">
                <div class="col-md-1">
                    <img src="img/contact-profile.png" class="avatar img-circle">
                </div>
                <div class="col-md-5">
                <h3 class="name">${pattern.name}</h3>
                    <span class="area">${pattern.version}</span><br/>
                    <span class="area">${pattern.accessesAsString}</span><br/>
                    <span class="area">${pattern.author}</span><br/>
                </div>
                
                <a href="report/archives/${pattern.id}" class="btn-flat icon large pull-right edit">
                    Pokaż raporty
                </a>             
                   
                <a class="btn-flat icon pull-right delete-user" data-toggle="tooltip" title="Skasuj definicję" data-placement="top">
                    <i class="icon-trash"></i>
                </a>
            </div>

            <div class="row profile">
                <!-- bio, new note & orders column -->
                <div class="col-md-9 bio">
                    <div class="profile-box">
                        <!-- biography -->
                        <div class="col-md-12 section">
                            <h6>Notatki</h6>
                            <p>There are many variations of passages of Lorem Ipsum available but the majority have humour suffered alteration in believable some formhumour , by injected humour, or randomised words which don't look even slightly believable. </p>
                        </div>

                        <h6>Manifest</h6>
                        <br>

                        <div class="">
                            <div class="col-md-12">
                                <textarea class="form-control" rows="8" readonly="readonly"><c:out value="${pattern.manifest}"/></textarea>
                            </div>
                        </div>

                    </div>
                </div>

                <!-- side address column -->
                <div class="col-md-3 col-xs-12 address pull-right">
                    <h6>Adres</h6>
                    <!-- iframe width="300" height="133" frameborder="0" scrolling="no" marginheight="0" marginwidth="0" src="https://maps.google.com.mx/?ie=UTF8&amp;t=m&amp;ll=19.715081,-155.071421&amp;spn=0.010746,0.025749&amp;z=14&amp;output=embed"></iframe-->
                    <ul>
                        <li>2301 East Lamar Blvd. Suite 140. </li>
                        <li>City, Arlington. United States,</li>
                        <li>Zip Code, TX 76006.</li>
                        <li class="ico-li">
                            <i class="ico-phone"></i>
                            1817 274 2933
                        </li>
                         <li class="ico-li">
                            <i class="ico-mail"></i>
                            <a href="#">alejandra@detail.com</a>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
        
</tiles:putAttribute>
</tiles:insertDefinition>        