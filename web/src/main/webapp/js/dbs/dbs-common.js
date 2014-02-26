$(function () {
	$.OnSubmit = function() {
	    $('#main-container').css('opacity', 0.5);
	    $('#main-loading').css('display', 'block');
	};
	
	$("form.dbs-form").submit(function (e) {
	    e.preventDefault();
	    $.OnSubmit();
	    this.submit();
	});	
});    
    
