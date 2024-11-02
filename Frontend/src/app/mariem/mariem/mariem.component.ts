import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-mariem',
  templateUrl: './mariem.component.html',
  styleUrls: ['./mariem.component.css']
})
export class MariemComponent {
constructor(private router: Router,  ){

}

  abc():void{
    this.router.navigateByUrl("/navbar");
    return;
  }

  hello():void{

    return ; 

  }






}
