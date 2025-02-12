package com.example.Job_Hunter.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestResponse<T> {
    private int statusCode;
    private String error;
    private Object message;
    private T data;
}
