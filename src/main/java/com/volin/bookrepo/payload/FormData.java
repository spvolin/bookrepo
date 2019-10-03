package com.volin.bookrepo.payload;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FormData {
	private String model;
    private MultipartFile[] files;

}
