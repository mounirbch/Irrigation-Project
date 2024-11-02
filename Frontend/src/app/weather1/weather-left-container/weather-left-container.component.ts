import { Component } from '@angular/core';
import { WeatherService } from '../weather.service';



@Component({
  selector: 'app-weather-left-container',
  templateUrl: './weather-left-container.component.html',
  styleUrls: ['./weather-left-container.component.css']
})
export class WeatherLeftContainerComponent {
  constructor(public weatherService : WeatherService){}
  onSearch(location:string){
    this.weatherService.cityName = location;
    this.weatherService.getData();
  }

}
