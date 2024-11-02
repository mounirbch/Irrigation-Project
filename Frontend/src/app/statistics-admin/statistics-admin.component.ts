import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Chart, registerables } from 'chart.js'; 

Chart.register(...registerables);
import 'chartjs-plugin-datalabels';
import { AdminServiceService } from '../home-admin/admin-service.service';


@Component({
  selector: 'app-statistics-admin',
  templateUrl: './statistics-admin.component.html',
  styleUrls: ['./statistics-admin.component.css']
})
export class StatisticsAdminComponent implements OnInit {
  dateObj :date ;
  public chartData: any;



  utilisateursActifsData: number = 0;
  utilisateursArchivesData: number = 0;
  utilisateursGoogleData: number = 0;

  constructor(private http: HttpClient,private adminService:AdminServiceService) {
    this.dateObj=new date();
   }
 
  ngOnInit(): void {
    this.fetchUtilisateursActifs();
    this.fetchUtilisateursArchives();
    this.fetchUtilisateursGoogle();

    this.dateObj.date = '2024-05'; 
    this.fetchData();
  
  }

  fetchUtilisateursActifs(): void {
    this.http.get<number>('http://localhost:8082/pourcentageUtilisateursActifs').subscribe(
      data => {
        this.utilisateursActifsData = data;
        this.createChartUtilisateursActifs();
      },
      error => {
        console.error('Erreur lors de la récupération des utilisateurs actifs :', error);
      }
    );
  }

  fetchUtilisateursArchives(): void {
    this.http.get<number>('http://localhost:8082/pourcentageUtilisateursArchives').subscribe(
      data => {
        this.utilisateursArchivesData = data;
        this.createChartUtilisateursArchives();
      },
      error => {
        console.error('Erreur lors de la récupération des utilisateurs archivés :', error);
      }
    );
  }

  createChartUtilisateursActifs(): void {
    const ctx = document.getElementById('chartUtilisateursActifs') as HTMLCanvasElement;
    new Chart(ctx, {
      type: 'pie',
      data: {
        labels: ['Utilisateurs Actifs', 'Utilisateurs non actifs'],
        datasets: [{
          data: [this.utilisateursActifsData, 100 - this.utilisateursActifsData],
          backgroundColor: [
            'rgb(75, 192, 192)',
            'rgb(201, 203, 207)'
          ]
        }]
      },
      options: {
        plugins: {
          datalabels: {
            color: '#fff', // couleur du texte des labels
            formatter: (value, ctx) => {
              return value + '%'; // format des labels
            }
          }
        }
      }
    });
  }


  createChartUtilisateursArchives(): void {
    const ctx = document.getElementById('chartUtilisateursArchives') as HTMLCanvasElement;
    new Chart(ctx, {
      type: 'pie',
      data: {
        labels: ['Utilisateurs Archivés', 'utilisateurs non archivé(e)s'],
        datasets: [{
          data: [this.utilisateursArchivesData, 100 - this.utilisateursArchivesData],
          backgroundColor: [
            'rgb(255, 99, 132)',
            'rgb(201, 203, 207)'
          ]
        }]
      }
    });
  }

  fetchUtilisateursGoogle(): void {
    this.http.get<number>('http://localhost:8082/pourcentageUtilisateursGoogle').subscribe(
      data => {
        this.utilisateursGoogleData = data;
        this.createChartUtilisateursGoogle();
      },
      error => {
        console.error('Erreur lors de la récupération des utilisateurs archivés :', error);
      }
    );
  }

  createChartUtilisateursGoogle(): void {
    const ctx = document.getElementById('chartUtilisateursGoogle') as HTMLCanvasElement;
    new Chart(ctx, {
      type: 'pie',
      data: {
        labels: ['Utilisateurs utilise Google','utilisateurs avec leur propre email   '],
        datasets: [{
          data: [this.utilisateursGoogleData, 100 - this.utilisateursGoogleData],
          backgroundColor: [
            'rgb(0, 0, 255)',
            'rgb(201, 203, 207)'
          ]
        }]
      }
    });
  }

  user:String="";
  fetchData() {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      })
    };

    this.http.get<string[]>('http://localhost:8082/Tousutilisateurs?date=' + this.dateObj.date, httpOptions)
      .subscribe(data => {
        console.log(data);

        if (this.chartData) {
          this.chartData.destroy(); 
        }

        if (Array.isArray(data) && data.length === 1 && data[0] === '0') {
          console.log("Aucun utilisateur n'a été ajouté.");
          this.user = "Aucun utilisateur n'a été ajouté durant ce mois";
        } else if (data && data.length > 0) {
          const jours = [];
          const utilisateurs = [];

          for (let i = 0; i < data.length; i++) {
            const elements = data[i].split(';');
            jours.push(parseInt(elements[0]));
            utilisateurs.push(parseInt(elements[1]));
          }

          const ctx = document.getElementById('myChart') as HTMLCanvasElement;
          this.chartData = new Chart(ctx, {
            type: 'line',
            data: {
              labels: jours.map(jour => jour.toString()),
              datasets: [
                {
                  label: 'Nombre d\'utilisateurs',
                  data: utilisateurs,
                  fill: false,
                  borderColor: 'rgb(75, 192, 192)',
                  tension: 0.1
                }
              ]
            },
            options: {
              scales: {
                x: {
                  title: {
                    display: true,
                    text: 'Jour du mois'
                  }
                },
                y: {
                  title: {
                    display: true,
                    text: 'Nombre d\'utilisateurs'
                  }
                }
              }
            }
          });
        } else {
          console.log("Aucune donnée n'a été renvoyée par l'API.");
        }
      });
  }
}
  
  

export class date {
  date: string="";
}