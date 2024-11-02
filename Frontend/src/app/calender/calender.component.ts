import { Component, OnInit } from '@angular/core';


import { CalendarEvent, CalendarView } from 'angular-calendar';
import { isSameDay, isSameMonth } from 'date-fns';
import { UserService } from '../service/user.service';
import { Observable } from 'rxjs';
import { MatDialog, MatDialogConfig, MatDialogRef } from '@angular/material/dialog';
import { StatisticsComponent } from '../statistics/statistics.component';
import { DetailsEventComponent } from '../details-event/details-event.component';
import { CalendrierServiceService } from '../calendrier-service.service';

@Component({
  selector: 'app-calender',
  templateUrl: './calender.component.html',
  styleUrls: ['./calender.component.css']
})
export class CalenderComponent implements OnInit {
  dialogRefConfig: { dialogRef: MatDialogRef<DetailsEventComponent>, dialogConfig: MatDialogConfig } | null = null;

  viewDate: Date = new Date();
  view: CalendarView = CalendarView.Week;
  CalendarView = CalendarView;

  events: CalendarEvent[] = [];
  activeDayIsOpen = false;
  Modifier: boolean=false;
  

  constructor(private userService: UserService,private dialog: MatDialog,private CalnderSerr: CalendrierServiceService  ){

   const event2={
   title: "Irrigation avec 3 ML",
    start: new Date("2024-03-22T04:30"),
    end: new Date("2024-03-22T17:30"),
  }
  const event3={
    title: "man event",
   
     start: new Date("2024-03-22T17:30"),
     end: new Date("2024-03-22T18:00"),
   }
   const event4={
    title: "Irrigation avec 3 ML",
   
     start: new Date("2024-04-06T08:30"),
     end: new Date("2024-04-06T10:00"),
   }
   const event1 = {
    
    title: "rihem ",
    start: new Date("2024-03-21T10:30"),
    end: new Date("2024-03-21T17:30"),
    draggable:true,
    resizable:{
      beforeStart: true ,
      afterEnd:true 
    }
   }
   this.events.push(event1);
   this.events.push(event2);
   this.events.push(event3);
   this.events.push(event4);
   
  }
  ngOnInit(): void {
    this.userService.getCalendarEvents().subscribe(events => {
      this.events = events;
      console.log(this.events);
    });
    this.CalnderSerr.Modifier$.subscribe((Modifier: boolean) => {
      this.Modifier = Modifier;
        
    if (this.Modifier){
      this.userService.getCalendarEvents().subscribe(events => {
        this.events = events;
        
      }); 
    }
    });
  
    
  
   
  
   
  } 
 
  ShowEvents():void{
  
    
    if (this.Modifier){
      this.userService.getCalendarEvents().subscribe(events => {
        this.events = events;
        
      }); 
    }
  }
  



  showEventDetails(event: any): void {
    console.log("La fonction showEventDetails() est appelée");
    console.log(event);

    this.dialog.open(DetailsEventComponent, {
      width: '400px',
      height: '370px',
      data: { event: event }
    });

  }
  setView(view: CalendarView) {
   this.view = view; 
  }
  dayClicked({ date, events}: {date:Date; events: CalendarEvent[]}): void{
   if(isSameMonth(date, this.viewDate)){
     if (
       (isSameDay(this.viewDate, date) && this.activeDayIsOpen === true) || 
       events.length === 0
     ){
       this.activeDayIsOpen = false;
     } else {
       this.activeDayIsOpen = true;
     }
     this.viewDate =date;
   }
  }
  showEventCoordinates(event: any): void {
    alert(`Coordonnées de l'événement :
    Latitude : ${event.title}
    Longitude : ${event.start}`);
}


}
