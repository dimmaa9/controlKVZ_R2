
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

                let html =
                    "<div class=\"col-md-6 mb-5\">" +
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
                    "            </div>" +
                    "<div class=\"col-md-6 mb-5\">" +
                    "<div id=\"containerChart\" style=\"width: 100%; height: 450px;\"></div>" +
                    "</div>";
                $('#div-table').append(html);
                anychart.onDocumentReady(function () {

                    let dataTempArr = [];
                    $.each(data, function (k, v) {
                        if (v != 0){
                            dataTempArr.push({
                                x: k,
                                value: v,
                                yoy: 30
                            });
                        }
                    });
                    dataTempArr.sort((a, b) => a.value < b.value ? 1 : -1);


                    let dataSet = anychart.data.set(dataTempArr);

                    // set chart type
                    var chart = anychart.column();

                    chart.title().enabled(true).useHtml(true)
                        .text("Діаграма підрозділів <br> по укомплектованості<br> за останніми оновленнями")
                        .hAlign("center");

                    // disable x axis title
                    chart.xAxis().title().enabled(false);

                    // allow two lines in x axis labels
                    chart.xAxis().staggerMode(2);

                    // set data
                    var column = chart.column(dataSet);

                    var view = dataSet.mapAs();

                    // set listener on chart
                    column.listen(

                        // listener type
                        "pointmouseover",

                        // function, if listener triggers
                        function(e) {
                            // receive all necessary information and summarize it in one variable
                            var infoGetter = "Підрозділ:<b>" +
                                view.get(e.pointIndex, "x") +
                                "</b><br/> Укомплектовано на: <b>" +
                                view.get(e.pointIndex, "value") +
                                "</b> %<br/>Нестача техніки: <b>" +
                                view.get(e.pointIndex, "yoy") + "</b>" ;

                            // set received information into chart title
                            chart.title().text(infoGetter).fontWeight(300);
                        }
                    );
                    column.listen(
                        // listener type
                        "pointmouseout",

                        // function, if listener triggers
                        function () {
                            chart.title().fontWeight(900).text("Діаграма підрозділів <br> по укомплектованості<br> за останніми оновленнями");
                        }
                    );

                    // set container and draw chart
                    chart.container("containerChart").draw();
                });
                let i = 1;
                $.each(data, function (k, v) {
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

                let html = "<div class=\"col-md-6 mb-5\">" +
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
                $.each(data, function (k, v) {
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
                    let html = "<div class=\"col-md-6 mb-5\">" +
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

$(document).ready(function () {
    $(document).on('click', '#btnClear', function () {
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
