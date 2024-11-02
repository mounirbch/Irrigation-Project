import { Component, OnInit } from '@angular/core';

import { chatMessage } from '../chatMessage';

@Component({
  selector: 'app-capteurs',
  templateUrl: './capteurs.component.html',
  styleUrls: ['./capteurs.component.css']
})
export class CapteursComponent implements OnInit {
  ngOnInit(): void {
    throw new Error('Method not implemented.');
  }

}
export class message{
  message:String='';
  user:String='';
}
