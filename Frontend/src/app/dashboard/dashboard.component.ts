import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { UserService } from '../service/user.service';
import { Location } from '@angular/common';


@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  donnee: donnee = new donnee();
  constructor(private http: HttpClient, private userService: UserService){}

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
        
        
      },
      err => console.error(err)
    );
  }
}
export class donnee{

  email:String='';
  adresse:String='';
  telephone:String='';
}

