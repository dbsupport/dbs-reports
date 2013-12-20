$(function () {
	$('a.access-delete').click(function (event) {
		event.preventDefault();
		var url = $(this).attr('data-url');
		if (confirm('Jesteś pewien, że chcesz usunąć to uprawnienie raportowe?')) {		
			window.location = url;
		}
    });
});    
    
