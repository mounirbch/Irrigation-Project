/*import { Injectable } from '@angular/core';
import { LocationDetails } from './Models/LocationDetails';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { WeatherDetails } from './Models/WeatherDetails';
import { TemperatureData } from './Models/TemperatureData';
import { TodayData } from './Models/TodayData';
import { WeekData } from './Models/WeekData';
import { TodaysHighlight } from './Models/TodaysHighlight';
import { Observable } from 'rxjs';
import { EnvironmentalVariables } from './Environment/EnvironmentVariables';
import { query } from '@angular/animations';*/

import { Injectable, OnInit } from "@angular/core";
import { LocationDetails } from "./Models/LocationDetails";
import { WeatherDetails } from "./Models/WeatherDetails";
import { TemperatureData } from "./Models/TemperatureData";
import { TodayData } from "./Models/TodayData";
import { WeekData } from "./Models/WeekData";
import { TodaysHighlight } from "./Models/TodaysHighlight";
import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Observable } from "rxjs";
import { EnvironmentalVariables } from "./Environment/EnvironmentVariables";



@Injectable({
  providedIn: 'root'
})
export class WeatherService {


  //variables which will be filled by api endpoint
  locationDetails?: LocationDetails;
  weatherDetails: WeatherDetails;

  //variables that have the extracted data from the api endpoint varibles
  temperatureDtata: TemperatureData = new TemperatureData();; //left-weather-container data

  //right-weather-container data
  todayData: TodayData[] = [];
  WeekData: WeekData[] = [];
  todaysHighlight: TodaysHighlight = new TodaysHighlight();;

  //variables to be used for api calls
  cityName:string = 'tunis';
  language:string = 'en-US';
  date:string ='20200622';
  units:string ='m'

  //variable holding current time
  currentTime:Date;

  //variable to control tabs
  today:boolean = false;
  week:boolean =true;

  //variable to control metric value
  celcius:boolean = true;
  fahrenheit:boolean = false;
  temperatureData: any;


  constructor(private httpClient: HttpClient) {
    this.getData();
    this.todayData = [];
    this.WeekData = [];
    this.temperatureDtata = new TemperatureData();
    this.todaysHighlight = new TodaysHighlight();
    this.currentTime = new Date();
    this.weatherDetails = {} as WeatherDetails;
    
  }


  getSummaryImage(summary : string):string{
    //base folder adress containing the images
    var baseAddress = 'assets/';
    //respective image name
    var cloudySunny ='cloudy.png';
    var rainSunny ='rainy-day.png';
    var windy ='windy.png';
    var sunny ='sun.png';
    var rainy ='rain.png';

    if(String(summary).includes("Partly Cloudy") || String(summary).includes('P Cloudy'))return baseAddress + cloudySunny;
    else if(String(summary).includes("Partly Rainy") || String(summary).includes('P Rainy'))return baseAddress + rainSunny;
    else if(String(summary).includes("wind"))return baseAddress + windy;
    else if(String(summary).includes("rain"))return baseAddress + rainy;
    else if(String(summary).includes("sun"))return baseAddress + sunny;

    return baseAddress + cloudySunny;
  }
//methode to create a chunk for left weather container using model temperature data
  fillTemeratureDataModel(){
    this.currentTime = new Date();
    this.temperatureDtata.day = this.weatherDetails['v3-wx-observations-current'].dayOfWeek;
    this.temperatureDtata.time = `${String(this.currentTime.getHours()).padStart(2,'0')}:${String(this.currentTime.getMinutes()).padStart(2,'0')}`//.getH()
    this.temperatureDtata.temperature = this.weatherDetails['v3-wx-observations-current'].temperature;
    //this.temperatureDtata.location = `${this.locationDetails.location.city[0]},${this.locationDetails.location.country}`//
    this.temperatureDtata.rainPercent = this.weatherDetails['v3-wx-observations-current'].precip24Hour;
    this.temperatureDtata.summaryPhrase= this.weatherDetails['v3-wx-observations-current'].cloudCoverPhrase;
    this.temperatureDtata.summaryImage= this.getSummaryImage(this.temperatureDtata.summaryPhrase);
  }

  //methode to create a chunk for right weather container using model week data
  fillWeekData(){
    var weekCount = 0;
    while(weekCount < 7){
      this.WeekData.push(new WeekData());
      this.WeekData[weekCount].day = this.weatherDetails['v3-wx-forecast-daily-15day'].dayOfWeek[weekCount].slice(0,3);
      this.WeekData[weekCount].tempMax = this.weatherDetails['v3-wx-forecast-daily-15day'].calendarDayTemperatureMax[weekCount];
      this.WeekData[weekCount].tempMin = this.weatherDetails['v3-wx-forecast-daily-15day'].calendarDayTemperatureMin[weekCount];
      this.WeekData[weekCount].summaryImage = this.getSummaryImage(this.weatherDetails['v3-wx-forecast-daily-15day'].narrative[weekCount]);

      weekCount++ ;
    }
    
  }

