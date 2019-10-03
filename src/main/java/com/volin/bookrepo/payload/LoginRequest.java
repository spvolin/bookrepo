package com.volin.bookrepo.payload;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
* Класс, содержащий поля, которые должны быть получены в запросе на вход.
* 
* @Data Getters, Setters и другие элементы через Lombok
*
* Ограничение:
* @NotBlank Убедитесь, что поле не пустое
 */
@Data
public class LoginRequest {
    @NotBlank
    private String usernameOrEmail;

    @NotBlank
    private String password;
}
