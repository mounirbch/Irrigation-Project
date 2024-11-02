package com.mongo.projetPFE.Data.data0;

import com.mongo.projetPFE.Data.data2.Repository;
import com.mongo.projetPFE.Data.data2.entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class IOTController {

    private final DataRepository repo;
    private final SimpMessagingTemplate messagingTemplate;
    @Autowired
    public IOTController(DataRepository repo, SimpMessagingTemplate messagingTemplate) {
        this.repo = repo;
        this.messagingTemplate = messagingTemplate;
    }




    @PostMapping("/iot")
    public ResponseEntity<String> receiveData(@RequestBody Datamodel data){
        System.out.println("donnees est recues " + data);
        repo.save(data);
        messagingTemplate.convertAndSend("/topic/newData", data);
        return ResponseEntity.ok("donnees sont recus avec succes");
    }

    @GetMapping("/getting")
    public List<Datamodel> gett(){
        List<Datamodel> entityList = new ArrayList<>();
        this.repo.findAll().forEach(entityList::add);
        return entityList;

    }

    

}