  fillTodayData(){
    var todayCount = 0;
    while(todayCount < 7){
      this.todayData.push(new TodayData());
      this.todayData[todayCount].time = this.weatherDetails['v3-wx-forecast-hourly-10day'].validTimeLocal[todayCount].slice(11,16);
      this.todayData[todayCount].temperature = this.weatherDetails['v3-wx-forecast-hourly-10day'].temperature[todayCount];
      this.todayData[todayCount].summaryImage = this.getSummaryImage(this.weatherDetails['v3-wx-forecast-hourly-10day'].wxPhraseShort[todayCount]);
      todayCount++ ;
    }
  }

  getTimeFromString(localeTime:string){
    return localeTime.slice(11,16);
  }

  //methode to get today's highlight data from the base variable
  fillTodayHighlight(){
    this.todaysHighlight.airQuality = this.weatherDetails['v3-wx-globalAirQuality'].globalairquality.airQualityCategoryIndex;
    this.todaysHighlight.humidity = this.weatherDetails['v3-wx-observations-current'].relativeHumidity;
    this.todaysHighlight.sunrise = this.getTimeFromString(this.weatherDetails['v3-wx-observations-current'].sunriseTimeLocal);
    this.todaysHighlight.sunset = this.getTimeFromString(this.weatherDetails['v3-wx-observations-current'].sunsetTimeLocal);
    this.todaysHighlight.uvIndex = this.weatherDetails['v3-wx-observations-current'].uvIndex;
    this.todaysHighlight.visibility = this.weatherDetails['v3-wx-observations-current'].visibility;
    this.todaysHighlight.windStatus = this.weatherDetails['v3-wx-observations-current'].windSpeed;

  }

  //method to create useful data chunks for api using the data received from the Api
  
  prepareData():void{
    //setting left weather container data model properties
    this.fillTemeratureDataModel();
    this.fillWeekData();
    this.fillTodayData();
    this.fillTodayHighlight();
    console.log(this.weatherDetails);
    console.log(this.temperatureDtata);
    console.log(this.WeekData);
    console.log(this.todayData);
    console.log(this.todaysHighlight);
  }

  celsiusToFahrenheit(celsius:number):number{
    return +((celsius * 1.8) + 32).toFixed(2);
  }
  fahrenheitToCelsius(fahrenheit:number):number{
    return +((fahrenheit-32) * 0.555).toFixed(2);
  }

  //methode to get location details from the api using the variable city name as input
  getLocationDetails(cityName:string,language:string):Observable<LocationDetails>{
    return this.httpClient.get<LocationDetails>(EnvironmentalVariables.weatherApiLocationBaseURL,{
      headers: new HttpHeaders()
      .set(EnvironmentalVariables.xRapidApiKeyName,EnvironmentalVariables.xRapidApiKeyValue)
      .set(EnvironmentalVariables.xRapidHostName,EnvironmentalVariables.xRapidHostValue),
      params: new HttpParams()
      .set('query', cityName)
      .set('language', language)
    })
  }

  getWeatherReport(date:string,latitude:number,longitude:number,language:string,units:string):Observable<WeatherDetails>{
    return this.httpClient.get<WeatherDetails>(EnvironmentalVariables.weatherApiForecastBaseURL,{
      headers: new HttpHeaders()
      .set(EnvironmentalVariables.xRapidApiKeyName,EnvironmentalVariables.xRapidApiKeyValue)
      .set(EnvironmentalVariables.xRapidHostName,EnvironmentalVariables.xRapidHostValue),
      params: new HttpParams()
      .set('date', date)
      .set('latitude', latitude)
      .set('longitude', longitude)
      .set('language', language)
      .set('units', units)
    });

  }

  getData(){
    this.todayData = [];
    this.WeekData =[];
    this.temperatureDtata = new TemperatureData();
    this.todaysHighlight = new TodaysHighlight();
    var latitude= 0;
    var longitude= 0;

    this.getLocationDetails(this.cityName,this.language).subscribe({
      next:(response)=>{
        this.locationDetails = response;
        latitude = this.locationDetails?.location.latitude[0];
        longitude =this.locationDetails?.location.longitude[0];
        

        //once we get the values for latitude and logitude we can call for the getweatherReport method
        this.getWeatherReport(this.date,latitude,longitude,this.language,this.units).subscribe({
          next:(response)=>{
            this.weatherDetails = response;
            this.prepareData();
            
          }
        })
      }
    });


    


  }
}
