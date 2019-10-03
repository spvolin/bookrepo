package com.volin.bookrepo.payload;

import lombok.Data;

/**
* Класс, содержащий ответ с JWT-токеном для пользовательского входа
*
* @Data Getters, Setters и другие элементы через Lombok
*
* Ограничение:
* @NotBlank Убедитесь, что поле не пустое
*/
@Data
public class JwtAuthenticationResponse {
    private String accessToken;
    private String tokenType = "Bearer";

    public JwtAuthenticationResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
