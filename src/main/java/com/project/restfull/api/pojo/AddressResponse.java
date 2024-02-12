package com.project.restfull.api.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressResponse {
    private String id;
    private String street;
    private String city;
    private String province;
    private String country;
    private String postalCode;
}
