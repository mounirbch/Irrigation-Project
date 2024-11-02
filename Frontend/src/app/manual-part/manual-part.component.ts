import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../service/user.service';
import { CalendarEvent } from 'angular-calendar';

@Component({
  selector: 'app-manual-part',
  templateUrl: './manual-part.component.html',
  styleUrls: ['./manual-part.component.css']
})
export class ManualPartComponent implements OnInit {
  events: any[] = []; 

  evenementObj :evenement ;
  evenementObjAffich :evenementAffich ;
  evenementObjChoix: evenementChoix;
  constructor(private router:Router, private http:HttpClient,  private userService:UserService){
    this.evenementObj= new evenement();
    this.evenementObjAffich= new evenementAffich();
    this.evenementObjChoix=new evenementChoix();
  }
  ngOnInit(): void {
    const startDate = new Date(); 
    startDate.setHours(startDate.getHours() + 1);

    
    this.evenementObjChoix.start = startDate.toISOString().slice(0, 16); 
    console.log("ngoNIT3t" + this.evenementObjChoix.start);
    
    this.fetchEventsByInterval();
   
  }
 

 
  message4: string | null = null;

  AjouterEvent(): void {
    if (!this.evenementObj.title || !this.evenementObj.start || !this.evenementObj.end) {
      this.message4 = 'Veuillez remplir tous les champs.';
      setTimeout(() => { 
        this.message4 = '';
      }, 3000)
      return; 
    }
    this.userService.AjouterEvent(this.evenementObj).subscribe({
      next: (Response)=>{
        this.message4=Response;
        setTimeout(() => {
          this.message4 = '';
        }, 3000)
        
      },
      error: (error) => {
        this.message4 = error.error; 
      
      }
      
    })
    
  }
  fetchEventsByInterval(): void {
    let start = this.evenementObjChoix.start;
    let end = this.evenementObjChoix.end;
    console.log(start);
    if (start) {
      start += ":00";
  }
  if (end) {
    
    end += ":00";
}
  console.log("mounir"+start)

    if (start && end) {
        this.userService.getEventsByInterval(start, end).subscribe({
            next: (events) => {
                this.events = events;
                console.log(events);
            },
            error: (error) => {
                console.error(error);
            }
        });
    } else if(start){
      this.userService.getEventsByInterval(start).subscribe({
        next: (events) => {
            this.events = events;
            console.log(events);
        },
        error: (error) => {
            console.error(error);
        }
    });
    }
    else {
        console.error("Start and/or end dates are undefined.");
    }
}

 
}

export class evenement {
  title: string = '';
  start: Date | undefined;
  end: Date | undefined;
}
export class evenementAffich {

  start: String | undefined ;
 
}
export class evenementChoix{
  start: string | undefined;
  end: string | undefined;
}
