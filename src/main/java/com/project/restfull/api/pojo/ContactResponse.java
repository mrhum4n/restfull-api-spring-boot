package com.project.restfull.api.pojo;

import lombok.Data;

@Data
public class ContactResponse {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}
