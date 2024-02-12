package com.project.restfull.api.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAddressRequest {
    @JsonIgnore
    @NotBlank
    private String contactId;
    @NotBlank
    private String street;
    @NotBlank
    private String city;
    @NotBlank
    private String province;
    @NotBlank
    private String country;
    private String postalCode;
}
