import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HTTP_INTERCEPTORS
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserService } from './service/user.service';

@Injectable()
export class TokenInterceptor implements HttpInterceptor {

  constructor(private UserService: UserService) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    
    console.log(request)
   const token=this.UserService.getToken()

   if (token!== null){
    let clone = request.clone({
      headers:request.headers.set('Authorization',' Bearer ' + token)
    }) 
    console.log(clone)
    return next.handle(clone)
   }


    return next.handle(request);
  }
}
export const TokenInterceptorProvider ={
  provide: HTTP_INTERCEPTORS,
  useClass: TokenInterceptor,
  multi: true
}
