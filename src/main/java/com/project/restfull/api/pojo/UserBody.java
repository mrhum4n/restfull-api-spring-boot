package com.project.restfull.api.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBody implements Serializable {
    @NotBlank
    private String username;

    @NotBlank
    private String name;

    @NotBlank
    private String password;
}
