anychart.onDocumentReady(function () {

    // set the data
    $.get("/getNullUnitValue", function (dataInput) {
        let data = [];

        $.each(dataInput, function (k, v) {
            data.push({x: k, value: v});
        });

        // create the chart
        let chart = anychart.pie();


        // add the data
        chart.data(data);

        // sort elements
        chart.sort("desc");

        // set legend position
        chart.legend().position("right");
        // set items layout
        chart.legend().itemsLayout("vertical");

        // display the chart in the container
        chart.container('container');
        chart.draw();
    });





});