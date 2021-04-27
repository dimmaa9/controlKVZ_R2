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
        }

    });
});

$(document).ready(function () {
    $(document).on('click', '#btnAll', function () {
        let s = $('#_units_' + mass[0]).val();
        // если всё пустое
        if (mass.length === 1 && valueScope == null && valueType == null && valueObject == null && s.toString() === '') {
            $('#div-table').empty();
        } else if (valueScope == null && valueType == null && valueObject == null) {
            //пустая техника
            let unitsVal = "";
            if ($('#_units_' + mass[mass.length - 1]).val().toString() === '' && mass.length > 1) {
                unitsVal = $('#_units_' + mass[mass.length - 2]).val().toString();
            } else if (mass.length === 0 && $('#_units_' + mass[0]).val().toString() != '') {
                unitsVal = $('#_units_' + mass[0]).val().toString();
            } else {
                unitsVal = $('#_units_' + mass[mass.length - 1]).val().toString();
            }
            let str = "/table/filterUnit?unit=" + unitsVal;
            $.get(str, function (data) {
                $('#div-table').empty();

                let html = "<div>" +
                    "                <div class=\"card mb-5\">" +
                    "                    <header class=\"card-header\">" +
                    "                        <h2 class=\"h4 card-header-title\">Укомплектованість</h2>" +
                    "                    </header>" +
                    "                    <div class=\"card-body pt-0\">" +
                    "                        <div class=\"table-responsive\">" +
                    "                            <table class=\"table table-hover mb-0 fold-table\">" +
                    "                                <thead>" +
                    "                                <tr>" +
                    "                                    <th>#</th>" +
                    "                                    <th>Найменування</th>" +
                    "                                    <th>Укомплектованість</th>" +
                    "                                </tr>" +
                    "                                </thead>" +
                    "                                <tbody id='tbody'>" +
                    "                                </tbody>" +
                    "                            </table>" +
                    "                        </div>" +
                    "                    </div>" +
                    "                </div>" +
                    "            </div>";
                $('#div-table').append(html);

                let i = 1;
                $.each(data, function (k, v){
                    let tr = '<tr>' + '<td class="font-weight-semi-bold">' + i++ + '</td>' +
                        '<td class="font-weight-semi-bold">' + k + '</td>' +
                        '<td class="font-weight-semi-bold">' + v + '%</td>' +
                        '</tr>';
                    $('tbody').append(tr);
                });

            });
        } else if (mass.length === 1 && s.toString() === '') {
            let str = "/table/filterObject?" +
                "valueScope=" + valueScope +
                "&valueType=" + valueType +
                "&valueObject=" + valueObject;
            $.get(str, function (data) {
                $('#div-table').empty();

                let html = "<div>" +
                    "                <div class=\"card mb-5\">" +
                    "                    <header class=\"card-header\">" +
                    "                        <h2 class=\"h4 card-header-title\">Укомплектованість</h2>" +
                    "                    </header>" +
                    "                    <div class=\"card-body pt-0\">" +
                    "                        <div class=\"table-responsive\">" +
                    "                            <table class=\"table table-hover mb-0 fold-table\">" +
                    "                                <thead>" +
                    "                                <tr>" +
                    "                                    <th>#</th>" +
                    "                                    <th>Найменування</th>" +
                    "                                    <th>Укомплектованість</th>" +
                    "                                </tr>" +
                    "                                </thead>" +
                    "                                <tbody id='tbody'>" +
                    "                                </tbody>" +
                    "                            </table>" +
                    "                        </div>" +
                    "                    </div>" +
                    "                </div>" +
                    "            </div>";
                $('#div-table').append(html);

                let i = 1;
                $.each(data, function (k, v){
                    let tr = '<tr>' + '<td class="font-weight-semi-bold">' + i++ + '</td>' +
                        '<td class="font-weight-semi-bold">' + k + '</td>' +
                        '<td class="font-weight-semi-bold">' + v + '%</td>' +
                        '</tr>';
                    $('tbody').append(tr);
                });

            });
            console.log(" пустые unit");
        } else {
            let unitsVal = "";
            if ($('#_units_' + mass[mass.length - 1]).val().toString() === '' && mass.length > 1) {
                unitsVal = $('#_units_' + mass[mass.length - 2]).val().toString();
            } else if (mass.length === 0 && $('#_units_' + mass[0]).val().toString() != '') {
                unitsVal = $('#_units_' + mass[0]).val().toString();
            } else {
                unitsVal = $('#_units_' + mass[mass.length - 1]).val().toString();
            }

            let str = "/table/filterAll?unit=" + unitsVal +
                "&valueScope=" + valueScope +
                "&valueType=" + valueType +
                "&valueObject=" + valueObject;
            $.get(str, function (data) {
                $('#div-table').empty();
                let dataIndex0 = data[0];
                let massIndex = [];

                for (let i = 1; i < dataIndex0.length; i++) {
                    let html = "<div>" +
                        "                <div class=\"card mb-5\">" +
                        "                    <header class=\"card-header\">" +
                        "                        <h2 class=\"h4 card-header-title\">" + dataIndex0[i] + "</h2>" +
                        "                    </header>" +
                        "                    <div class=\"card-body pt-0\">" +
                        "                        <div class=\"table-responsive\">" +
                        "                            <table class=\"table table-hover mb-0 fold-table\">" +
                        "                                <thead>" +
                        "                                <tr>" +
                        "                                    <th>#</th>" +
                        "                                    <th>Найменування</th>" +
                        "                                    <th>Укомплектованість</th>" +
                        "                                </tr>" +
                        "                                </thead>" +
                        "                                <tbody id='tbody_" + i + "'>" +
                        "                                </tbody>" +
                        "                            </table>" +
                        "                        </div>" +
                        "                    </div>" +
                        "                </div>" +
                        "            </div>";
                    massIndex.push(i);
                    $('#div-table').append(html);
                }

                for (let item = 0; item < massIndex.length; item++) {
                    for (let i = 1; i < data.length; i++) {
                        let output = data[i][massIndex[item]];
                        let tr = '<tr>' + '<td class="font-weight-semi-bold">' + i + '</td>' +
                            '<td class="font-weight-semi-bold">' + data[i][0] + '</td>' +
                            '<td class="font-weight-semi-bold">' + output.toString() + '%</td>' +
                            '</tr>';
                        $('#tbody_' + massIndex[item]).append(tr);
                    }
                }
            });
        }
    });
});

