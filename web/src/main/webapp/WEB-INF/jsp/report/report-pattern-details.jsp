<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<tiles:insertDefinition name="tiles-default" flush="true">
<tiles:putAttribute name="id" type="string">dbs-page-profile-profile</tiles:putAttribute>
<tiles:putAttribute name="title" type="string">profil użytkownika</tiles:putAttribute>
<tiles:putAttribute name="css" type="string"><link rel="stylesheet" href="css/compiled/user-profile.css" type="text/css" media="screen" /></tiles:putAttribute>
<tiles:putAttribute name="content" type="string">

        <div id="pad-wrapper" class="user-profile">
            <!-- header -->
            <div class="row header">
                <div class="col-md-8">
                    <img src="img/contact-profile.png" class="avatar img-circle">
                    <h3 class="name">Alejandra Galván Castillo</h3>
                    <span class="area">Graphic Designer</span>
                </div>
                <a class="btn-flat icon pull-right delete-user" data-toggle="tooltip" title="Delete user" data-placement="top">
                    <i class="icon-trash"></i>
                </a>
                 <a class="btn-flat icon large pull-right edit" href="profile/edit">
                    Edytuj ten profil
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

                        <h6>Ostatnie raporty</h6>
                        <br>
                        <!-- recent orders table -->
                        <table class="table table-hover">
                            <thead>
                                <tr>
                                    <th class="col-md-2">
                                        ID
                                    </th>
                                    <th class="col-md-3">
                                        <span class="line"></span>
                                        Nazwa raportu
                                    </th>
                                    <th class="col-md-3">
                                        <span class="line"></span>
                                        Wielkość
                                    </th>
                                    <th class="col-md-3">
                                        <span class="line"></span>
                                        Data wykonania
                                    </th>
                                </tr>
                            </thead>
                            <tbody>
                                <!-- row -->
                                <tr class="first">
                                    <td>
                                        <a href="report/details">#459</a>
                                    </td>
                                    <td>
                                        Jan 03, 2013
                                    </td>
                                    <td>
                                        2312kB
                                    </td>
                                    <td>
                                        Zeznanie ZUS
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <a href="report/details">#510</a>
                                    </td>
                                    <td>
                                        Feb 22, 2013
                                    </td>
                                    <td>
                                        5787kB
                                    </td>
                                    <td>
                                        Dzienna ilość operacji 
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <a href="report/details">#618</a>
                                    </td>
                                    <td>
                                        Jan 03, 2013
                                    </td>
                                    <td>
                                        8212kB
                                    </td>
                                    <td>
                                        PIT 37
                                    </td>
                                </tr>
                            </tbody>
                        </table>

                        <!-- new comment form -->
                        <div class="col-md-12 section comment">
                            <h6>Krótka notatka do profilu</h6>
                            <p>Dodaj krótką notatkę by o czymś nie zapomnieć.</p>
                            <textarea></textarea>
                            <!-- a href="#">Attach files</a-->
                            <div class="col-md-12 submit-box pull-right">
                                <input type="submit" class="btn-glow primary" value="Dodaj notatkę">
                                <span>&nbsp;</span>
                                <input type="reset" value="Anuluj" class="reset">
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