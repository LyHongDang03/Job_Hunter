package com.example.Job_Hunter.domain.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqLoginDTO {
    @NotBlank(message = "USN Not blank")
    private String username;
    @NotBlank(message = "PW Not blank")
    private String password;

}
