$(function () {
    
    $.popupdialogshide = function() {
    	$('.pop-dialog-log').hide();	
    };
    
    $.popupdialogtoogle = function(trigger) {
    	$.popupdialogshide(); 
    	var dialog = trigger.closest('.log').find('.pop-dialog-log');
    	
		if (!dialog.is(":visible")) {
			var rid = dialog.attr('data-report-id');
			$.ajax({
				url: 'report/'+rid+'/logs', 
				timeout: 10000,
				type: "GET",
				success: function(data) {
					$.popupdialogsetup(data); 
				},
				error: function(data) {},
				complete: function() {}
	  	  });    	    			
   		}
  		dialog.toggle();
    };
    
	$.popupdialogsetup = function(data) {
		var items = $(".pop-dialog-log[data-report-id='" + data.rid +"']").find('.body .items');
		$(items).empty();
		
		$.each(data.logs, function (i, log) {
			var icon = '<i class="icon-bolt"></i> &nbsp;błąd';
			if (log.level == 'INFO') icon = '<i class="icon-tasks"></i> &nbsp;info';
			else if (log.level == 'WARNING') icon = '<i class="icon-warning-sign"></i> &nbsp;uwaga';
			
            var item = '<div class="item">'
                +'<span class="title '+log.level.toLowerCase()+'">'+icon+'</span>'
                +'<span class="time"><i class="icon-time"></i>&nbsp;'+log.date+'</span><br/>'
                +'<span class="msg">'+log.msg+'</span>'
                +'</div>';
            
	    	$(items).append(item);
	    });
		
		if (data.logs.length<data.count) {
			$(items).append('<div class="item"><i class="icon-warning-sign"></i>&nbsp;Pełna lista logów zawiera '+data.count+' pozycji.</div>');
		}
		
    	/**
    	 * footer
    	 */
		if (data.logs.length>0) {
			if (data.downloadable) {
				var footer = '<div class="item"><a href="report/'+data.rid+'/logs/download" class="btn-glow primary btn-next"><i class="icon-download"></i>&nbsp;Pobierz szczegóły</a></div>';
				$(items).append(footer);
			}
		} else {
			var footer = '<div class="item"><i class="icon-remove-sign"></i>&nbsp;brak logów</div>';
			$(items).append(footer);
		}
	};

	$('a.pop-dialog-log-trigger').each(function() {
		$(this).on('click', function(e) {
			$.popupdialogtoogle($(this));
		    e.stopPropagation();
		});	
	});
	
	$(document).click(function(e) {
		var self = $(e.target);
		if (self.closest('.pop-dialog-log').length!=0) {
			e.stopPropagation();
			return;
		}
	    $.popupdialogshide(); 	
	});
});