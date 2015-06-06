<%@ include file="/WEB-INF/jsp/tiles/common/taglib.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>


        
        <div class="pop-dialog alerts-popup">
            <div class="pointer">
                <div class="arrow"></div>
                <div class="arrow_border"></div>
            </div>
            <div class="body">
                <div class="settings">
                    <a class="close-icon" id="alerts-close"><i class="icon-remove-sign"></i></a>
                    <div class="items">
                        <dbs:alerts/>
                    </div>
                </div>
            </div>
        </div>

