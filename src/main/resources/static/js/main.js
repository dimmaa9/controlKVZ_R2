(function ($) {
    $(document).on('ready', function () {
        // Tooltips & Popovers
        $('[data-toggle="tooltip"]').tooltip();
        $('[data-toggle="popover"]').popover();

        // Dismiss Popovers on next click
        $('.popover-dismiss').popover({
            trigger: 'focus'
        });

        // File Input
        if ($('.custom-file-input').length) {
            bsCustomFileInput.init()
        }

        // Header Search
        $('.u-header-search').each(function () {
            //Variables
            var $this = $(this),
                searchTarget = $this.data('search-target'),
                searchMobileInvoker = $this.data('search-mobile-invoker'),
                windowWidth = window.innerWidth;

            $(searchMobileInvoker).on('click', function (e) {
                $('.dropdown.show [data-toggle="dropdown"]').dropdown('toggle');

                e.stopPropagation();

                $(searchTarget).fadeToggle(400);
            });

            $('[data-toggle="dropdown"]').on('click', function () {
                if ($(searchTarget).is(':visible') && $(searchMobileInvoker).is(':visible')) {
                    $(searchTarget).hide();
                }
            });

            $(searchTarget).on('click', function (e) {
                e.stopPropagation();
            });

            $(window).on('click', function (e) {
                $(searchTarget).fadeOut(200);
            });

            if (windowWidth <= 576) {
                $(window).on('click', function (e) {
                    $(searchTarget).fadeOut(400);
                });
            } else {
                $(window).off('click');
            }

            $(window).on('resize', function () {
                var windowWidth = window.innerWidth;

                if (windowWidth >= 576) {
                    $(searchTarget).attr('style', '');

                    $(window).off('click');
                } else {
                    $(window).on('click', function (e) {
                        $(searchTarget).fadeOut(400);
                    });
                }
            });
        });

        // Custom Scroll
        $('.u-sidebar').mCustomScrollbar({
            scrollbarPosition: 'outside',
            scrollInertia: 150
        });
    });


})(jQuery);

$(function () {
    $(".fold-table tr.view").on("click", function () {
        $(this).toggleClass("open").next(".fold").toggleClass("open");
    });
});

$(document).ready(function() {
    $('#_scope_').change(function() {
        sendAjaxRequest();
    });
});

function sendAjaxRequest() {
    let s = $("#_scope_").val();
    console.log(s);
    $.get( "/type?_scope_=" + s, function( data ) {
        $('#_type_').empty();
        $.each(data, function(k, v) {
                let option = "<option value = " + k + ">" + v +  "</option>";
                $("#_type_").append(option);
        });
    });
}


$(document).ready(function() {
    $('#_type_').change(function() {
        sendAjaxRequest2();
    });
});

function sendAjaxRequest2() {
    let s = $("#_type_").val();
    console.log(s);
    $.get( "obj?_type_=" + s, function( data ) {
        $('#_object_').empty();
        $.each(data, function(k, v) {
            let option = "<option value = " + k + ">" + v +  "</option>";
            $("#_object_").append(option);
        });
    });
}

