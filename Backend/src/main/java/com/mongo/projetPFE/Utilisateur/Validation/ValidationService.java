package com.mongo.projetPFE.Utilisateur.Validation;

import com.mongo.projetPFE.Utilisateur.Notification.NotificationService;
import com.mongo.projetPFE.Utilisateur.Utilisateur;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;

@Service
@AllArgsConstructor
public class ValidationService {
    private ValidationRepository validationRepository;
    private NotificationService notificationService;
    public void enregistrer(Utilisateur utilisateur){
        Validation validation=new Validation();
        validation.setUtilisateur(utilisateur);
        Instant creation=Instant.now();
        Instant expiration = creation.plus(10, ChronoUnit.MINUTES);// Ajoute 10 minutes à l'heure actuelle
        validation.setExpiration(expiration);
        Random random =new Random();
        int randomInteger=random.nextInt(999999);
        String code= String.format("%06d",randomInteger);
        validation.setCode(code);
        this.validationRepository.save(validation);
        this.notificationService.envoyer(validation);


    }
    public Validation LireEnFonctionDuCode(String code ){
       return this.validationRepository.findByCode(code).orElseThrow(()->new RuntimeException("votre code est invalide"));

    }
}
