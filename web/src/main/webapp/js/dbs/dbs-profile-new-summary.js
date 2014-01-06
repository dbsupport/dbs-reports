$(function () {
	
	var slider = $('.slider-button');
    slider.click(function() {
    	var field = $('input[name="accepted"]');
    	if (field!=null) {
	        if ($(this).hasClass("on")) {
	            $(this).removeClass('on').html($(this).data("off-text"));
	            field.val('false');
	        } else {
	            $(this).addClass('on').html($(this).data("on-text"));
	            field.val('true');
	        }
    	}
    });

    slider.init = function() {
    	var field = $('input[name="accepted"]');
    	if (field!=null) {
    		if (field.val() == 'true') {
    			$(this).addClass('on').html($(this).data("on-text"));
    		} else {
    			$(this).removeClass('on').html($(this).data("off-text"));
    		}
    	}
    };
    
    slider.init();
});    
    
