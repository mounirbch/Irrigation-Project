import { Component, OnInit } from '@angular/core';
import { AdminServiceService } from './admin-service.service';
import { HttpClient } from '@angular/common/http';
import Swal from 'sweetalert2';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ModifierProfilComponent } from '../modifier-profil/modifier-profil.component';
import { ProfileComponent } from '../profile/profile.component';
import { ScrollStrategyOptions } from '@angular/cdk/overlay';

@Component({
  selector: 'app-home-admin',
  templateUrl: './home-admin.component.html',
  styleUrls: ['./home-admin.component.css']
})
export class HomeAdminComponent implements OnInit {
  retrievedImage: any;
  base64Data: any;
  retrieveResonse: any;
  utilisateurs: any[] = [];
  utilisateurs1: any[] = [];


  dialogRefConfig: { dialogConfig: MatDialogConfig; dialogRef: any; } | undefined;
  isSmallScreen: any;
  archive:boolean=false;


  constructor(private http: HttpClient, private adminService: AdminServiceService,private dialog: MatDialog,  private scrollStrategyOptions: ScrollStrategyOptions) { }

  ngOnInit(): void {
   
      this.fetchUsers();
    
  }

  async fetchImage(email: string): Promise<string> {
    let imageUrl = '';
    try {
      const res: any = await this.http.get<any>('http://localhost:8082/image/get-by-email/' + email).toPromise();
      if (res && res.picByte) {
        this.base64Data = res.picByte;
        this.retrievedImage = 'data:image/jpeg;base64,' + this.base64Data;
        imageUrl = this.retrievedImage;
      } else {
        this.retrievedImage = '../../assets/images/user.png';
        imageUrl = this.retrievedImage;
      }
    } catch (error) {
      console.error('Erreur lors de la récupération de l\'image :', error);
      this.retrievedImage = '../../assets/images/user.png';
      imageUrl = this.retrievedImage;
    }
    return imageUrl;
  }

 /* async fetchImage1(email: string): Promise<string> {
    let imageUrl = '';
    try {
      const res: any = await this.http.get<any>('http://localhost:8082/image/get-by-email/' + email).toPromise();
      if (res && res.picByte) {
        this.base64Data = res.picByte;
        this.retrievedImage = 'data:image/jpeg;base64,' + this.base64Data;
        imageUrl = this.retrievedImage;
      } else {
        this.retrievedImage = '../../assets/images/user.png';
        imageUrl = this.retrievedImage;
      }
    } catch (error) {
      console.error('Erreur lors de la récupération de l\'image :', error);
      this.retrievedImage = '../../assets/images/user.png';
      imageUrl = this.retrievedImage;
    }
    return imageUrl;
  }*/

  fetchUsers(): void {
    this.adminService.getUtilisateur().subscribe(
      async (data: any[]) => {
        for (let utilisateur of data) {
          utilisateur.image = await this.fetchImage(utilisateur.email);
        }
        
        this.utilisateurs = data;
     
      },
      error => {
        console.error('Erreur lors de la récupération des utilisateurs :', error);
      }
    );
    this.archive=false;
  }
  fetchUsersArchive(): void {
    this.adminService.getUtilisateurArchive().subscribe(
      async (data: any[]) => {
      
        this.utilisateurs1 = data;
        for (let utilisateur of data) {
          utilisateur.image = await this.fetchImage(utilisateur.email);
        }
      },
      error => {
        console.error('Erreur lors de la récupération des utilisateurs :', error);
      }
    );
    this.archive=true;
  }

  ArchiverUtilisateur(email: string): void {
    this.adminService.ArchiveUtilisateur(email).subscribe(
        (response:any) =>{
            this.showSuccess(); 
            this.fetchUsers(); 
        },
        (error: any) => {
            console.error('Erreur lors de la suppression de l\'utilisateur', error);
            this.showError();
        }
    );
}
DesarchiveUtilisateur(email: string): void {
  this.adminService.DesarchiverUtilisateur(email).subscribe(
      (response:any) =>{
          this.showSuccess1(); 
          this.fetchUsers(); 
      },
      (error: any) => {
          console.error('Erreur lors de la suppression de l\'utilisateur', error);
          this.showError1();
      }
  );
}


  AreySure(email: string): void {
    Swal.fire({
      title: "Etes-vous sure ?",
      text: "cet utilisateur sera archivé(e)",
      icon: "warning",
      showCancelButton: true,
      confirmButtonText: 'oui,archiver!',
      cancelButtonText: 'No, garder le',
       confirmButtonColor: '#dc3545' ,
    }).then((result) => {
      if (result.isConfirmed) {
        this.ArchiverUtilisateur(email);
      }
    });
  }

  showSuccess(): void {
    Swal.fire({
      title: "Bon travail!",
      text: "Utilisateur archivé(e) avec succès",
      icon: "success"
    });
  }

  showError(): void {
    Swal.fire({
      icon: "error",
      title: "Oops...",
      text: "Quelque chose s'est mal passé!",
    });
  }



  AreySure1(email: string): void {
    Swal.fire({
      title: "Etes-vous sure ?",
      text: "cet utilisateur sera desarchivé(e)",
      icon: "warning",
      showCancelButton: true,
      confirmButtonText: 'oui,Desarchiver!',
      cancelButtonText: 'No, garder le',
       confirmButtonColor: '#dc3545' ,
    }).then((result) => {
      if (result.isConfirmed) {
        this.DesarchiveUtilisateur(email);
      }
    });
  }
  showSuccess1(): void {
    Swal.fire({
      title: "Bon travail!",
      text: "Utilisateur desarchivé(e) avec succès",
      icon: "success"
    });
  }

  showError1(): void {
    Swal.fire({
      icon: "error",
      title: "Oops...",
      text: "Quelque chose s'est mal passé!",
    });
  }

  



  openModel(utilisateurEmail: string): void {
    const dialogConfig = new MatDialogConfig();
    
 
      dialogConfig.maxWidth = '100vw'; 
      dialogConfig.maxHeight = '100vh'; 
    
  
    dialogConfig.position = { top: '60px' };
    dialogConfig.panelClass = 'custom-dialog-container';
  
    dialogConfig.data = { email: utilisateurEmail };

    
    this.dialog.open(ProfileComponent, dialogConfig);
  }

}
