package com.mongo.projetPFE.Data.Data1;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("message")
public class MessageEntity {
    @Id
    String id;
    String message;
    String user;
    public MessageEntity(String message, String user) {
        this.message = message;
        this.user = user;
    }

}

