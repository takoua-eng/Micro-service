package com.esprit.microservice.cours.dto;

/**
 * Simple DTO representing a User exposed by user-ms.
 * Not persisted in cours-ms.
 */
public class UserDTO {

    private String id;
    private String name;
    private String email;

    public UserDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

