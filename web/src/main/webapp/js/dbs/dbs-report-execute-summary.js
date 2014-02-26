$(function () {
	$('a.report-delete').click(function (event) {
		event.preventDefault();
		var url = $(this).attr('data-url');
		if (confirm('Jesteś pewien, że chcesz usunąć ten raport?')) {		
			window.location = url;
		}
    });
});    
    
