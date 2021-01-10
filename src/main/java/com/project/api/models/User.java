package com.project.api.models;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class User {
    
    @MongoId(value = FieldType.OBJECT_ID)
    private String id;

    private String name;

    private String email;

    private String password;

    private Date date = new Date();
    
}
