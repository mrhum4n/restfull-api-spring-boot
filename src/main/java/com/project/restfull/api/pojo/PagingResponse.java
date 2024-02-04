package com.project.restfull.api.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagingResponse {
    private Integer currentPage;
    private Integer totalPage;
    private Integer size;
}
