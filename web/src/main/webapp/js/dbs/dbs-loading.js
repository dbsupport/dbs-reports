$(function () {
	$.OnSubmit = function() {
	    $('#main-container').css('opacity', 0.5);
	    $('#main-loading').css('display', 'block');
	    
	    (function worker() {
	    	  $.ajax({
	    	    url: 'ping', 
	    	    timeout: 5000,
	    	    success: function(data) {
	    	      //console.log('success');
	    	      setTimeout(worker, 60000);
	    	    },
	    	    error: function(data) {
	    	    	//console.log('fail');
	    	    	$(location).attr('href', 'security/noaccess');
	    	    },
	    	    complete: function() {
	    	    }
	    	  });
	    })();	    
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
    
