import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { MainComponent } from './main/main.component';
import { UserService } from './service/user.service';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { NAVBARComponent } from './navbar/navbar.component';

interface SideNavToggle {
  screenWidht: number;
  collapsed: boolean;
}

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  [x: string]: any;
  title = 'last_one_pfe';
  isSideNavCollapsed = false;
  screenWidht = 0;
  a: boolean = false;
  isUserLoggedIn: boolean = true;
  l: boolean = false;
  @ViewChild(MainComponent) mainComponent!: MainComponent;
  @ViewChild(NAVBARComponent) navbarComponent!: NAVBARComponent; // Accédez au composant NavbarComponent


  constructor(
    private router: Router,

    private main: MainComponent,
    private cdr: ChangeDetectorRef,
    private userService: UserService,
    private dialog: MatDialog

    // Injectez le MainComponent
  ) { }
  ngOnInit(): void {



    this.userService.isUserLoggedIn$.subscribe((isLoggedIn: boolean) => {
      this.isUserLoggedIn = isLoggedIn;
    });
    console.log(this.isUserLoggedIn);











  }



  onToggLeSideNav(data: SideNavToggle): void {
    this.screenWidht = data.screenWidht;
    this.isSideNavCollapsed = data.collapsed;
  }




}
