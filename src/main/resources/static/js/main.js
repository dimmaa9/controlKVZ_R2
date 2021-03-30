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

let mass = [1];

function showmass(id) {
    if (!mass.includes(id)) {
        return true;
    } else return false;
}

function addElement(id) {
    if (showmass(id)) {
        let html = '<select class="selectpicker" multiple title="Choose" required="required" name="_units_' + id + '" id="_units_' + id + '"></select>';
        $('#selector-div').append(html);
        $('#_units_' + id).selectpicker('render');
        mass.push(id);
    }
}

function returnData(id) {
    let d;
    let value = $('#_units_' + id).val();
    $.get("/table/units?_units_=" + value.toString(), function (data) {
        d = data;
    });
    console.log(d)
    return d;
}

function fillElement(id, data) {
    $('#_units_' + id).empty();
    $.each(data, function (k, v) {
        let option = "<option value = " + k + ">" + v + "</option>";
        $('#_units_' + id).append(option);
    });
    $('#_units_' + id).selectpicker('refresh');
}

function showElement(id) {
    $('#_units_' + id).selectpicker('show');
}

function hideElement(id) {
    $('#_units_' + id).selectpicker('hide');
}


$(document).ready(function () {
    $('.selectpicker').change(function () {

        let data = returnData(mass[v]);
        let size = 0;
        $.each(data, function () {
            size++;
        });

        if (size > 0) {
            addElement(mass[v]);
            showElement(mass[v]);
            fillElement(mass[v], data);

        } else if (size === 0) {
            hideElement(mass[v]);
        }
    });


    //auto(mass[0]);

    //let idCounter = 2;
    //
    // $('#_units_1').change(function () {
    //     let s = $('#_units_1').val();
    //
    //     if (idCounter === 2) {
    //         let html = '<select class="selectpicker" multiple title="Choose" required="required" name="_units_' + idCounter + '" id="_units_' + idCounter + '"></select>';
    //         $('#selector-div').append(html);
    //         $('#_units_' + idCounter).selectpicker('render');
    //         idCounter++;
    //     }
    //
    //     if (s.toString() === "") {
    //         $('#_units_2').selectpicker('hide');
    //     } else {
    //         $('#_units_2').selectpicker('show');
    //     }
    //
    //     $.get("/table/units?_units_=" + s.toString(), function (data) {
    //         console.log(data);
    //
    //         $("#_units_2").empty();
    //
    //         $.each(data, function (k, v) {
    //             let option = "<option value = " + k + ">" + v + "</option>";
    //             $('#_units_2').append(option);
    //         });
    //
    //         $("#_units_2").selectpicker('refresh');
    //     });
    // });
});

// $(document).ready(function () {
//     $('#_units_1').change(function () {
//
//         let s = $("#_units_1").val();
//         console.log(s.toString());
//
//         $.get("/table/units?_units_=" + s.toString(), function (data) {
//
//             $("#_units_2").empty();
//
//             $.each(data, function (k, v) {
//                 let option = "<option value = " + k + ">" + v + "</option>";
//                 $('#_units_2').append(option);
//             });
//
//             $('.selectpicker').selectpicker('refresh');
//         });
//
//     });
// });



