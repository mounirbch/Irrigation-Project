package com.mongo.projetPFE.Data.data0;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IOTService{
    @Autowired
    private DataRepository repo;

    public void saveData(Datamodel data) {
        repo.save(data);
    }
}