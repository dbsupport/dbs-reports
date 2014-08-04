$(function () {
	   
	   $.setupNotifications = function(data) {
		    $('#new-reports .pop-dialog .body .notifications').empty();

		    /**
		     * header: newly
		     */
	    	var header = 'Ostatnio nie zakończyła się żadna generacja.';
	    	
	    	if (data.reports==1) {
	    		header = 'Zakończono generację jednego raportu!';
	    	} else if (data.reports>1) { 
	    		header = 'Zakończono generację '+data.reports+' raportów!';
	    	}			

	    	$('#new-reports .pop-dialog .body .notifications').append('<h3>'+header+'</h3>');
	    	$('#new-reports a.trigger').attr('title', header);
    		
	    	/**
	    	 * counter
	    	 */
	    	$('#new-reports a .count').text(data.reports);
	    	
	    	/**
	    	 * construct orders list 
	    	 */
	    	$.each(data.orders, function (i, order) {
	    		var item = '<a href="report/unarchived/order/'+order.reports+'" data-id="'+order.id+'" class="item"><i class="icon-signin"></i> '+order.name+' <span class="time"><i class="icon-time"></i> '+order.time+'</span></a>';	    	    		
	    		$('#new-reports .pop-dialog .body .notifications').append(item);
	    	});
	    	
	    	/**
	    	 * footer
	    	 */
    		var footer = '<div class="footer"><a href="report/unarchived" class="logout">Zobacz wszystkie</a></div>';
    		$('#new-reports .pop-dialog .body .notifications').append(footer);
    		
    		/**
    		 * audio
    		 */
    		if (data.brandnew>0) {
    			$('#new-reports #audio')[0].play();   
    		}
	   };
	   
	   (function worker() {
	 	  $.ajax({
	  	    url: 'report/order/notications', 
	  	    timeout: 10000,
	  	    type: "GET",
	  	    success: function(data) {
	  	    	$.setupNotifications(data); 
	  	    	setTimeout(worker, 10000);
	    		if (data.brandnew>0) {
	    			$.event.trigger({
	    				type: "notification-brandnew",
	    				time: new Date()
	    			});
	    		}	  	    	
	  	    },
	  	    error: function(data) {
	  	    },
	  	    complete: function() {
	  	    }
	  	  });
	   })();
	
});    
    
