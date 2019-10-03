package com.volin.bookrepo.payload;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
* Класс, содержащий данные, которые должны быть получены в
* запрос на регистрацию пользователя
*
* @Data Getters, Setters и другие элементы через Lombok
* @AllArgsConstructor конструктор через Lombok со всеми атрибутами класса в качестве аргументов.
*
* Ограничения:
* @NotBlank Убедитесь, что поле не пустое
* @Size Проверка длины поля в заданном диапазоне.
* @Email Проверяет, что поле соответствует почтовому формату.
* 
*/
@Data
public class SignUpRequest {
    @NotBlank
    @Size(min = 4, max = 40)
    private String name;

    @NotBlank
    @Size(min = 3, max = 15)
    private String username;

    @NotBlank
    @Size(max = 40)
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 20)
    private String password;
}
