$(document).ready(function() {

	$("form.dbs-form input[type=checkbox].checker-ctrl").click(function() {
		var checked = $(this).is(':checked');
		$("form.dbs-form input:checkbox[name='id']").each(function() {
			$(this).prop('checked', checked);
		});
	});
});