package com.mongo.projetPFE.Utilisateur.Notification;

import com.mongo.projetPFE.Utilisateur.Validation.Validation;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class NotificationService {
    JavaMailSender javaMailSender;

    public void envoyer (Validation validation){
        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom("bouchaalamounir9@gmail.com");
        message.setTo(validation.getUtilisateur().getEmail());
        message.setSubject("votre Code d'activation");

       String texte= String.format("bonjour  %S <br/> %S votre code d'activation",
               validation.getUtilisateur().getNom(),
               validation.getCode()
           );

        message.setText(texte);
        javaMailSender.send(message);

        }
}
