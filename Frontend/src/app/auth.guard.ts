import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { UserService } from './service/user.service';
import { Observable } from 'rxjs';


@Injectable({
  providedIn: 'root'
})


export class AuthGuard implements CanActivate {

  constructor(private authService:UserService, private router: Router){}
  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): boolean {
    const expectedRole = route.data['expectedRole']; 
    const currentUserRole = this.authService.getRole(); 




    if (this.authService.isLogged()) {
      console.log("connecter")
      if (currentUserRole === expectedRole) {
        console.log("ok")
        return true; 
      } else {
        
        return false; 
      }
    } else {
      this.router.navigate(['/navbar']);
      return false; 
    }
  }
  
}
  