$(document).ready(function (){
    $(document).on('click', '#btnClear', function (){
       $('#div-table').empty();
    });
});

$(window).load(function () {
    $('#load-spinner').hide();
});

$(document).ajaxStart(function () {
    //$('#card-table').hide();
    $('#load-spinner').show();
}).ajaxStop(function () {
    $('#load-spinner').hide();
    //$('#card-table').show();
});


$(document).ready(function() {
    $('#scope').change(function() {
        sendAjaxRequest();
    });
});

function sendAjaxRequest() {
    let s = $("#scope").val();
    console.log(s);
    $.get( "/object/create/type?scope=" + s, function( data ) {
        $('#type').empty();
        $.each(data, function(k, v) {
            let option = "<option value = " + k + ">" + v +  "</option>";
            $("#type").append(option);
        });
    });
}


$(document).ready(function() {
    $('#type').change(function() {
        sendAjaxRequest2();
    });
});

function sendAjaxRequest2() {
    let s = $("#type").val();
    console.log(s);
    $.get( "/object/create/object?type=" + s, function( data ) {
        $('#object').empty();
        $.each(data, function(k, v) {
            let option = "<option value = " + k + ">" + v +  "</option>";
            $("#object").append(option);
        });
    });
}

$(document).ready(function(){
    $('input:radio[name="inlineRadioOptions"]').change(function(){
        if($(this).val() === 'option2'){
            $("#sel1").removeAttr('disabled');
            $("#form").prop("action", "/units/unit/save");
        }else {
            $("#sel1").prop("disabled", true);
            $("#form").prop("action", "/units/unit/saveNull");
        }
    });
});

$(document).ready(function(){
    $(document).on('click', '#dtnSave', function () {
        let element = document.getElementById('div-table');
        html2pdf(element);
    });
});
