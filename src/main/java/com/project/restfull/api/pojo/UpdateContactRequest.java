package com.project.restfull.api.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateContactRequest implements Serializable {
    @JsonIgnore
    @NotBlank
    private String id;

    @NotBlank
    private String firstName;

    private String lastName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String phone;
}
