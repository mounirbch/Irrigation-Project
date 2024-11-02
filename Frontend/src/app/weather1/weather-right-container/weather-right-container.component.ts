import { Component } from '@angular/core';
import { WeatherService } from '../weather.service';

@Component({
  selector: 'app-weather-right-container',
  templateUrl: './weather-right-container.component.html',
  styleUrls: ['./weather-right-container.component.css']
})
export class WeatherRightContainerComponent {

  constructor(public weatherService: WeatherService){};

  
  //function to control tab values or tab states
  //function to click sur tab today
  onTodayClick(){
    this.weatherService.today = true;
    this.weatherService.week = false;
  }
  //function to sur tab week
  onWeekClick(){
    this.weatherService.today = false;
    this.weatherService.week = true;
  }

  //function to control metric values
  //functionn to click sur metric celcius
  onCelciusClick(){
    this.weatherService.celcius = true;
    this.weatherService.fahrenheit = false;
  }
  //functionn to click sur metric Fahrenheit
  onFahrenheitClick(){
    this.weatherService.celcius = false;
    this.weatherService.fahrenheit = true;
  }
}
