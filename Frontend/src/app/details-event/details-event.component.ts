import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { CalendarEvent } from 'angular-calendar';
import { CalendrierServiceService } from '../calendrier-service.service';
import { CalenderComponent } from '../calender/calender.component'; // Importez le composant CalendrierComponent
import { UserService } from '../service/user.service';


@Component({
  selector: 'app-details-event',
  templateUrl: './details-event.component.html',
  styleUrls: ['./details-event.component.css']
})
export class DetailsEventComponent implements OnInit {
  eventObj: EVENT;
  modifier:boolean=false;
 
  constructor(
    public dialogRef: MatDialogRef<DetailsEventComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { event: any},private calenderSER:CalendrierServiceService,private userService: UserService
  ) {
    
    this.eventObj= new EVENT(); }
  ngOnInit(): void {
    throw new Error('Method not implemented.');
  }
  closeDialog(): void {
    this.dialogRef.close();
    this.calenderSER.SetModifierEvent(true);
    
    
  }
  modifierhoraire(){
    this.modifier=true;
  }
  message:String='';
  affiche:boolean=false;

  deleteEvent():void{
    this.calenderSER.SetModifierEvent(true);

    const eventObj={
      start:this.data.event.event.start,
      end: this.data.event.event.end
    }
    this.calenderSER.deleteEvent(eventObj).subscribe(
      response=>{
        console.log(response);
        this.message=response;
        this.affiche=true


      },
      error=>{
        console.log(error);
      }

    )
  }
  message2:String='';
  affiche2:boolean=false;


  modifierEvent(): void {
    this.calenderSER.SetModifierEvent(true);

    
    const eventObj1={
      title:this.data.event.event.title,
      start:this.data.event.event.start,
      end: this.data.event.event.end
    }
  

    this.calenderSER.modifierEvent(eventObj1.title,eventObj1.start, eventObj1.end, this.eventObj)
    .subscribe({
      next: (response) => {
        this.message2 = response;
        console.log(response); 
        this.affiche2 = true;
      },
      error: (error) => {
        console.error(error);
        this.message2 = error.error; 
        this.affiche2 = true;
      }
    });

  
  }
 
}
export class EVENT {
  start : Date | undefined ; 
  end: Date | undefined ;
}
