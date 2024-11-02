import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { response } from 'express';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AdminServiceService {

  constructor(private http:HttpClient) { }
  getUtilisateur(): Observable<any> {
    const httpOptions: { headers: HttpHeaders, responseType: 'json' } = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json' 
      }),
      responseType: 'json' 
    };
    return this.http.get<any[]>('http://localhost:8082/utilisateurs',httpOptions);
  }
  
  getUtilisateurArchive(): Observable<any> {
    const httpOptions: { headers: HttpHeaders, responseType: 'json' } = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json' 
      }),
      responseType: 'json' 
    };
    return this.http.get<any[]>('http://localhost:8082/utilisateursArchive',httpOptions);
  }

  ArchiveUtilisateur(email: string): Observable<string> {
    const url = `http://localhost:8082/archiveUtilisateur/${email}`;
    return this.http.delete(url, { responseType: 'text' });
  }
  DesarchiverUtilisateur(email: string): Observable<string> {
    const url = `http://localhost:8082/desarchiveUtilisateur/${email}`;
    return this.http.delete(url, { responseType: 'text' });
  }

  getUtilisateurParMail(email: string): Observable<any> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      })
    };
    return this.http.get<any>('http://localhost:8082/getUser/' + email, httpOptions);
  }


  fetchData(date: string) {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      })
    };
    return this.http.get<any>(`http://localhost:8080/Tousutilisateurs?date=${date}`, httpOptions);
  }
}
