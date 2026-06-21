package com.example.second;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "entities")
public class Entity {

    @Id
    private Integer id;
    private String name;
    private String designation;

    public Entity() {
    }

    public Entity(Integer id, String name, String designation) {
        this.id = id;
        this.name = name;
        this.designation = designation;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }
}
