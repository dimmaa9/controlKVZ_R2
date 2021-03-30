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

$(document).ready(function () {
    $('#_scope_').change(function () {
        sendAjaxRequest();
    });
});

function sendAjaxRequest() {
    let s = $("#_scope_").val();
    console.log(s);
    $.get("/type?_scope_=" + s, function (data) {
        $('#_type_').empty();
        $.each(data, function (k, v) {
            let option = "<option value = " + k + ">" + v + "</option>";
            $("#_type_").append(option);
        });
    });
}


//test

function showmass(id) {
    if (!mass.includes(id)) {
        return true;
    } else return false;
}

function addElement(id) {
    if (showmass(id)) {
        let html = '<select class="selectpicker" multiple data-actions-box="true" required="required" name="_units_' + id + '" id="_units_' + id + '"></select>';
        $('#selector-div').append(html);
        $('#_units_' + id).selectpicker('render');
        mass.push(id);
    }
}

function fillElement(id, data) {
    $('#_units_' + id).empty();
    $.each(data, function (k, v) {
        let option = "<option value = " + k + ">" + v + "</option>";
        $('#_units_' + id).append(option);
    });
    $('#_units_' + id).selectpicker('refresh');
}


let mass = [1];

$(document).ready(function () {

    $(document).on('change', '.selectpicker', function () {
        let checkId = $(this).prop('id').split('_')[2] * 1;
        let s = $('#_units_' + checkId).val();


        if (s == undefined) {
            s = '';
        }
        console.log(s.toString());

        $.get("/table/units?_units_=" + s.toString(), function (data) {
            // size data
            let size = 0;
            $.each(data, function () {
                size++;
            });

            if (size > 0) {
                addElement(checkId + 1);
                fillElement(checkId + 1, data);
            } else if (size === 0) {
                $.each(mass, function (k, v) {
                    if (v > checkId) {
                        $('#_units_' + v).selectpicker('destroy');
                        $('#_units_' + v).remove();
                    }
                });
                mass.splice(checkId, mass[mass.length - 1] - checkId);

                console.log('mass = ' + mass.toString());
            }

        });

    });
});



