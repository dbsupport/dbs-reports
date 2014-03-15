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
	
	$("ul.pager-ctrl").find("li > a").each(function() {
		$(this).click(function() { $.OnSubmit(); });
	});
	
	$("select.pager-ctrl").on('change', function() {
		$.OnSubmit();
		var url = $(this).attr('target')+$(this).val();
		$(location).attr('href',url); 
	});
	
	
	$("a.sorter-ctrl").click(function() { $.OnSubmit(); });
	
});    
    
