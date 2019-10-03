package com.volin.bookrepo.payload;

import java.util.List;
import java.util.Optional;

import com.volin.bookrepo.model.Book;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
* Класс, который функционирует как сериализатор или инкапсулятор для возврата
* соответствующих данных книга и список файлов
*
* @Data Getters, Setters и другие элементы через Lombok
* @AllArgsConstructor конструктор через Lombok со всеми атрибутами класса в качестве аргументов.
*/


@Data
@AllArgsConstructor
public class BookFileResponse {
    
	private Optional<Book> book;
	private List<FileResponse> fileResponse;
}
