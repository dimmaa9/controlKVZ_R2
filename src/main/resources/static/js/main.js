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

function showmass(id) {
    if (!mass.includes(id)) {
        return true;
    } else return false;
}

function addElement(id) {
    if (showmass(id)) {
        let html = '<select class="selectpicker unit" data-size="10" multiple data-actions-box="true" data-live-search="true" required="required" name="_units_' + id + '" id="_units_' + id + '"></select>';
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

let valueScope = null, valueType = null, valueObject = null;

$(document).ready(function () {
    $(document).on('change', '.unit', function () {
        let checkId = $(this).prop('id').split('_')[2] * 1;
        let s = $('#_units_' + checkId).val();

        $.get("/table/units?_units_=" + String(s) + "", function (data) {
            // size data
            let size = 0;
            $.each(data, function () {
                size++;
            });

            if (size > 0) {
                addElement(checkId + 1);
                fillElement(checkId + 1, data);

                let i = 1;
                $('#_tbody_').empty();

                $.get("/table/unit?unit=" + String(s) +
                        "&valueScope=" + String(valueScope) +
                        "&valueType=" + String(valueType) +
                        "&valueObject=" + String(valueObject), function (dataIneger) {
                    if(valueScope != null && valueType != null && valueObject != null){
                        $("#title").text("Укомплектованість 3");
                    }else {
                        $.each(dataIneger, function (key, value) {
                            let option = '<tr>' + '<td class="font-weight-semi-bold">' + i++ + '</td>' +
                                '<td class="font-weight-semi-bold">' + key + '</td>' +
                                '<td class="font-weight-semi-bold">' + value + '%</td>' +
                                '<td class="text-center">' +
                                '<div class="dropdown">' +
                                '<a id="basicTable1MenuInvoker" class="u-icon-sm link-muted" href="#"' +
                                'role="button" aria-haspopup="true" aria-expanded="false"' +
                                'data-toggle="dropdown"' +
                                'data-offset="8">' +
                                '<span class="ti-more"></span>' +
                                '</a>' +
                                '<div class="dropdown-menu dropdown-menu-right" style="width: 150px;">' +
                                '<div class="card border-0 p-3">' +
                                '<ul class="list-unstyled mb-0">' +
                                '<li class="mb-3">' +
                                '<a class="d-block link-dark" href="/table/edit/'  +
                                '">Редагувати</a>' +
                                '</li>' +
                                '<li>' +
                                '<a class="d-block link-dark" href="/table/delete/'  +
                                '">Видалити</a>' +
                                '</li>' +
                                '</ul>' +
                                '</div>' +
                                '</div>' +
                                '</div>' +
                                '</td>' + '</tr>';
                            $('#_tbody_').append(option);
                        });
                    }
                });

            } else if (size === 0) {
                $('#_tbody_').empty();
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

$(document).ready(function () {
    $(document).on('change', '#_scope_', function () {
        let id = $(this).prop('id');
        let s = $('#_scope_').val();


        if (s.toString() == '') {
            valueScope = null;
            valueType = null;
            valueObject = null;
        } else {
            valueScope = s;
            valueType = null;
            valueObject = null;
        }
        $.get("/table/scopes?_scope_=" + s.toString(), function (data) {
            // size data
            let size = 0;
            $.each(data, function () {
                size++;
            });

            if (size > 0) {
                let html = '<select class="selectpicker" data-size="10" data-live-search="true" multiple data-actions-box="true" required="required" name="_type_" id="_type_"></select>';
                $('#selector-div-object').append(html);
                $('#_type_').selectpicker('render');

                $('#_type_').empty();
                $.each(data, function (k, v) {
                    let option = "<option value = " + k + ">" + v + "</option>";
                    $('#_type_').append(option);
                });
                $('#_type_').selectpicker('refresh');


            } else if (size === 0) {
                $('#_type_').selectpicker('destroy');
                $('#_type_').remove();
                $('#_object_').selectpicker('destroy');
                $('#_object_').remove();
            }
        });
    });
});

$(document).ready(function () {
    $(document).on('change', '#_type_', function () {
        let s = $('#_type_').val();
        if (s.toString() == '') {
            valueType = null;
            valueObject = null;
        } else {
            valueType = s;
            valueObject = null;
        }

        $.get("/table/types?_type_=" + s.toString(), function (data) {
            // size data
            let size = 0;
            $.each(data, function () {
                size++;
            });

            if (size > 0) {
                let html = '<select class="selectpicker" data-size="10" data-live-search="true" multiple data-actions-box="true" required="required" name="_object_" id="_object_"></select>';
                $('#selector-div-object').append(html);
                $('#_object_').selectpicker('render');

                $('#_object_').empty();
                $.each(data, function (k, v) {
                    let option = "<option style='max-width: 200px' value = " + k + ">" + v + "</option>";
                    $('#_object_').append(option);
                });
                $('#_object_').selectpicker('refresh');
            } else if (size === 0) {
                $('#_object_').selectpicker('destroy');
                $('#_object_').remove();
            }
        });
    });
});

$(document).ready(function () {
    $(document).on('change', '#_object_', function () {
        let s = $('#_object_').val();
        if (s.toString() == '') {
            valueObject = null;
        } else {
            valueObject = s;
            $('#title').text("Укомплектованість" + s);
        }

    });
});




$(window).load(function () {
    $('#load-spinner').hide();
});

$(document).ajaxStart(function () {
    $('#card-table').hide();
    $('#load-spinner').show();
}).ajaxStop(function () {
    $('#load-spinner').hide();
    $('#card-table').show();
});




