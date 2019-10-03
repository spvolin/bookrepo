package com.volin.bookrepo.payload;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
* Класс, который функционирует как сериализатор или инкапсулятор для возврата
* метаданных файла
*
* @Data Getters, Setters и другие элементы через Lombok
* @AllArgsConstructor конструктор через Lombok со всеми атрибутами класса в качестве аргументов.
*
*/

@Data
@AllArgsConstructor
public class UploadFileResponse {
    
	private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;
    
}
