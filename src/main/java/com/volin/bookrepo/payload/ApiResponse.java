package com.volin.bookrepo.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
* Класс, который работает как сериализатор json и возвращает простые ответы
* API запросы
*
* @Data Getters, Setters и другие элементы через Lombok
* @AllArgsConstructor конструктор через Lombok со всеми атрибутами класса в качестве аргументов.
*
*/
@Data
@AllArgsConstructor
public class ApiResponse {
    private Boolean success;
    private String message;
}
