package com.example.Job_Hunter.domain.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePermissionReq {
    private Long id;
    private String name;
    private String apiPath;
    private String method;
    private String module;
}
