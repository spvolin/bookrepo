package com.volin.bookrepo.payload;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.volin.bookrepo.model.Book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FormDataList {
	private Book book;
    private List<MultipartFile> files;

}
