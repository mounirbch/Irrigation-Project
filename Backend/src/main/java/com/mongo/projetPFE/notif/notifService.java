package com.mongo.projetPFE.notif;

import com.mongo.projetPFE.Calendrier.EntityCalender;
import com.mongo.projetPFE.Calendrier.RepositoryCalender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class notifService { // Nom de classe en camelCase

    @Autowired
    private NotifRepo notifRepo;

    @Autowired
    private RepositoryCalender repositoryCalender;

    @Scheduled(cron = "0 * * * * *")
    public void checkEventTimeInterval() {
        deleteEventsBeforeDate(new Date());
        Date currentTime = new Date();


        Iterable<EntityCalender> events = repositoryCalender.findAll();

        Calendar currentCal = Calendar.getInstance();
        currentCal.setTime(currentTime);
        currentCal.set(Calendar.SECOND, 0);

        for (EntityCalender event : events) {
            Calendar startCal = Calendar.getInstance();
            startCal.setTime(event.getStart());
            startCal.set(Calendar.SECOND, 0);

            Calendar endCal = Calendar.getInstance();
            endCal.setTime(event.getEnd());
            endCal.set(Calendar.SECOND, 0);

            // Comparaison en ignorant les secondes
            if (currentCal.get(Calendar.YEAR) == startCal.get(Calendar.YEAR) &&
                    currentCal.get(Calendar.MONTH) == startCal.get(Calendar.MONTH) &&
                    currentCal.get(Calendar.DAY_OF_MONTH) == startCal.get(Calendar.DAY_OF_MONTH) &&
                    currentCal.get(Calendar.HOUR_OF_DAY) == startCal.get(Calendar.HOUR_OF_DAY) &&
                    currentCal.get(Calendar.MINUTE) == startCal.get(Calendar.MINUTE)) {
                System.out.println("NOTIFICATION ouverte");

                notifEntity entityStart = new notifEntity();
                entityStart.setMessage("La pompe a eau commence l'irrigation");
                entityStart.setDateNotif(new Date());
                notifRepo.save(entityStart);
            } else if (currentCal.get(Calendar.YEAR) == endCal.get(Calendar.YEAR) &&
                    currentCal.get(Calendar.MONTH) == endCal.get(Calendar.MONTH) &&
                    currentCal.get(Calendar.DAY_OF_MONTH) == endCal.get(Calendar.DAY_OF_MONTH) &&
                    currentCal.get(Calendar.HOUR_OF_DAY) == endCal.get(Calendar.HOUR_OF_DAY) &&
                    currentCal.get(Calendar.MINUTE) == endCal.get(Calendar.MINUTE)) {
                System.out.println("NOTIFICATION fermé");
                notifEntity entityEnd = new notifEntity();
                entityEnd.setMessage("La pompe a eau s'est fermé");
                entityEnd.setDateNotif(new Date());
                notifRepo.save(entityEnd);


            }
        }
    }
    private void deleteEventsBeforeDate(Date endDate) {
        Iterable<EntityCalender> events = repositoryCalender.findAll();
        for (EntityCalender event : events) {
            if (event.getEnd().before(endDate)) {
                repositoryCalender.delete(event);
            }
        }
    }

}
