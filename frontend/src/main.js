import Vue from 'vue'
import App from './App.vue'
import Chart from 'chart.js'

const axios = require('axios')

new Vue({
  el: '#app',
  render: h => h(App),
  mounted () {
    axios
      .get('https://still-mountain-17725.herokuapp.com/api/temp?limit=100&page=-1')
      .then(response => {
        this.dates = response.data.map(list => {
          return list.date
        })

        this.temps = response.data.map(list => {
          return list.value
        })

        var ctx = document.getElementById('tempChart')
        this.chart = new Chart(ctx, {
          type: 'line',
          data: {
            labels: this.dates,
            datasets: [
              {
                label: 'Avg. Temp',
                backgroundColor: 'rgba(54, 162, 235, 0.5)',
                borderColor: 'rgb(54, 162, 235)',
                fill: false,
                data: this.temps
              }
            ]
          },
          options: {
            title: {
              display: true,
              text: 'Temperature @ Home'
            },
            scales: {
              xAxes: [
                {
                  type: 'time',
                  time: {
                    unit: 'minute',
                    displayFormats: {
                      hour: 'LL @ kk:mm'
                    },
                    tooltipFormat: 'LL @ kk:mm'
                  },
                  scaleLabel: {
                    display: true,
                    labelString: 'Date/Time'
                  }
                }
              ],
              yAxes: [
                {
                  scaleLabel: {
                    display: true,
                    labelString: 'Temperature (°С)'
                  },
                  ticks: {
                    callback: function (value) {
                      return value + '°С'
                    }
                  }
                }
              ]
            }
          }
        })
      })
      .catch(error => {
        throw (error)
      })
      .finally(() => (this.loading = false))
  }
}).$mount('#app')
