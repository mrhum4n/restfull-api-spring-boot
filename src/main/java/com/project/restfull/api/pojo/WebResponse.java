package com.project.restfull.api.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebResponse<T> implements Serializable {
    private T data;
    private String errors;
    private PagingResponse paging;
}
