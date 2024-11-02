import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CalendrierServiceService {
  private ModifierEventSubject: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  public Modifier$: Observable<boolean> = this.ModifierEventSubject.asObservable();

  constructor(private http:HttpClient) { }
  deleteEvent(eventObj: any): Observable<string> {
    return this.http.delete('http://localhost:8082/calendar/delete', { body: eventObj, responseType: 'text' });
}


modifierEvent(title: string, start: Date, end: Date, eventObj: any): Observable<string> {
  const url = `http://localhost:8082/calendar/modifier?title=${title}&start=${start.toISOString()}&end=${end.toISOString()}`;
  
  return this.http.put<string>(url, eventObj, { responseType: 'json' });
}
SetModifierEvent(value: boolean): void {
  this.ModifierEventSubject.next(value);
  
}


}
