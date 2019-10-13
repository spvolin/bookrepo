package com.volin.bookrepo.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
* Класс, который функционирует как сериализатор или инкапсулятор для возврата
* соответствующие метаданных файла
*
* @Data Getters, Setters и другие элементы через Lombok
* @AllArgsConstructor конструктор через Lombok со всеми атрибутами класса в качестве аргументов.
*/


@Data
@AllArgsConstructor
public class FileResponse {
    
	private String id;
	private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;
    
}
