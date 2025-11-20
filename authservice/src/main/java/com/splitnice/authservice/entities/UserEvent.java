package com.splitnice.authservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

@NoArgsConstructor
@AllArgsConstructor

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
    public class UserEvent {
        private String userId;
        private String email;
        private String firstName;
        private String lastName;
    }

