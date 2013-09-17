<%@ include file="/WEB-INF/jsp/tiles/taglib.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>


<div class="eb-dashed">
    <h1><sec:authentication property="principal.firstname"/> <sec:authentication property="principal.surename"/></h1> 
</div>

<ul class="nav nav-pills nav-stacked" id="eb-menu">
	<li class="divider"></li>
	<li id="eb-page-transfers-accept"><a href="transfers/init/acceptance.ebk" onclick="return EB.OnWait();">przelewy do akceptacji</a></li>
	<li id="eb-page-transfer"><a href="transfer/init/transfer.ebk" onclick="return EB.OnWait();">zlecenie przelewu</a></li>
	<li id="eb-page-transfers"><a href="transfer/init/transfers.ebk" onclick="return EB.OnWait();">historia operacji</a></li>
	<li class="divider"></li>
	<li id="eb-page-password-change"><a href="password_change.ebk" onclick="return EB.OnWait();">zmiana hasła</a></li>
</ul>

