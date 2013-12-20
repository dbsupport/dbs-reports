$(function () {
    $('#fileupload').fileupload({
        dataType: 'json'
        ,done: function (e, data) {
        			$('#photo').removeClass('error');
        			$('#photo .photo-img img').remove();
        			$('#photo .alert-msg a').remove();
        			$('#photo .alert-msg').prepend('<a href="profile/edit/photo/delete" class="close-icon"><i class="icon-remove-sign"></i></a>');
        			$('#photo .photo-img').prepend('<img src="profile/edit/photo" class="avatar img-circle"/>');
                }
	    ,fail: function (e, data) {
	    			$('#photo').addClass('error');
	        		$('#photo .alert-msg').prepend('<i class="icon-remove-sign">&nbsp;Problem z zaczytaniem pliku.</i>');
	        	}    
    });
});    
    
