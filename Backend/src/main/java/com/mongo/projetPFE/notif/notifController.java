package com.mongo.projetPFE.notif;

import com.mongo.projetPFE.Calendrier.EntityCalender;
import com.mongo.projetPFE.Calendrier.RepositoryCalender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@RestController
public class notifController {
    @Autowired
    private NotifRepo notifRepo;

    @Autowired
    private RepositoryCalender repositoryCalender;

    @GetMapping("/notifications")
    public List<notifEntity> getNotifications() {
        List<notifEntity> notifications = (List<notifEntity>) notifRepo.findAll();
        Collections.reverse(notifications); // Inverser la liste des notifications
        return notifications;
    }



}
