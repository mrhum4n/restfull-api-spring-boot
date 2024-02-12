package com.project.restfull.api.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAddressRequest {
    @JsonIgnore
    @NotBlank
    private String contactId;
    @JsonIgnore
    @NotBlank
    private String addressId;
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
