import { Component } from '@angular/core';
import { AngularFireAuth } from '@angular/fire/compat/auth';
import { GoogleAuthProvider } from '@firebase/auth';
import { Router } from '@angular/router';
import { map } from 'rxjs/operators';
import { Observable, of } from 'rxjs';
import firebase from 'firebase/compat/app'; // Importation du module Firebase
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-login-google',
  templateUrl: './login-google.component.html',
  styleUrls: ['./login-google.component.css']
})
export class LoginGoogleComponent {

  user$: Observable<{ user: firebase.User | null; }> = this.angularAuth.authState.pipe(
    map(user => ({ user }))
  );

  constructor(private angularAuth: AngularFireAuth, private router: Router, private http:HttpClient) {}

  onSignInClick() {
    this.angularAuth.signInWithPopup(new GoogleAuthProvider())
      .then((result) => {
        // Récupérer l'utilisateur connecté
        const user = result.user;
        // Vérifier si l'utilisateur n'est pas null avant d'appeler saveUser
        if (user) {
          // Appeler la méthode saveUser pour enregistrer l'utilisateur dans le backend
          this.saveUser(user);
        } else {
          console.error("User is null");
        }
        console.log("Successfully signed in", result);
      })
      .catch((error) => {
        console.error("Error signing in", error);
      });
  }
  
    saveUser(user: firebase.User) {
      this.http.post('http://localhost:8082/utilisateursGoogle', {
        nom: user.displayName,
        email: user.email,

      }).subscribe(
        (response) => {
          console.log("User data saved successfully", response);
        },
        (error) => {
          console.error("Error saving user data", error);
        }
      );
    }
    
  onSignOutClick() {
    this.angularAuth.signOut()
      .then(() => {
        // Réinitialiser user$ à un observable vide
        this.user$ = of({ user: null });
        this.router.navigate(['/google']);
        console.log("Successfully signed out");
      })
      .catch((error) => {
        console.error("Error signing out", error);
      });
  }
}
