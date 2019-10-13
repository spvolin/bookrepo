package com.volin.bookrepo.service;


import java.util.List;
import java.util.Optional;

import com.volin.bookrepo.model.Book;
import com.volin.bookrepo.payload.FormDataList;

/**
 * The Interface BookService включает методы работы с репозитарием сущности "Book".
 */
public interface BookService {
	
	Optional<Book> findById(Long id);
	//Optional<Book> findByIsbn(String isbn);
	Book findByIsbn(String isbn);
	Book saveBook(Book book);
	void updateBook(Book Book);
	void deleteBookById(Long id);
	void deleteAllBooks();
	boolean isBookExistWithThisIsbn(Book book);
	boolean isBookExistWithThisId(Book book);
	List<Book> findAllBooks();
	List<Book> findAllBooksByUser();
	List<Book> findBooksByTitle(String searchedTitle);
	List<Book> findBooksByAuthor(String searchedAuthor);
	List<Book> findBooksByIsbn(String searchedIsbn);
	Book saveBookForm(FormDataList formDataList);	
}