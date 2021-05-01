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

//test




$(document).ready(function () {
    $('#scope').change(function () {
        sendAjaxRequest();
    });
});

function sendAjaxRequest() {
    let s = $("#scope").val();
    if(s ==='null'){
        $('#type').empty();
        $("#type").append("<option value='null'>Нічого не вибрано</option>");
    }else {
        $.get("/object/create/type?scope=" + s, function (data) {
            $('#type').empty();

            let count = 0;
            $.each(data, function (k, v) {
                count++;
            });

            if(count === 0){
                $("#type").append("<option value='null'>Відсутня інформація</option>");
            }else {
                $("#type").append("<option value='null'>Нічого не вибрано</option>");
                $.each(data, function (k, v) {
                    let option = "<option value = " + k + ">" + v + "</option>";
                    $("#type").append(option);
                });
            }
        });
    }
}


$(document).ready(function () {
    $('#type').change(function () {
        sendAjaxRequest2();
    });
});

function sendAjaxRequest2() {
    let s = $("#type").val();
    if(s ==='null'){
        $('#object').empty();
        $("#object").append("<option value='null'>Нічого не вибрано</option>");
    }else {
        $.get("/object/create/object?type=" + s, function (data) {
            $('#object').empty();

            let count = 0;
            $.each(data, function (k, v) {
                count++;
            });

            if(count === 0){
                $("#object").append("<option value='null'>Відсутня інформація</option>");
            }else {
                $("#object").append("<option value='null'>Нічого не вибрано</option>");
                $.each(data, function (k, v) {
                    let option = "<option value = " + k + ">" + v + "</option>";
                    $("#object").append(option);
                });
            }
        });
    }
}

$(document).ready(function () {
    $('input:radio[name="inlineRadioOptions"]').change(function () {
        if ($(this).val() === 'option2') {
            $("#sel1").removeAttr('disabled');
            $("#form").prop("action", "/units/unit/save");
        } else {
            $("#sel1").prop("disabled", true);
            $("#form").prop("action", "/units/unit/saveNull");
        }
    });
});

$(document).ready(function () {
    $(document).on('click', '#dtnSave', function () {
        let element = document.getElementById('div-table');
        html2pdf(element);
    });
});




