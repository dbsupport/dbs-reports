$(function () {
    
    $.popupdialogshide = function() {
    	$('.pop-dialog-log').hide();	
    };
    
    $.popupdialogtoogle = function(trigger) {
    	$.popupdialogshide(); 
    	trigger.closest('.log').find('.pop-dialog-log').toggle();
    };   


	$('a.pop-dialog-log-trigger').each(function() {
		$(this).on('click', function(e) {
			$.popupdialogtoogle($(this));
		    e.stopPropagation();
		});	
//		$(this).closest('.log').find('.close-icon').on('click', function() {
//	        $.popupdialogtoogle($(this));
//	        e.stopPropagation();
//		});
	});
	
	$(document).click(function(e) {
		var self = $(e.target);
		if (self.closest('.pop-dialog-log').length!=0) {
			e.stopPropagation();
			return;
		}
	    $.popupdialogshide(); 	
	});
});