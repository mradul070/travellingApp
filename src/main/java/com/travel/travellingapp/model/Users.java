package com.travel.travellingapp.model;

import javax.validation.constraints.NotBlank;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import lombok.Data;
@Entity
@Data
public class Users {
    @Id
    private UUID id;

    @Column(name = "first_name" ,nullable = false)
    @NotBlank()
    private String firstName;

    @Column(name = "last_name" ,nullable = false)
    @NotBlank()
    private String lastName;

    @Column(name = "email" ,nullable = false)
    @NotBlank()
    @Email()
    private String email;

    @Column(name = "is_active", columnDefinition = "boolean default 'false'")
    private boolean isActive;


    public void setId(String id) {
        UUID uuid = UUID.fromString(id);
        this.id = uuid;
    }
    // @Column(name = "gender" ,nullable = false)
    // @Enumerated(EnumType.STRING)
    // @NotBlank()
    // private Gender gender;
}
