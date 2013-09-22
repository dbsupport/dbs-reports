/**
 * (c) dbs
 * 
 * Requires jquery
 */

DBS = {};
/**
 * Activate current menu item.
 */
DBS.OnMenu = function() {
	//..activate menu item..
	var li = $('#sidebar-nav submenu li#'+$(document.body).attr('id')+'-sidebar');
	var a = $('#sidebar-nav submenu li#'+$(document.body).attr('id')+'-sidebar a');
	a.addClass('active');
	li.parent().addClass('active');
	li.parent().parent().addClass('active');
};



/**
 * Init page
 */
DBS.OnMenu();


