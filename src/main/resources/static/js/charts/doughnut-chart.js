(function ($) {
  $(document).on('ready', function () {
    $.get( "/getPercent", function(dataInput) {
      let data0 = [];
      $.each(dataInput, function(i, v) {
        data0.push(v);
      });

      let chart,
          chartClass = '.js-doughnut-chart',
          data = {
            datasets: [{
              data: dataInput,
              backgroundColor: ['#9cffa3', '#ff7575', '#f3f5fb'],
              hoverBackgroundColor: ['#00ff10', '#ff0000', '#f3f5fb'],
              borderWidth: 0
            }],
            labels: ['Blue', 'Light Blue', 'Teal', 'Gray']
          },
          options = {
            responsive: true,
            legend: false,
            tooltips: {
              enabled: false
            },
            cutoutPercentage: 40
          };

      $(chartClass).each(function (i, el) {

        if ($(el).parents().is('.tab-pane')) {

          if ($(el).parents().is('.tab-pane.active')) {

            chart = new Chart(el, {
              type: 'doughnut',
              data: data,
              options: options
            });

            $(el).addClass('initialized');

          }

        } else {

          chart = new Chart(el, {
            type: 'doughnut',
            data: data,
            options: options
          });

          $(el).addClass('initialized');

        }

      });

      if ($('[data-toggle="tab"]').length) {

        $('[data-toggle="tab"]').on('shown.bs.tab', function (e) {

          var $targetEl = $($(e.target).attr('href')).find(chartClass + ':not(.initialized)');

          if ($targetEl.length) {

            chart = new Chart($targetEl, {
              type: 'doughnut',
              data: data,
              options: options
            });

            $targetEl.addClass('initialized');

          }

        });

      }

    });
  });
})(jQuery);