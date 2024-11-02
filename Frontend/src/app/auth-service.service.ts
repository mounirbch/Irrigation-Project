/*import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthServiceService {

  private isLoggedInValue: boolean = false;
  private userRole: string = ''; // Peut être 'user' ou 'admin'

  constructor(private http: HttpClient) {}

  login(userCredentials: any): Observable<any> {
    return this.http.post('http://localhost:8082/connexion', userCredentials)
      .pipe(
        tap((response: any) => {
          const token = response.token;
          const role = response.role;
          this.saveUserRole(role); // Sauvegarde du rôle de l'utilisateur
          this.setIsLoggedIn(true); // Définit l'état de connexion comme vrai
          localStorage.setItem('token', token); // Stockage du token JWT dans le localStorage
          localStorage.setItem('role', role);
        })
      );
  }

  saveUserRole(role: string): void {
    this.userRole = role;
  }

  getUserRole(): string {
    return this.userRole;
  }

  isLoggedIn(): boolean {
    return this.isLoggedInValue;
  }

  setIsLoggedIn(value: boolean): void {
    this.isLoggedInValue = value;
  }
}
*/