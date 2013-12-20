<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<tiles:insertDefinition name="tiles-default" flush="true">
<tiles:putAttribute name="id" type="string">dbs-page-report-pattern-details</tiles:putAttribute>
<tiles:putAttribute name="title" type="string">definicja raportu</tiles:putAttribute>
<tiles:putAttribute name="css" type="string">
<link rel="stylesheet" href="css/compiled/user-profile.css" type="text/css" media="screen" />
<link rel="stylesheet" href="css/dbs/dbs-profile.css" type="text/css" media="screen" />
</tiles:putAttribute>
<tiles:putAttribute name="js" type="string">
<script src="js/dbs/dbs-pattern-details.js"></script>
</tiles:putAttribute> 
<tiles:putAttribute name="content" type="string">

        <div id="pad-wrapper" class="user-profile">
            <!-- header -->
            <div class="row header">
                <div class="col-md-1">
                   	<c:if test="${!empty pattern.creator.photo}">
						<img src="profile/photo/${pattern.creator.photo.id}" class="img-circle avatar" />
                   	</c:if>
					<c:if test="${empty pattern.creator.photo}">
                    	<img src="img/no-img-personal.png" class="img-circle avatar" />
                    </c:if>
                </div>
                <div class="col-md-5">
                	<h3 class="name">${pattern.name}</h3>
                    <span class="area">${pattern.version}</span><br/>
                    <span class="area">${pattern.accessesAsString}</span><br/>
                    <span class="area">${pattern.author}</span><br/>
                </div>
                
                <div class="col-md-5 pull-right">
                	<a href="report/execute/${pattern.id}" class="btn-flat success pull-right delete-user"><span>&#43;</span>GENERUJ RAPORT</a>
	                <a href="#" data-url="report/pattern/delete/${pattern.id}" class="btn-flat icon pull-right delete-user pattern-delete" data-toggle="tooltip" title="Skasuj definicję" data-placement="top"><i class="icon-trash"></i></a>
	                <a href="report/archives/${pattern.id}" class="btn-flat icon large pull-right edit">Pokaż raporty</a>             
                </div>
            </div>

            <div class="row profile">

                <div class="col-md-9 bio">
                    <div class="profile-box">
                        <div class="col-md-12 section">
                            <h6>Szczegóły</h6>
                            <p>Formaty: ${pattern.formatsAsString}</p>
                            <p>Data importu: <fmt:formatDate value="${pattern.uploadDate}"/></p>
                            <p>Zaimportował: ${pattern.creator.name}</p>
                            <c:if test="${!empty pattern.form}"><p>Formularz: ${pattern.form.name}</p></c:if>
                            
                        </div>
					</div>
					<div class="profile-box">
						<div class="col-md-12 section">
                        	<h6>Manifest</h6>
                            <div class="col-md-12">
                                <textarea class="form-control" rows="8" readonly="readonly"><c:out value="${pattern.manifest.text}"/></textarea>
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