(function ($) {
  $(document).on('ready', function () {

    let stateVn = [];
    let statePb = [];
    let stateRm = [];
    $.ajax({
      url: "/getStateByCategory",
      success: function (res) {
        stateVn = res.listVn;
        statePb = res.listPb;
        stateRm = res.listRm;
      },
      async: false
    });

    console.log(stateVn);
    console.log(statePb);
    console.log(stateRm);

    var chart,
      chartClass = '.js-area-chart',
      data = {
        labels: ['1', '2', '3', '4', '5'],
        datasets: [{
          data: stateVn,

          borderColor: 'rgb(62,174,0)',
          backgroundColor: 'rgba(0,237,150,0.1)',

          pointBackgroundColor: "#ffffff",
          pointShadowColor: 'rgba(0, 0, 0, .19)',
          pointShadowOffsetX: 0,
          pointShadowOffsetY: 2
        }, {
          data: statePb,

          borderColor: 'rgb(233,231,0)',
          backgroundColor: 'rgba(233, 231, 248, .1)',

          pointBackgroundColor: "#ffffff",
          pointShadowColor: 'rgba(0, 0, 0, .19)',
          pointShadowOffsetX: 0,
          pointShadowOffsetY: 2
        },
          {
            data: stateRm,

            borderColor: 'rgb(255,0,0)',
            backgroundColor: 'rgba(68, 75, 248, .1)',

            pointBackgroundColor: "#ffffff",
            pointShadowColor: 'rgba(0, 0, 0, .19)',
            pointShadowOffsetX: 0,
            pointShadowOffsetY: 2
          }]
      },
      options = {
        responsive: true,
        maintainAspectRatio: false,
        legend: {
          display: false
        },
        hover: {
          mode: 'nearest',
          intersect: false
        },
        tooltips: {
          enabled: false,
          mode: 'nearest',
          intersect: false,
          custom: function (tooltipModel) {
            // Tooltip Element
            var tooltipEl = document.getElementById('chartjsAreaTooltip');

            // Create element on first render
            if (!tooltipEl) {
              tooltipEl = document.createElement('div');
              tooltipEl.id = 'chartjsAreaTooltip';
              tooltipEl.classList.add('u-chartjs-tooltip-wrap');
              tooltipEl.innerHTML = '<div class="u-chartjs-tooltip"></div>';
              document.body.appendChild(tooltipEl);
            }

            // Hide if no tooltip
            if (tooltipModel.opacity === 0) {
              tooltipEl.style.opacity = 0;
              return;
            }

            // Set caret Position
            tooltipEl.classList.remove('above', 'below', 'no-transform');
            if (tooltipModel.yAlign) {
              tooltipEl.classList.add(tooltipModel.yAlign);
            } else {
              tooltipEl.classList.add('no-transform');
            }

            function getBody(bodyItem) {
              return bodyItem.lines;
            }

            // Set Text
            if (tooltipModel.body) {
              var titleLines = tooltipModel.title || [],
                bodyLines = tooltipModel.body.map(getBody),
                today = new Date();

              var innerHtml = '<header class="u-chartjs-tooltip-header">';

              titleLines.forEach(function (title) {
                innerHtml += title + ' категорія';
              });

              innerHtml += '</header><div class="u-chartjs-tooltip-body">';

              bodyLines.forEach(function (body, i) {
                var oldBody = body[0],
                  newBody = oldBody.substring(0, oldBody.length - 3) + ',' + oldBody.substring(oldBody.length - 3);

                innerHtml += (oldBody.length > 3 ? newBody : body);
              });

              innerHtml += '</div>';

              var tooltipRoot = tooltipEl.querySelector('.u-chartjs-tooltip');
              tooltipRoot.innerHTML = innerHtml;
            }

            // `this` will be the overall tooltip
            var position = this._chart.canvas.getBoundingClientRect();

            // Display, position, and set styles for font
            console.log();

            tooltipEl.style.opacity = 1;
            tooltipEl.style.left = position.left + window.pageXOffset + tooltipModel.caretX - (tooltipEl.offsetWidth / 2) - 3 + 'px';
            tooltipEl.style.top = position.top + window.pageYOffset + tooltipModel.caretY - tooltipEl.offsetHeight - 25 + 'px';
            tooltipEl.style.pointerEvents = 'none';
          }
        },
        elements: {
          line: {
            borderWidth: 3
          },
          point: {
            pointStyle: 'circle',
            radius: 5,
            hoverRadius: 7,
            borderWidth: 3,
            backgroundColor: '#ffffff'
          }
        },

      };

    $(chartClass).each(function (i, el) {

      if ($(el).parents().is('.tab-pane')) {

        if ($(el).parents().is('.tab-pane.active')) {

          chart = new Chart(el, {
            type: 'line',
            data: data,
            options: options
          });

          $(el).addClass('initialized');

        }

      } else {

        chart = new Chart(el, {
          type: 'line',
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
            type: 'line',
            data: data,
            options: options
          });

          $targetEl.addClass('initialized');

        }

      });

    }

  });
})(jQuery);