package com.mongo.projetPFE.Calendrier;

import com.mongo.projetPFE.Utilisateur.Utilisateur;
import com.mongo.projetPFE.notif.NotifRepo;
import com.mongo.projetPFE.notif.notifEntity;
import com.mongo.projetPFE.security.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;

@Service
public class TacheFinal {
    @Autowired
    private NotifRepo notifRepo;
    @Autowired
    private RepositoryCalender repositoryCalender;
    @Autowired
    private quantityRepository quantityRepository;

    @Autowired
    private JwtFilter jwtFilter;

    private static final String SYSTEM_USER_ID = "Modéle Machine Learning";

    private LocalDateTime previousEventDate;

    @Scheduled(cron = "0 52 11 * * ?")
    public void executerTache() {


        Quantity latestData = quantityRepository. findTopByOrderByTimestampDesc();

        if (latestData != null) {
            double valeur1 = latestData.getValeur();


            int valeur1Entier = (int) valeur1;


            double debit = 240.0;
            int duree1 = (int) Math.floor(valeur1 / debit);


            int duree1Heures = duree1 / 60;
            int duree1Minutes = duree1 % 60;


            LocalDateTime now = LocalDateTime.now();

            LocalDateTime dd1 = now.plusDays(0).withHour(14).withMinute(51).withSecond(0);


            LocalDateTime df1 = dd1.plusHours(duree1Heures).plusMinutes(duree1Minutes);


            saveOrUpdateCalendrier("Irrigation avec " + valeur1Entier + "ML (généré par IA)", dd1, df1);

            Date start1=Date.from(dd1.atZone(ZoneId.systemDefault()).toInstant());
            System.out.println(start1);



            System.out.println("Données enregistrées avec succès dans la collection 'calendrier'.");
        } else {
            System.out.println("La collection 'quantity' est vide.");
        }
    }


    private void saveOrUpdateCalendrier(String title, LocalDateTime start, LocalDateTime end) {

        EntityCalender calendrier = new EntityCalender();
        calendrier.setTitle(title);
        calendrier.setStart(Date.from(start.atZone(ZoneId.systemDefault()).toInstant()));
        calendrier.setEnd(Date.from(end.atZone(ZoneId.systemDefault()).toInstant()));

        Utilisateur systemUser = new Utilisateur();
        systemUser.setId(SYSTEM_USER_ID);
        calendrier.setUtilisateur(systemUser);
        System.out.println("event IA");

        notifEntity entityStart = new notifEntity();
        entityStart.setMessage("Un évenement d'IA a été ajouté automatiquement");
        entityStart.setDateNotif(new Date());
        notifRepo.save(entityStart);


        repositoryCalender.save(calendrier);
    }




}
