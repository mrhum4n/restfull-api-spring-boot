package com.project.restfull.api.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class WebResponse<T> implements Serializable {
    private T data;
    private String errors;
}
