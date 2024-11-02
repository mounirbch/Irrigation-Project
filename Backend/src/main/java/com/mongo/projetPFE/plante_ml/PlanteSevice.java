package com.mongo.projetPFE.plante_ml;

import com.mongo.projetPFE.Data.data0.Datamodel;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PlanteSevice {
    private MongoTemplate mongoTemplate;
    public PlanteSevice(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
    @Scheduled(cron = "0 57 20 * * ?")
    public String pred_plante(){
        LocalDate currentDate = LocalDate.now();
        LocalDateTime startOfDay = currentDate.atStartOfDay();
        LocalDateTime endOfDay = currentDate.atStartOfDay().plusDays(1).minusNanos(1);
        Criteria criteria = Criteria.where("receivedDate").gte(startOfDay).lte(endOfDay);
        Query query = new Query(criteria);
        List<Datamodel> dailyData = mongoTemplate.find(query, Datamodel.class);

        double sumTemperature = 0;
        double sumHumidity = 0;
        double sumPh = 0;

        int totalCount = dailyData.size();
        for (Datamodel data : dailyData) {
            sumTemperature += data.getTemperature();
            sumHumidity += data.getHumidity();
            sumPh += data.getPh();

        }

        if (totalCount > 0) {
            double avgTemperature = sumTemperature / totalCount;
            double avgHumidity = sumHumidity / totalCount;
            double avgPh = sumPh / totalCount;

            PlanteModel planteModel = new PlanteModel();
            planteModel.setFinalTemperature(avgTemperature);
            planteModel.setFinalHumidity(avgHumidity);
            planteModel.setFinalPh(avgPh);
            mongoTemplate.save(planteModel);

            System.out.println("resultat saved avec succes " );

            return "Final results saved for " + currentDate;


        }else {
            // seulement pour voir donnees dans console
            System.out.println("No data available for " + currentDate);
            //-------------------------------------------------------


            return "No data available for " + currentDate;


        }

    }
}
