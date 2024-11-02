package com.mongo.projetPFE.notif;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="notif")
public class notifEntity {
    @Id
    String id;
    String message;
    Date dateNotif;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDateNotif() {
        return dateNotif;
    }

    public void setDateNotif(Date dateNotif) {
        this.dateNotif = dateNotif;
    }
}