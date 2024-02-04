package com.project.restfull.api.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchContactRequest implements Serializable {
    private String name;
    private String email;
    private String phone;
    @NotNull
    private Integer page;
    @NotNull
    private Integer size;
}
