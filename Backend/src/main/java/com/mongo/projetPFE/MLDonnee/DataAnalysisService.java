package com.mongo.projetPFE.MLDonnee;

import com.mongo.projetPFE.Data.data0.Datamodel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DataAnalysisService {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Scheduled(cron = "0 30 17 * * *")
    public String calculateDailyAverages() {

        LocalDate currentDate = LocalDate.now();
        LocalDateTime startOfDay = currentDate.atStartOfDay();
        LocalDateTime endOfDay = currentDate.atStartOfDay().plusDays(1).minusNanos(1);
        Criteria criteria = Criteria.where("receivedDate").gte(startOfDay).lte(endOfDay);
        Query query = new Query(criteria);
        List<Datamodel> dailyData = mongoTemplate.find(query, Datamodel.class);


        double sumTemperature = 0;
        double sumHumidity = 0;
        double sumSoilHumidity = 0;
        double sumPh = 0;
        double minTemperature = Double.MAX_VALUE;
        double maxTemperature = Double.MIN_VALUE;
        double minHumidity = Double.MAX_VALUE;
        double maxHumidity = Double.MIN_VALUE;
        double minSoilHumidity = Double.MAX_VALUE;
        double maxSoilHumidity = Double.MIN_VALUE;
        double minPh = Double.MAX_VALUE;
        double maxPh = Double.MIN_VALUE;
        int totalCount = dailyData.size();
        for (Datamodel data : dailyData) {
            sumTemperature += data.getTemperature();
            sumHumidity += data.getHumidity();
            sumSoilHumidity += data.getSoilHumidity();
            sumPh += data.getPh();

            minTemperature = Math.min(minTemperature, data.getTemperature());
            maxTemperature = Math.max(maxTemperature, data.getTemperature());
            minHumidity = Math.min(minHumidity, data.getHumidity());
            maxHumidity = Math.max(maxHumidity, data.getHumidity());
            minSoilHumidity = Math.min(minSoilHumidity, data.getSoilHumidity());
            maxSoilHumidity = Math.max(maxSoilHumidity, data.getSoilHumidity());
            minPh = Math.min(minPh, data.getPh());
            maxPh = Math.max(maxPh, data.getPh());
        }


        if (totalCount > 0) {
            double avgTemperature = sumTemperature / totalCount;
            double avgHumidity = sumHumidity / totalCount;
            double avgSoilHumidity = sumSoilHumidity / totalCount;
            double avgPh = sumPh / totalCount;


            double finalTemperature = (avgTemperature - minTemperature) / (maxTemperature - minTemperature);
            double finalHumidity = (avgHumidity - minHumidity) / (maxHumidity - minHumidity);
            double finalSoilHumidity = (avgSoilHumidity - minSoilHumidity) / (maxSoilHumidity - minSoilHumidity);
            double finalPh = (avgPh - minPh) / (maxPh - minPh);



            System.out.println("Daily Averages for " + currentDate);
            System.out.println("Average Temperature: " + avgTemperature);
            System.out.println("Average Humidity: " + avgHumidity);
            System.out.println("Average Soil Humidity: " + avgSoilHumidity);
            System.out.println("Average pH: " + avgPh);
            //----------------------------------------------------

            System.out.println("Min Temperature: " + minTemperature);
            System.out.println("Max Temperature: " + maxTemperature);
            System.out.println("Min Humidity: " + minHumidity);
            System.out.println("Max Humidity: " + maxHumidity);
            System.out.println("Min Soil Humidity: " + minSoilHumidity);
            System.out.println("Max Soil Humidity: " + maxSoilHumidity);
            System.out.println("Min pH: " + minPh);
            System.out.println("Max pH: " + maxPh);
            //--------------------------------------------------------------

            System.out.println("Final Result for Temperature: " + finalTemperature);
            System.out.println("Final Result for Humidity: " + finalHumidity);
            System.out.println("Final Result for Soil Humidity: " + finalSoilHumidity);
            System.out.println("Final Result for pH: " + finalPh);

            /*return "Daily Averages for " + currentDate + "\n" +
                    "Average Temperature: " + avgTemperature + "\n" +
                    "Average Humidity: " + avgHumidity + "\n" +
                    "Average Soil Humidity: " + avgSoilHumidity + "\n" +
                    "Average pH: " + avgPh + "\n" +
                    "Min Temperature: " + minTemperature + "\n" +
                    "Max Temperature: " + maxTemperature + "\n" +
                    "Min Humidity: " + minHumidity + "\n" +
                    "Max Humidity: " + maxHumidity + "\n" +
                    "Min Soil Humidity: " + minSoilHumidity + "\n" +
                    "Max Soil Humidity: " + maxSoilHumidity + "\n" +
                    "Min pH: " + minPh + "\n" +
                    "Max pH: " + maxPh + "\n" +
                    "Final Result for Temperature" + finalTemperature + "\n" +
                    "Final Result for Humidity: " + finalHumidity + "\n" +
                    "Final Result for Soil Humidity: " + finalSoilHumidity + "\n" +
                    "Final Result for pH: " + finalPh ;*/


            FinalResult finalResult = new FinalResult();
            finalResult.setFinalTemperature(finalTemperature);
            finalResult.setFinalHumidity(finalHumidity);
            finalResult.setFinalSoilHumidity(finalSoilHumidity);
            finalResult.setFinalPh(finalPh);
            mongoTemplate.save(finalResult);


            return "Final results saved for " + currentDate;



        } else {

            System.out.println("No data available for " + currentDate);
            //-------------------------------------------------------



            return "No data available for " + currentDate;


        }



    }


}

