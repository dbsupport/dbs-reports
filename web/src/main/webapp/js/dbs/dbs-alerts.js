$(function () {

    if ($('.alerts-popup')) {
        $('#alerts-close').click(function (e) {
            $('.alerts-popup').hide();
            e.stopPropagation();
        });

        if ($('.alerts-popup .body .settings .items').has( ".item" ).length > 0) {
            $('.alerts-popup').show();
        }
    }


});