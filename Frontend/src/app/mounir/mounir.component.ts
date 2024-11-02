import { Component, OnInit } from '@angular/core';
import { UserService } from '../service/user.service';


@Component({
  selector: 'app-mounir',
  templateUrl: './mounir.component.html',
  styleUrls: ['./mounir.component.css']
})
export class MounirComponent implements OnInit {

  public qr: string="" ;
  public userList:any[] =[];

  constructor(private userService:UserService) { }

  ngOnInit() {this.getuser();
  }
  getuser(){
    this.userService.getUsers().subscribe(
      result=>{
        this.userList =result;
        console.log(result);
      }
    )
  }

}
