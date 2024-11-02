package com.mongo.projetPFE.Data.Data1;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends CrudRepository<MessageEntity,String> {
}
