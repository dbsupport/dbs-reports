$(function () {
    // select2 plugin for select elements
//    $(".multiselect").select2({
//        placeholder: ""
//    });
    
    $('.selectpicker').selectpicker({
    	countSelectedText: 'zaznaczone {0} z {1}' 
    });
    
    
    $('.multiselectall').change(function() {
    	var target = $('#'+$(this).attr('data-target'));
    	
		if (this.checked) target.selectpicker('selectAll');
		else target.selectpicker('deselectAll');
		
    	var counter = $(".multiselectcounter[data-target='"+$(target).attr('id')+"']");
    	counter.val(target.val()!=null?target.val().length:0);
    });
    
    $('.selectpicker').change(function() {
    	var target = $('#'+$(this).attr('id'));
    	
    	var counter = $(".multiselectcounter[data-target='"+$(this).attr('id')+"']");
    	counter.val(target.val()!=null?target.val().length:0);
    	
    	return true;
    });    
});    
    
