import { Injectable } from '@angular/core';
import { Stomp } from '@stomp/stompjs';
import * as SockJS from 'sockjs-client';
import { Observable, Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class Statistics1Service {
  private stompClient: any;
  private newDataSubject = new Subject<any>();

  constructor() { 
    this.initConnectionSocket();
  }

  initConnectionSocket(): void {
    const url='http://localhost:8082/chat-socket';
    const socket = new SockJS(url);
    this.stompClient = Stomp.over(socket)
    this.stompClient.connect({}, () => {
      console.log('WebSocket connected successfully');
      this.stompClient.subscribe('/topic/newData', (message: any) => {
        this.newDataSubject.next(JSON.parse(message.body));
      });
    }, (error: any) => {
      console.error('WebSocket connection failed:', error);
    });
  }

  getNewDataObservable(): Observable<any> {
    return this.newDataSubject.asObservable();
  }
}
