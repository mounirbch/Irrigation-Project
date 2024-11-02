import { Component, OnInit } from '@angular/core';
import { Notification1Service } from './notification1.service';

@Component({
  selector: 'app-notification',
  templateUrl: './notification.component.html',
  styleUrls: ['./notification.component.css']
})
export class NotificationComponent implements OnInit {
  notifications: any[] = [];
  constructor(private notificationService: Notification1Service) { }
  ngOnInit(): void {
    this.loadNotifications();
  }
  loadNotifications() {
    this.notificationService.getNotifications().subscribe(
      (data) => {
        this.notifications = data;
      },
      (error) => {
        console.error('Une erreur s\'est produite lors de la récupération des notifications : ', error);
      }
    );
  }

}
