package com.mongo.projetPFE.Data.data2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")

public class Controlleur {
    private final Repository repo;
    private final SimpMessagingTemplate messagingTemplate;
    @Autowired
    public Controlleur(Repository repo, SimpMessagingTemplate messagingTemplate) {
        this.repo = repo;
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping("/adding")
    public entity ajouter(@RequestBody entity entity){
        Date currenteDte=new Date();
        entity.setDate(currenteDte);
        this.repo.save(entity);
        messagingTemplate.convertAndSend("/topic/newData", entity);

        return  entity;
    }
    @GetMapping("/getting")
    public List<entity> gett(){
        List<entity> entityList = new ArrayList<>();
        this.repo.findAll().forEach(entityList::add);
       return entityList;

    }
}
