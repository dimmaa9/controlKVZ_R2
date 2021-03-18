(function ($) {
  $(document).on('ready', function () {

    let stateVn = 0;
    let statePb = 0;
    let stateRm = 0;
    $.ajax({
      url: "/getState",
      success: function (res) {
        stateVn = res.vn;
        statePb = res.pb;
        stateRm = res.rm;
      },
      async: false
    });
    var chart,
      chartClass = '.js-doughnut-chart',
      data = {
        datasets: [{
          data: [stateVn, statePb, stateRm],
          backgroundColor: ['#9cffa3', '#fffa85', '#ff7575', '#f3f5fb'],
          hoverBackgroundColor: ['#00ff10', '#fff400', '#ff0000', '#f3f5fb'],
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
})(jQuery);