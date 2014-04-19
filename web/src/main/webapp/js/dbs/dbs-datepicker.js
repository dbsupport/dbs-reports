$(function () {
	$('.input-datepicker').datepicker({
		language: "pl"
		,todayBtn: "linked"
		,todayHighlight: true
		,autoclose: true
	});	
	
	$('.input-datetimepicker').datetimepicker({
		language: "pl"
		,todayBtn: "linked"
		,todayHighlight: true
		,autoclose: true
		,minuteStep: 1
	});	
}); 
    
