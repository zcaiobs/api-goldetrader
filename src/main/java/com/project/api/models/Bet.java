package com.project.api.models;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
@Document
public class Bet {

    @MongoId(value = FieldType.OBJECT_ID)
    private String id = new ObjectId().toString();
    private String event;
    private String championship;
    private String date;
    private Double stake;
    private Double result;
    private Double odd;
    private String strategy;
    private String description;

}
