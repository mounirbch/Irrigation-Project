import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { MainComponent } from '../main/main.component';
import { Subject } from 'rxjs/internal/Subject';
import { BehaviorSubject, Observable, map } from 'rxjs';
import { CalendarEvent } from 'angular-calendar';
@Injectable({
  providedIn: 'root'
})
export class UserService {
  

  private isUserLoggedInSubject: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  public isUserLoggedIn$: Observable<boolean> = this.isUserLoggedInSubject.asObservable();
  userRole: string="";


  constructor(private http: HttpClient, ) { }

  // Méthode pour récupérer les utilisateurs depuis l'API
  public getUsers() {
    return this.http.get<any[]>("https://jsonplaceholder.typicode.com/users");}

    login(connectionObj: any): Observable<any> {
      return this.http.post('http://localhost:8082/connexion', connectionObj); //, { responseType: 'text' }
    }

    logout(): Observable<any> {
      return this.http.post('http://localhost:8082/deconnexion', {}); //, { responseType: 'text' }
    }
//cote admin pour afficher tous les utilisateur stocker dans ma collection utilisateur

    getUtilisateur(): Observable<any> {
      const httpOptions: { headers: HttpHeaders, responseType: 'json' } = {
        headers: new HttpHeaders({
          'Content-Type': 'application/json' 
        }),
        responseType: 'json' 
      };
      return this.http.get<any[]>('http://localhost:8082/utilisateurs', httpOptions);
    }




    public getPredPlanteData(): Observable<any[]> {
      // Définir les options HTTP
      const httpOptions: { headers: HttpHeaders, responseType: 'json' } = {
        headers: new HttpHeaders({
          'Content-Type': 'application/json' 
        }),
        responseType: 'json'
      };
      return this.http.get<any[]>('http://localhost:8082/api/pred_plante', httpOptions);
    }






    ModifierProfil(ProfilObj: any): Observable<string> {
      return this.http.post('http://localhost:8082/modifier', ProfilObj, { responseType: 'text' });
    }

   
  
    
    AjouterEvent(evenement: any): Observable<string> {
      return this.http.post('http://localhost:8082/calendar/add', evenement, { responseType: 'text' });
    }
    getCalendarEvents(): Observable<CalendarEvent[]> {
      return this.http.get<EventDate[]>('http://localhost:8082/calendar/get1').pipe(
        map((events: any[]) => events.map(event => ({
          title: event.title,
          start: new Date(event.start),
          end: new Date(event.end)
        })))
      );
    }

    //extraire token et role de reponse texte
  extractTokenAndRole(response: any): { token: string, role: string } {
    return {
      token: response.token,
      role: response.role
    };
  }
  


  setIsUserLoggedIn(value: boolean): void {
    this.isUserLoggedInSubject.next(value);
    
  }
  //save token besh nesta3melha fi main.ts
  saveToken(token:string):void{
    // Supprimer les accolades du token avant de l'enregistrer
    const tokenWithoutBraces = token.replace(/^{([^{}]*)}$/, '$1');
    localStorage.setItem('token',tokenWithoutBraces)
  }

  ////save role besh nesta3melha fi main.ts ????
  saveRole(role: string): void {
    localStorage.setItem('role', role);
  }
  

  isLogged(): boolean{
    const token =localStorage.getItem('token')
    console.log(token)
    return token !== null && token !== undefined;

    //return !! token 
  }

  clearToken(): void{
    localStorage.removeItem('token')
  }
  clearrole(): void{
    localStorage.removeItem('role')
  }
  //get de token min localstorage
  getToken(): String |null{
    return localStorage.getItem('token')
  }
  //get role  ??
  getRole(): string | null {
    return localStorage.getItem('role');
  }

  getUserRole(): string {
    return this.userRole;
  }
getEventsByInterval(start: string, end?: string): Observable<any[]> {
  let url: string;

  if (end) {
    url = `http://localhost:8082/calendar/getByInterval?start=${start}&end=${end}`;
  } else {
    url = `http://localhost:8082/calendar/getByInterval?start=${start}`;
  }

  return this.http.get<any[]>(url);
}

  

}
interface EventDate {
  title: string;
  start: string;
  end: string;
}


