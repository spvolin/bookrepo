package com.volin.bookrepo.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.volin.bookrepo.model.Book;
import com.volin.bookrepo.model.DBFile;
import com.volin.bookrepo.model.User;
import com.volin.bookrepo.payload.FormDataList;
import com.volin.bookrepo.repositories.BookRepository;
import com.volin.bookrepo.repositories.DBFileRepository;
import com.volin.bookrepo.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * The Class BookServiceImpl - реализует интерфейс BookService.
 */
@Service("bookService")
//@Transactional
public class BookServiceImpl implements BookService {

	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private DBFileRepository fileRepository;

	/**
	 * Find by id.
	 *
	 * @param id the id
	 * @return the book
	 */
	public Optional<Book> findById(Long id) {
		return bookRepository.findById(id);
	}

	/**
	 * Find by isbn.
	 *
	 * @param isbn the isbn
	 * @return the book
	 */
	// public Optional<Book> findByIsbn(String isbn) {
	public Book findByIsbn(String isbn) {
		return bookRepository.findByIsbn(isbn);
	}

	/**
	 * Save book.
	 *
	 * @param book the book
	 */
	@Transactional(readOnly = false)
	public Book saveBook(Book book) {
		User user = getCurrentUser();
		book.setUser(user);
		return bookRepository.save(book);
	}

	/**
	 * Update book.
	 *
	 * @param book the book
	 */
	@Transactional(readOnly = false)
	public void updateBook(Book book) {
		// User user = getCurrentUser();
		saveBook(book);
	}

	/**
	 * Delete book by id.
	 *
	 * @param id the id
	 */
	@Transactional(readOnly = false)
	public void deleteBookById(Long bookId) {
		fileRepository.deleteByBookId(bookId);
		bookRepository.deleteById(bookId);
	}

	/**
	 * Delete all books.
	 */
	@Transactional(readOnly = false)
	public void deleteAllBooks() {
		bookRepository.deleteAll();
	}

	/**
	 * Find all books.
	 *
	 * @return the list
	 */
	public List<Book> findAllBooks() {
		return bookRepository.findAll();
	}

	public List<Book> findBooksByTitle(String searchedTitle) {
		User user = getCurrentUser();
		return bookRepository.findBooksByTitle(searchedTitle, user.getId());
	}

	public List<Book> findBooksByAuthor(String searchedAuthor) {
		User user = getCurrentUser();
		return bookRepository.findBooksByAuthor(searchedAuthor, user.getId());
	}

	public List<Book> findBooksByIsbn(String searchedIsbn) {
		User user = getCurrentUser();
		return bookRepository.findBooksByIsbn(searchedIsbn, user.getId());
	}

	/**
	 * Find all books that belong to user.
	 *
	 * @return the list
	 */
	public List<Book> findAllBooksByUser(String name) {
		User user = getCurrentUser();
		return bookRepository.findAllBooksByUser(user);
	}

	/**
	 * Checks if is book exist.
	 *
	 * @param book the book
	 * @return true, if is book exist
	 */
	public boolean isBookExistWithThisIsbn(Book book) {
		return findByIsbn(book.getIsbn()) != null;
	}

	public boolean isBookExistWithThisId(Book book) {
		return findById(book.getId()) != null;
	}


	@Override
	public List<Book> findAllBooksByUser() {

		User user = getCurrentUser();
		return bookRepository.findAllBooksByUser(user);
	}

	
	/**
	 * Метод getCurrentUser:
	 * - извлекает имя пользователя из контекста безопасности
	 * - получает из userRepository по имени экземпляр класса User
	 * @return возвращает найденный экземпляр класса User  
	 */
	public User getCurrentUser() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();

		User user = userRepository.findByUsernameOrEmail(currentPrincipalName, currentPrincipalName).orElseThrow(
				() -> new UsernameNotFoundException("User not found with username or email : " + currentPrincipalName));

		return user;
	}

	
	/**
	 * Метод saveBookForm: сохраняет в БД данные из формы FormDataList
	 * Входной параметр: форма FormDataList, содержащая книгу и файлы полученные из запроса метода POST
	 * 
	 * @return  returnedBook - экземпляр сохраненной в БД книги 
	 */
	public Book saveBookForm(FormDataList fdl) {

		Book book = fdl.getBook();
		User user = getCurrentUser();
		book.setUser(user);
		// сохраняем экземпляр книги в БД 
		Book returnedBook = bookRepository.saveAndFlush(book);
		try {
			// вызываем метод который сохраняет файлы книги, если они есть: 
			saveUploadedFiles(returnedBook, fdl.getFiles());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return returnedBook;
	}
	
	/**
	 * Метод saveUploadedFiles: сохраняет в БД файлы, полученные из FormDataList
	 * 
	 * Входные параметры: 
	 * - Book book
	 * - List<MultipartFile> files
	 */
	private void saveUploadedFiles(Book book, List<MultipartFile> files) throws IOException {
		
		if (files != null && files.size() > 0) {
			
			System.out.println("Files#:" + files.size());

			for (int i = 0; i < files.size(); i++) {

				MultipartFile file = files.get(i);
				DBFile dbFile = null;
				if (!file.isEmpty()) {
			        System.out.println("Filename: " + file.getName());
					dbFile = new DBFile();
					dbFile.setFileName(file.getOriginalFilename());
					dbFile.setFileSize(file.getSize());
					dbFile.setFileType(file.getContentType());
					dbFile.setData(file.getBytes());
					dbFile.setBook(book);

					fileRepository.save(dbFile);
				}
			}
		}
	}
}
