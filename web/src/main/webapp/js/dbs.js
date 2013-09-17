/**
 * (c) dbs
 * 
 * Requires jquery
 */

EB = {};
/**
 * On submit form.
 */
EB.OnSubmit = function(form) {
	EB.OnWait();
	return true;
}

/**
 * Disable content, show modal.
 */
EB.OnWait = function() {
	var css = {
			top: $("#eb-main").position().top,
			left: $("#eb-main").position().left,
			width: '100%',
			height: $("#eb-main").height()};
	$('#eb-cover').css(css);
	$('#eb-cover').show();
	
	$('#eb-wait').modal({
		  keyboard: false,
		  backdrop:false
	});
}
/**
 * Activate current menu item.
 */
EB.OnMenu = function() {
	//..activate menu item..
	var li = '#eb-menu li#'+$(document.body).attr('id');
	if (li!=null&&li!=undefined) $(li).addClass('active');
}

/**
 * Setup account data.
 * Data is held in $('#'+name+' option:selected') and has structure:
 * {owner:'', assets:''}
 * Id owner has value 'disabled' owner field wont be changed.
 */
EB.OnAccount = function(name) {
	var title = $('#'+name+' option:selected').attr('title') != undefined? 
			   $('#'+name+' option:selected').attr('title'):"{'owner':'', 'assets':'0.00'}";
	
	var data = jQuery.parseJSON(title.replace(/'/g, '"'));
	if ($('#'+name+'-name')!=undefined&&"disabled"!=data.owner) $('#'+name+'-name').val(data.owner);
	if ($('#'+name+'-assets')!= undefined) $('#'+name+'-assets').text(data.assets+" PLN");	
}

/**
 * Disable for a while..
 */
EB.DisableFor = function(el, time) {
	var qname = 'disqueue';
    el.queue(qname, function () {
        el.attr('disabled', 'disabled');
        setTimeout( function () {
            el.dequeue(qname);
        }, time || 3000);
    })
    .queue(qname, function () {
        el.removeAttr('disabled');
    })
    .dequeue(qname);	
}

/**
 * On page loaded. 
 */
$(document).ready(function() {
	EB.OnMenu();
});

