package com.mongo.projetPFE.Calendrier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Date;

@org.springframework.stereotype.Service
public class ServiceCalender {
    @Autowired
    private RepositoryCalender repositoryCalender;



    public EntityCalender addEvent(EntityCalender event) {
        return repositoryCalender.save(event);
    }

    public int checkEventTimeInterval() {
        Date currentTime = new Date();

        Iterable<EntityCalender> events = repositoryCalender.findAll();

        for (EntityCalender event : events) {
            if (currentTime.after(event.getStart()) && currentTime.before(event.getEnd())) {
                System.out.println("L'heure actuelle est dans l'intervalle d'un événement.");

                return 1;

            }
        }
        System.out.println("L'heure actuelle n'est pas dans l'intervalle d'un événement.");

        return 2;
    }






}
