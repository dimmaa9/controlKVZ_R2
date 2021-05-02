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
    if (s === 'null') {
        $('#type').empty();
        $("#type").append("<option value='null'>Нічого не вибрано</option>");
    } else {
        $.get("/object/create/type?scope=" + s, function (data) {
            $('#type').empty();

            let count = 0;
            $.each(data, function (k, v) {
                count++;
            });

            if (count === 0) {
                $("#type").append("<option value='null'>Відсутня інформація</option>");
            } else {
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
    if (s === 'null') {
        $('#object').empty();
        $("#object").append("<option value='null'>Нічого не вибрано</option>");
    } else {
        $.get("/object/create/object?type=" + s, function (data) {
            $('#object').empty();

            let count = 0;
            $.each(data, function (k, v) {
                count++;
            });

            if (count === 0) {
                $("#object").append("<option value='null'>Відсутня інформація</option>");
            } else {
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

$(document).ready(function () {
    $('#_date_').change(function () {
        sendAjaxRequestDate();
    });
});

function sendAjaxRequestDate() {
    let value = $("#_date_").val();
    let idUnit = $("#idUnit").val();
    let table = $('#unitThingTable').DataTable();
    table.clear().draw();

    if (value === 'all') {
        $.get("/units/table/" + idUnit.toString() + "/all", function (data) {
            let i = 1;
            $.each(data, function (k, v) {
                //[0]-item.getObject().getObjectName()
                //[1]-item.getGeneralNeed() item.getGeneralHave()
                let values = v.split('|||');
                let values23 = values[1].toString().split(' ');

                let evensStr = "<a style=\"margin-left: 10px;\" type=\"button\"\n" +
                "class=\"btn btn-outline-primary text-uppercase mb-2 mr-2\"\n" +
                "href=\"/units/edit/"+ k +"\">Редагувати</a>\n" +
                "<a style=\"margin-left: 10px;\" type=\"button\"\n" +
                "class=\"btn btn-outline-danger text-uppercase mb-2 mr-2\"\n" +
                "href=\"/units/delete/"+ k +"\">Видалити</a>"

                table.row.add([
                    i++,
                    values[0],
                    values23[0],
                    values23[1],
                    values23[2],
                    evensStr
                ]).draw();
            });
        });
    } else {
        $.get("/units/table/" + idUnit.toString() + "/" + value, function (data) {
            let i = 1;
            $.each(data, function (k, v) {
                //[0]-item.getObject().getObjectName()
                //[1]-item.getGeneralNeed() item.getGeneralHave()
                let values = v.split('|||');
                let values23 = values[1].toString().split(' ');

                let evensStr = "<a style=\"margin-left: 10px;\" type=\"button\"\n" +
                    "class=\"btn btn-outline-primary text-uppercase mb-2 mr-2\"\n" +
                    "href=\"/units/edit/"+ k +"\">Редагувати</a>\n" +
                    "<a style=\"margin-left: 10px;\" type=\"button\"\n" +
                    "class=\"btn btn-outline-danger text-uppercase mb-2 mr-2\"\n" +
                    "href=\"/units/delete/"+ k +"\">Видалити</a>"

                table.row.add([
                    i++,
                    values[0],
                    values23[0],
                    values23[1],
                    value,
                    evensStr
                ]).draw();
            });
        });
    }
}




