import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-capteur',
  templateUrl: './capteur.component.html',
  styleUrls: ['./capteur.component.css']
})
export class CapteurComponent implements OnInit{

  selectedSensor!: string;
  quantity!: number;
  responseMessage: string = ''; 
  capteurs: any[] = [];

  constructor(private http: HttpClient) {}
  ngOnInit(): void {
    this.recupererCapteurs();
  }

  recupererCapteurs(): void {
    const httpOptions: { headers: HttpHeaders } = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      }),
      
    };
    this.http.get<any[]>('http://localhost:8082/api/tous_les_capteurs', httpOptions)
      .subscribe(
        (data: any[]) => {
          this.capteurs = data;
        },
        (error) => {
          console.error('Erreur lors de la récupération des utilisateurs :', error);
        }
      
      );
  }

  
  ajouterDonnees(formData: any): void {
    const httpOptions: { headers: HttpHeaders } = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      })
    };

    this.http.post<any>('http://localhost:8082/api/ajouter_data', formData , httpOptions)
      .subscribe(response => {
        this.responseMessage = response.message;; 
      }, error => {
        this.responseMessage = 'Erreur lors de envoi des données : ' + error.message; 
      });
  }

  
  supprimerDonnees(sensor: string): void {
    this.http.delete<any>(`http://localhost:8082/api/supprimer_data/${sensor}`)
      .subscribe(response => {
        this.responseMessage = response.message;; 
      }, error => {
        this.responseMessage = 'Erreur lors de la suppression des données : ' + error.message; 
      });
  }

 
  modifierDonnees(formData: any): void {
    this.http.put<any>('http://localhost:8082/api/modifier_data', formData)
      .subscribe(response => {
        this.responseMessage = response.message; 
      }, error => {
        this.responseMessage = 'Erreur lors de la modification des données : ' + error.message; 
      });
  }

 
  onSubmit(): void {
    const formData = {
      sensor: this.selectedSensor,
      quantity: this.quantity
    };
    this.ajouterDonnees(formData);
  }


  onSupprimer(): void {
    this.supprimerDonnees(this.selectedSensor);
  }

 
  onModifier(): void {
    const formData = {
      sensor: this.selectedSensor,
      quantity: this.quantity
    };
    this.modifierDonnees(formData);
  }

}
