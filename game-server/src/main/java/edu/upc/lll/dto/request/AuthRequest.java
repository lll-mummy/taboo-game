package edu.upc.lll.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class AuthRequest {
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 20, message = "用户名长度必须在3到20位之间")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 3, max = 20, message = "密码长度必须在6到20位之间")
    private String password;
}

