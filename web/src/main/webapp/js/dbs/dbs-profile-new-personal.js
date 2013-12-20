$(function () {
	
    $('#fileupload').fileupload({
        dataType: 'json'
        ,done: function (e, data) {
        			$('#photo').removeClass('error');
        			$('#photo .photo-img img').remove();
        			$('#photo .alert-msg a').remove();
        			$('#photo .alert-msg').prepend('<a href="profile/new/photo/delete" class="close-icon"><i class="icon-remove-sign"></i></a>');
        			$('#photo .photo-img').prepend('<img src="profile/new/photo" class="avatar img-circle"/>');
        			
//        			$('#photo .alert-msg').find('a').on('click', { files: data.files }, function (event) {                        
//                        event.preventDefault();
//        				data.files.length = 0;
//        				data.submit();
//        			});
                }
	    ,fail: function (e, data) {
	    			$('#photo').addClass('error');
	        		$('#photo .alert-msg').prepend('<i class="icon-remove-sign">&nbsp;Problem z zaczytaniem pliku.</i>');
	        	}    
        /*
        ,progressall: function (e, data) {
                var progress = parseInt(data.loaded / data.total * 100, 10);
                $('#progress .bar').css(
                    'width',
                    progress + '%'
                );
            },
     
            dropZone: $('#dropzone')
        */
    });
});    
    
