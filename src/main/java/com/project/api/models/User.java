package com.project.api.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
@Document
public class User {
    
    @MongoId(value = FieldType.OBJECT_ID)
    private String id;

    private String name;   

    @Indexed(unique = true)
    private String email;

    private String password;

    private Date date = new Date();
    
    private String verification = "No";

    private List<Bet> trader = new ArrayList<Bet>();
    
}
