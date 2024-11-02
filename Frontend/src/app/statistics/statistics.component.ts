import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Statistics1Service } from './statistics1.service';
import { Chart, registerables } from 'chart.js';


@Component({
  selector: 'app-statistics',
  templateUrl: './statistics.component.html',
  styleUrls: ['./statistics.component.css']
})
export class StatisticsComponent implements OnInit {
  temperatures: Donnee[] = [];
  humidites: Donnee[] = [];
  humiditesSol: Donnee[] = [];
  chartTemperature!: Chart;
  chartHumidity!: Chart;
  chartSoilTemperature!: Chart;
  
  constructor(private http: HttpClient, private statisticsService: Statistics1Service) { }

  ngOnInit(): void {
    this.fetchData();

    this.statisticsService.getNewDataObservable().subscribe(newData => {
      this.temperatures.push({ valeur: newData.temperature, date: new Date(newData.receivedDate) });
      this.humidites.push({ valeur: newData.humidity, date: new Date(newData.receivedDate) });
      this.humiditesSol.push({ valeur: newData.soilHumidity, date: new Date(newData.receivedDate) });
      this.updateCharts(); 
    });
  }

  fetchData(): void {
    const httpOptions: { headers: HttpHeaders, responseType: 'json' } = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      }),
      responseType: 'json'
    };

    this.http.get<any[]>('http://localhost:8082/api/getting', httpOptions).subscribe(
      (response) => {
        if (Array.isArray(response) && response.length > 0) {
          response.forEach(data => {
            this.temperatures.push({ valeur: data.temperature, date: new Date(data.receivedDate) });
            this.humidites.push({ valeur: data.humidity, date: new Date(data.receivedDate) });
            this.humiditesSol.push({ valeur: data.soilHumidity, date: new Date(data.receivedDate) });
          });
          this.renderCharts();
        } else {
          console.error("Invalid response data format");
        }
      },
      err => console.error(err)
    );
  }

  renderCharts(): void {
    const labels = this.temperatures.map(d => d.date.toLocaleTimeString());
    const xLabels = this.generateXLabels(this.temperatures.map(d => d.date));

    this.chartTemperature = new Chart("chartTemperature", {
      type: 'line',
      data: {
        labels: labels,
        datasets: [{
          label: 'Température',
          data: this.temperatures.map(d => d.valeur),
          borderColor: 'red',
          fill: false,
          pointRadius:0
        }]
      },
      options: {
        scales: {
          x: {
            ticks: {
              callback: (value, index, values) => {
                // Afficher la minute chaque 5 minutes et l'heure chaque fois que l'heure change
                if (index % 5 === 0 || (index > 0 && xLabels[index] !== xLabels[index - 1])) {
                  return xLabels[index];
                } else {
                  return '';
                }
              }
            }
          },
          y: {
            beginAtZero: true
          }
        }
      }
    });

    this.chartHumidity = new Chart("chartHumidity", {
      type: 'line',
      data: {
        labels: labels,
        datasets: [{
          label: 'Humidité',
          data: this.humidites.map(d => d.valeur),
          borderColor: 'blue',
          fill: false,
          pointRadius:0
        }]
      },
      options: {
        scales: {
          x: {
            ticks: {
              callback: (value, index, values) => {
                if (index % 5 === 0 || (index > 0 && xLabels[index] !== xLabels[index - 1])) {
                  return xLabels[index];
                } else {
                  return '';
                }
              }
            }
          },
          y: {
            beginAtZero: true
          }
        }
      }
    });

    this.chartSoilTemperature = new Chart("chartSoilTemperature", {
      type: 'line',
      data: {
        labels: labels,
        datasets: [{
          label: 'Humidite du sol',
          data: this.humiditesSol.map(d => d.valeur),
          borderColor: 'green',
          fill: false,
          pointRadius:0
        }]
      },
      options: {
        scales: {
          x: {
            ticks: {
              callback: (value, index, values) => {
                if (index % 5 === 0 || (index > 0 && xLabels[index] !== xLabels[index - 1])) {
                  return xLabels[index];
                } else {
                  return '';
                }
              }
            }
          },
          y: {
            beginAtZero: true
          }
        }
      }
    });
  }


  updateCharts(): void {
    if (this.chartTemperature && this.chartHumidity && this.chartSoilTemperature) {
      const labels = this.temperatures.map(d => d.date.toDateString());
      const xLabels = this.generateXLabels(this.temperatures.map(d => d.date));

      this.chartTemperature.data.labels = labels;
      this.chartTemperature.data.datasets[0].data = this.temperatures.map(d => d.valeur);

      this.chartHumidity.data.labels = labels;
      this.chartHumidity.data.datasets[0].data = this.humidites.map(d => d.valeur);

      this.chartSoilTemperature.data.labels = labels;
      this.chartSoilTemperature.data.datasets[0].data = this.humiditesSol.map(d => d.valeur);

      this.chartTemperature.update();
      this.chartHumidity.update();
      this.chartSoilTemperature.update();
    }
  }  

  generateXLabels(dates: Date[]): string[] {
    let lastDay = '';
    return dates.map((date, index) => {
      const currentDate = date.toLocaleDateString([], { weekday: 'long' });
      const currentHour = date.getHours();
      const currentMinute = date.getMinutes();
  
      
      const dayChanged = currentDate !== lastDay;
      lastDay = currentDate;
  
      
      if (index === 0 || dayChanged || currentHour !== dates[index - 1].getHours() || currentMinute % 30 === 0) {
        if (dayChanged) {
          return `${currentDate}, ${date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}`;
        } else {
          return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
        }
      } else {
        return '';
      }
    });
  }
  
  
  
}
export class Donnee {
  valeur: number = 0;
  date: Date = new Date();
}
