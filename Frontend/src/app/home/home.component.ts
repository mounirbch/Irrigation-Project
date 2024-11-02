
import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Chart, registerables } from 'chart.js';
import { UserService } from '../service/user.service';
import { Statistics1Service } from '../statistics/statistics1.service';
Chart.register(...registerables);

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {


  temperatures: Donnee[] = [];
  humidites: Donnee[] = [];
  humiditesSol: Donnee[] = [];
  chartInstance!: Chart;

  //pie
  pieChart: any;
  crops: string[] = [];
  probabilities: number[] = [];
 

  constructor(private http: HttpClient, private statisticsService:Statistics1Service,private userService: UserService) {}

  ngOnInit(): void {
    this.fetchData();
    this.initPieChart();

    this.statisticsService.getNewDataObservable().subscribe(newData => {
      this.temperatures.push({ valeur: newData.temperature, date: new Date(newData.receivedDate) });
      this.humidites.push({ valeur: newData.humidity, date: new Date(newData.receivedDate) });
      this.humiditesSol.push({ valeur: newData.soilHumidity, date: new Date(newData.receivedDate) });
      this.updateChart(); 
    }),
    this.userService.getPredPlanteData().subscribe(data => {
      this.crops = data.map(item => item.crop);
      this.probabilities = data.map(item => item.probability);

      this.updatePieChart();
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
          this.renderChart();
        } else {
          console.error("Invalid response data format");
        }
      },
      err => console.error(err)
    );
  }
  renderChart(): void {
    const labels: string[] = [];
    const xLabels = this.generateXLabels(this.temperatures.map(d => d.date));
  
    this.temperatures.forEach((d, index) => {
      if (index === 0 || xLabels[index] !== xLabels[index - 1]) {
        labels.push(xLabels[index]);
      } else {
        labels.push('');
      }
    });
  
    this.chartInstance = new Chart("lineChart", {
      type: 'line',
      data: {
        labels: labels,
        datasets: [
          {
            label: 'Température',
            data: this.temperatures.map(d => d.valeur),
            borderColor: 'red',
            fill: false,
            pointRadius: 0
          },
          {
            label: 'Humidité',
            data: this.humidites.map(d => d.valeur),
            borderColor: 'blue',
            fill: false,
            pointRadius: 0
          },
          {
            label: 'Humidité du sol',
            data: this.humiditesSol.map(d => d.valeur),
            borderColor: 'green',
            fill: false,
            pointRadius: 0
          }
        ]
      },
      options: {
        scales: {
          y: {
            beginAtZero: true
          }
        }
      }
    });
  }
  

  generateXLabels(dates: Date[]): string[] {
    return dates.map((date, index) => {
      if (index % 5 === 0 || (index > 0 && date.getMinutes() === 0 && date.getMinutes() !== dates[index - 1].getMinutes())) {
        return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
      } else {
        return '';
      }
    });
  }

  

  updateChart(): void {
    if (this.chartInstance) {
      const labels = this.generateXLabels(this.temperatures.map(d => d.date));
  
      this.chartInstance.data.labels = labels;
      this.chartInstance.data.datasets[0].data = this.temperatures.map(d => d.valeur);
      this.chartInstance.data.datasets[1].data = this.humidites.map(d => d.valeur);
      this.chartInstance.data.datasets[2].data = this.humiditesSol.map(d => d.valeur);
  
      this.chartInstance.update();
    }
  }
  updatePieChart(): void{
    this.pieChart.data.labels = this.crops;
    this.pieChart.data.datasets[0].data = this.probabilities;
    this.pieChart.data.datasets[0].backgroundColor = this.generateRandomColors(this.crops.length);
    this.pieChart.update();
  }
  
  initPieChart(): void {
    this.pieChart = new Chart('pieChart', {
      type: 'pie',
      data: {
        labels: [], 
        datasets: [{
          label: 'Probability',
          data: [], 
          borderWidth: 1
        }]
      },
      options: {
        plugins: {
          legend: {
            position: 'left',
            align: 'start',
            labels: {
              boxWidth: 20, 
              padding: 15, 
              usePointStyle: true 
            }
          }
        }
      }
    });
}
generateRandomColors(numColors: number): string[] {
  const colors: string[] = [];
  for (let i = 0; i < numColors; i++) {
    const color = `rgba(${Math.floor(Math.random() * 256)}, ${Math.floor(Math.random() * 256)}, ${Math.floor(Math.random() * 256)}, 0.5)`;
    colors.push(color);
  }
  return colors;
}
  
}

export class Donnee {
  valeur: number = 0;
  date: Date = new Date();
}

