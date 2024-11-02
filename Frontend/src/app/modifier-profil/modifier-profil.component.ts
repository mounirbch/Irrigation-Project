import { Component, OnInit } from '@angular/core';
import { UserService } from '../service/user.service';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';

@Component({
  selector: 'app-modifier-profil',
  templateUrl: './modifier-profil.component.html',
  styleUrls: ['./modifier-profil.component.css']
})
export class ModifierProfilComponent implements OnInit {
 
  retrievedImage: any;
  base64Data: any;
  retrieveResonse: any;
  imageName: any;



  ProfilObj : Profil ;
  donnee: donnee = new donnee();
    imageObj: image;
  httpClient: any;

 
  constructor(private http: HttpClient, private userService: UserService){
    
    this.ProfilObj=new Profil();
    this.imageObj=new image();

  }

  ngOnInit(): void {
    const httpOptions: { headers: HttpHeaders, responseType: 'json' } = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json' 
      }),
      responseType: 'json' 
    };

    this.http.get<any>('http://localhost:8082/get', httpOptions).subscribe(
      (response) => {
       
        this.donnee.email = response.email;
        this.donnee.adresse = response.adresse;
        this.donnee.telephone = response.telephone;
      this.ProfilObj.adresse= this.donnee.adresse.toString();
      this.ProfilObj.telephone=this.donnee.telephone.toString();
     
    

        
        
        
        
        
      },
      err => console.error(err)
    );

  this.http.get('http://localhost:8082/image/get').subscribe(

  res=>{
    
    this.retrieveResonse = res;
     this.base64Data = this.retrieveResonse.picByte;

    this.retrievedImage = 'data:image/jpeg;base64,' + this.base64Data;

  },
  error=>{
    this.retrievedImage = '../../assets/images/user.png';

  }
  )
   






  

  }
  message: string | null = null;
  messageModifierDonnee: string | null = null;
  changer: boolean = false;
  changer1: boolean = false;


  modifierProfil(): void {
    this.userService.ModifierProfil(this.ProfilObj).subscribe({
        next: (response: string) => { 
            this.changer1 = true;
            console.log(response);
            this.messageModifierDonnee = response;
        },
        error: (error) => {
            this.messageModifierDonnee = error.error; 
        }
    });
}
  
  


  selectedFile!: File;

  public onFileChanged(event:any) {


    this.selectedFile = event.target.files[0];


    if (event.target.files){
      var reader = new FileReader();
      reader.readAsDataURL(event.target.files[0]);
      reader.onload=(event:any)=>{
        this.retrievedImage=event.target.result;
        this.changer=true;

      }

    }
    
  }

onUpload() {
  console.log(this.selectedFile);
  
  const uploadImageData = new FormData();
  uploadImageData.append('imageFile', this.selectedFile, this.selectedFile.name);

  this.http.post('http://localhost:8082/image/upload', uploadImageData, { observe: 'response', responseType: 'text' })
    .subscribe((response: HttpResponse<any>) => {
      if (response.status === 200) {
        this.message = 'Image uploaded successfully';
        this.changer=true;
      } else {
        this.message = 'Image not uploaded successfully';
      }
    }, (error) => {
      // En cas d'erreur, gérez l'erreur ici
      console.error('Une erreur s\'est produite lors de l\'envoi de l\'image : ', error);
      this.message = 'Une erreur s\'est produite lors de l\'envoi de l\'image';
    });
}


 





}



export class donnee{

  email:String='';
  adresse:String='';
  telephone:String='';
}
export class Profil {
  adresse: string = ''; // Initialize properties for better data handling
  telephone: string = '';
}
export class image{
  url: string="../../assets/images/user.png";
}



