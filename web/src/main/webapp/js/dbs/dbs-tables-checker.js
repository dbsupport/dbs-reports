$(document).ready(function() {

	$("input[type=checkbox].checker-ctrl").click(function() {
		var checked = $(this).is(':checked');
		$("input:checkbox[name='id']").each(function() {
			$(this).prop('checked', checked);
		});
	});
});