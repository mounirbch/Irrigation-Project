import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../service/user.service';


import { AngularFireAuth } from '@angular/fire/compat/auth';
import firebase from 'firebase/compat/app'; 
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { GoogleAuthProvider } from '@firebase/auth';

// Remove FormsModule import if not used in this component
// import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css'],


})
export class MainComponent implements OnInit {
  user$: Observable<{ user: firebase.User | null; }> = this.angularAuth.authState.pipe(
    map(user => ({ user }))
  );
  [x: string]: any;

  registerObj : register ;// Initialize directly
  validobj:validation;
  modifierPaddwordObj:modifierPassword
  nouveauPasswordObj:nouveauPassword
  connectionObj:connection
  containerActive: boolean = false;

  constructor(private router: Router, private http: HttpClient, private userService:UserService, private angularAuth: AngularFireAuth) {
    this.registerObj=new register();
    this.validobj= new validation();
    this.nouveauPasswordObj= new nouveauPassword();
    this.modifierPaddwordObj=new modifierPassword();
    this.connectionObj=new connection();
  }

  ngOnInit(): void {
   
  }
  onSignInClick() {
    this.angularAuth.signInWithPopup(new GoogleAuthProvider())
      .then((result) => {
        const user = result.user;
        if (user) {
          
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
        const { token, role } = this.userService.extractTokenAndRole(response);
        this.userService.saveToken(token);
        this.userService.saveRole(role);
        this.message4 = 'Connexion réussie !'; 
        this.userService.setIsUserLoggedIn(true);
        const currentUserRole = this.userService.getRole()
        if( role ==="utilisateur"){
          this.router.navigateByUrl('/dashboard/calender');
        }
        else if( role ==="administrateur"){
          this.router.navigateByUrl('/admin/home_admin');
        }
        else{
          this.router.navigateByUrl('/navbar');

        }
        console.log("User data saved successfully", response);
      },
      (error) => {
        console.error("Error saving user data", error);
      }
    );
  }

  goToLogin(): void {
    this.containerActive = false;
  }

  goToRegister(): void {
    this.containerActive = true;
  }

  goToMounir(): void {
    this.router.navigate(['/mounir']); // Remove comma
  }
  message: string | null = null;
  isSuccessMessage: boolean = false;

  
 
  inscription(): void {
    if (!this.registerObj.nom || !this.registerObj.email || !this.registerObj.password) {
      this.message = 'Veuillez remplir tous les champs.';
      this.isSuccessMessage = false;
      return; 
    }



    this.http.post('http://localhost:8082/mounir', this.registerObj, { responseType: 'text' })
      .subscribe({
        next: (response) => {
          this.message = response; 
          this.isSuccessMessage = response === "Inscription réussie"; 
          if (this.isSuccessMessage) {
            
            this.router.navigateByUrl('/accueil');
          }
        },
        error: (error) => {
          this.message = error.error; 
          this.isSuccessMessage = false; 
        }
      });
  }
  message1: string | null = null;
  active: boolean = false;

  validation(): void {
    if (!this.validobj.code) {
      this.message1 = 'Veuillez entrer le code';
     
      return; 
    }



    this.http.post('http://localhost:8082/activation', this.validobj, { responseType: 'text' })
      .subscribe({
        next: (response) => {
          this.message1 = response;
          this.active = response === "Activation réussie veuillez vous-connecter";
         
      
        },
        error: (error) => {
          this.message1 = error.error; 
        }
      });
  }
  changer1: boolean = false;
  changerPassword(event: Event): void {
    event.preventDefault();
    this.changer1 = true;
    // Autre logique
  }
  message2: string | null = null;
  active2: boolean = false;

  changerPasswordd(): void {
    if (!this.modifierPaddwordObj.email) {
      this.message2 = 'Veuillez entrer le code';
     
      return; 
    }



    this.http.post('http://localhost:8082/modifier-mot-de-passe', this.modifierPaddwordObj, { responseType: 'text' })
      .subscribe({
        next: (response) => {
          this.message2 = response;
          this.active2 = response === "utilisateur reconnue"
          
         
      
        },
        error: (error) => {
          this.message2 = error.error; 
        }
      });
  }
  message3: string | null = null;
  active3: boolean = false;


  nouveauPassword(): void {
    this.nouveauPasswordObj.email = this.modifierPaddwordObj.email.toString();
    if ( !this.nouveauPasswordObj.code || !this.nouveauPasswordObj.password) {
      this.message3 = 'Veuillez remplir tous les champs.';
     
      return; // Interrompt l'exécution de la fonction si un champ est vide
    }



    this.http.post('http://localhost:8082/nouveau-mot-de-passe', this.nouveauPasswordObj, { responseType: 'text' })
      .subscribe({
        next: (response) => {
          this.message3 = response;
          this.active3 = response === "mot de passe changé avec succes"
          
         
      
        },
        error: (error) => {
          this.message3 = error.error; // Affiche le message d'erreur dans le div
        // Ce n'est pas un message de succès
        }
      });
  }
  SeConnecter():void{

  this.changer1=false;
  this.active2=false;
  this.active3=false


  }
  //message4: string | null = null;


  isUserLoggedIn: boolean = false;
 /* connection(l:boolean): void {
    if (!this.connectionObj.username || !this.connectionObj.password ) {
      this.message4 = 'Veuillez remplir tous les champs.';
     
      return; // Interrompt l'exécution de la fonction si un champ est vide
    }



    this.http.post('http://localhost:8082/connexion', this.connectionObj, { responseType: 'text' })
      .subscribe({
        next: (response) => {
          this.message4 = response;
          if (this.message4!=="Adresse e-mail incorrecte" && this.message4!=="Mot de passe incorrect" && this.message4!=="Problème d'authentification" && this.message4!=="Échec de l'authentification"    ){
                localStorage.setItem('token',response)
                l=true;
              
                this.isUserLoggedIn=true;
                console.log(this.isUserLoggedIn);
                console.log(l);
                

          }
          

          
          
         
      
        },
        error: (error) => {
          this.message4 = error.error; // Affiche le message d'erreur dans le div
        // Ce n'est pas un message de succès
        }
      });
  }*/

  message4: string = '';
  login(connectionObj:any): void {
    if (!this.connectionObj.username || !this.connectionObj.password ) {
      this.message4 = 'Veuillez remplir tous les champs.';
     
      return; // Interrompt l'exécution de la fonction si un champ est vide
    }

    this.userService.login(connectionObj).subscribe({
        next: (response) => {
          if (typeof response === 'string'){
            this.message4 = response ; //fare affichage de probleme dans div
          }else{
            const { token, role } = this.userService.extractTokenAndRole(response);
            this.userService.saveToken(token);
            this.userService.saveRole(role);
            this.message4 = 'Connexion réussie !'; 
            this.userService.setIsUserLoggedIn(true);
            //console.log('Connexion réussie !');
            const currentUserRole = this.userService.getRole()
            if( role ==="utilisateur"){
              this.router.navigateByUrl('/dashboard/home');
            }
            else if( role ==="administrateur"){
              this.router.navigateByUrl('/admin/home_admin');
            }
            else{
              this.router.navigateByUrl('/navbar');

            }
            

          }
       
        },
        error: (error) => {
          this.message4 = error.error; // Affiche le message d'erreur dans le div
         // Ce n'est pas un message de succès
        }
      });
  }

  connection1(l:boolean){
    l=true;
  }
 
  
  
  


}

  

export class register {
  nom: string = ''; // Initialize properties for better data handling
  email: string = '';
  password: string = '';
}
export class validation {
 code : String='';

}
export class modifierPassword{
 email:String='';
}
export class nouveauPassword{
  email:string='';
  code:string='';
  password:string='';

}
export class connection{
  username:String='';
  password:String='';
}
