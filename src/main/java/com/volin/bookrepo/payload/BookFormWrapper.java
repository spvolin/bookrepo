package com.volin.bookrepo.payload;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.volin.bookrepo.model.Book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Deprecated
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookFormWrapper {
	
	private Book book;
    private List<MultipartFile> files;
}
