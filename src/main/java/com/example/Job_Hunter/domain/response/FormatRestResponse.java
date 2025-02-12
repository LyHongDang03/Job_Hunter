package com.example.Job_Hunter.domain.response;

import com.example.Job_Hunter.utill.anotation.ApiMessage;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FormatRestResponse implements ResponseBodyAdvice<Object> {


    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        HttpServletResponse httpResponse = ((ServletServerHttpResponse) response).getServletResponse();
        int status = httpResponse.getStatus();
        RestResponse<Object> restResponse = new RestResponse<>();
        restResponse.setStatusCode(status);
        if (body instanceof String || body instanceof Resource) {
            return body;
        }
        if (status >= 400) {
            return body;

        }
        else {
            restResponse.setData(body);
            ApiMessage message = returnType.getMethodAnnotation(ApiMessage.class);
            restResponse.setMessage(message != null ? message.value() : "Call api sucessfully");
        }
        return restResponse;
    }
}
