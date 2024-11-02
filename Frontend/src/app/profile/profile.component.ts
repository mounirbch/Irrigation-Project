import { Component, Inject, OnInit } from '@angular/core';
import { AdminServiceService } from '../home-admin/admin-service.service';
import { MAT_DIALOG_DATA, MatDialogConfig, MatDialogRef } from '@angular/material/dialog';
import { HttpClient } from '@angular/common/http';
import { DetailsEventComponent } from '../details-event/details-event.component';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  retrievedImage: any;
  base64Data: any;
  retrieveResonse: any;
  donnee: donnee = new donnee();
 ;
  ngOnInit(): void {
    const email = this.data.email;
    this.getEmailDetails(email); 
    this.fetchImage(email);
 }
  constructor(    @Inject(MAT_DIALOG_DATA) public data: any,public dialogRef: MatDialogRef<DetailsEventComponent>,
  private adminService: AdminServiceService,private http: HttpClient) { }

  getEmailDetails(email: string): void {
    this.adminService.getUtilisateurParMail(email).subscribe(
      (response) => {
        console.log('Détails de l\'utilisateur par e-mail:', response);
this.donnee.email=response.email;
this.donnee.adresse=response.adresse;
this.donnee.telephone=response.telephone;


},
      (error) => {
        console.error('Erreur lors de la récupération des détails de l\'utilisateur:', error);
      }
    );
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
  closeDialog(): void {
    this.dialogRef.close();
    
    
  }


}
export class donnee{

  email:String='';
  adresse:String='';
  telephone:String='';
}
