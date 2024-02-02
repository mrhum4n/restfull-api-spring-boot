package com.project.restfull.api.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class TokenResponse implements Serializable {
    private String token;
    private Long expiredAt;
}
