package com.volin.bookrepo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.util.MimeTypeUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.volin.bookrepo.model.Book;
import com.volin.bookrepo.model.DBFile;
import com.volin.bookrepo.payload.BookFileResponse;
import com.volin.bookrepo.payload.FileResponse;
import com.volin.bookrepo.payload.FormDataList;
import com.volin.bookrepo.service.BookService;
import com.volin.bookrepo.service.DBFileStorageService;
import com.volin.bookrepo.util.CustomErrorType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * The Class RestApiController - REST-контроллер для выполнения CRUD-операций 
 * с репозитарием книг и связанных с ними файлов
 */
@RestController
@Api()
@RequestMapping("/api/books")
public class BookController {

	/** The Constant logger. */
	public static final Logger logger = LoggerFactory.getLogger(BookController.class);

    @Autowired
    private DBFileStorageService fileStorageService;
	@Autowired
	BookService bookService; 

	// -------------------Create a Book-------------------------------------------

    /**
	 * Метод сохраняет новую книгу (без файлов).
	 *
	 * @param book - книга
	 * @param ucBuilder - URI-билдер
	 * @return the response entity
	 */
	@ApiOperation(value = "Сохранить новую книгу ", response = Book.class, tags = "createBook")
    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = "application/json;charset=UTF-8")
    //return 201 instead of 200
    @ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> createBook(@RequestBody Book book, UriComponentsBuilder ucBuilder) {
		logger.info("Creating Book : {}", book);

		if (bookService.isBookExistWithThisIsbn(book)) {
			logger.error("Unable to create. A Book with ISBN {} already exist", book.getIsbn());
			return new ResponseEntity<CustomErrorType>(new CustomErrorType("Unable to create. A Book with name " + 
			book.getIsbn() + " already exist."),HttpStatus.CONFLICT);
		}
		
		bookService.saveBook(book);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/books/{id}").buildAndExpand(book.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}
	
	/**
	 * Метод обрабатывает HTTP-запрос метода POST 
	 * @param request - запрос, содержащий модель данных класса Book, 
	 * и возможно MultipartFile - файлы (>=0)
	 * @param response
	 * @param ucBuilder
	 * @return
	 * @throws IOException
	 * 
	 * Ref: https://stackoverflow.com/questions/33920248/angularjs-formdata-file-array-upload/33921749
	 */
	@RequestMapping(value = "/files", method = RequestMethod.POST)
	public ResponseEntity<?> createBookAndFiles(MultipartHttpServletRequest request, HttpServletResponse response, UriComponentsBuilder ucBuilder) throws IOException {
		
		// Parse JSON to Book
	    final String bookModel = request.getParameter("model");
	    Book book = getBookFromJson(bookModel);

		logger.info("Creating Book: ", ""+book);

		// Check if book exists:
		if (bookService.isBookExistWithThisIsbn(book)) {
			logger.error("Unable to create. A Book with ISBN {} already exist", book.getIsbn());
			return new ResponseEntity<CustomErrorType>(new CustomErrorType("Unable to create. A Book with name " + 
			book.getIsbn() + " already exist."),HttpStatus.CONFLICT);
		}

		// Get MultipartFile files
	    Iterator<String> iterator = request.getFileNames();
	    MultipartFile multipartFile = null;
	    ArrayList<MultipartFile> mpfiles = new ArrayList<MultipartFile>();
	    
	    while (iterator.hasNext()) {
	        multipartFile = request.getFile(iterator.next());
	        //do something with the file.....
	        System.out.println("File: " + multipartFile);
	        System.out.println("Filename: " + multipartFile.getName());
	        mpfiles.add(multipartFile);
	    }

	    // Сохраняем данные в форме
		FormDataList fdl = new FormDataList();
		fdl.setBook(book);
		fdl.setFiles(mpfiles);
		
		// вызываем метод сервиса на сохранение книги
		Book returnedBook = bookService.saveBookForm(fdl);

	    HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/books/{id}").buildAndExpand(returnedBook).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);	    
	}

	// ------------------- Delete a Book (no files) -----------------------------------------

    /**
	 * Метод удаляет книгу. Пока без файлов.
	 *
	 * @param id the id
	 * @return the response entity
	 */
	@ApiOperation(value = "Удалить книгу ", response = Book.class, tags = "deleteBook")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteBook(@PathVariable("id") long id) {
		logger.info("Fetching & Deleting Book with id {}", id);

		Optional<Book> book = bookService.findById(id);
		if (book == null) {
			logger.error("Unable to delete. Book with id {} not found.", id);
			return new ResponseEntity<CustomErrorType>(new CustomErrorType("Unable to delete. Book with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		bookService.deleteBookById(id);
		book = bookService.findById(id);
		System.out.println(""+book);
		return new ResponseEntity<Book>(HttpStatus.NO_CONTENT);
	}

	// ------------------- Delete a File of a Book -----------------------------------------
    /**
	 * Метод удаляет файл.
	 *
	 * @param id - UUID файла
	 * @return the response entity
	 */
	@ApiOperation(value = "Удалить файл книги ", response = DBFile.class, tags = "deleteFile")
	@RequestMapping(value = "/{book_id}/files/{file_id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteFile(
			@PathVariable("book_id") Long book_id, 
			@PathVariable("file_id") String file_id, 
			UriComponentsBuilder ucBuilder) {
		
		logger.info("Fetching & Deleting File with file_id {} of Book {}", file_id, book_id);
		
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        System.out.println("currentPrincipalName = "+ currentPrincipalName);

		// Part 1
		Optional<DBFile> f = fileStorageService.getFileById(file_id);
		if (f == null) {
			logger.error("Unable to delete. File with file_id {} not found.", file_id);
			return new ResponseEntity<CustomErrorType>(new CustomErrorType(
					"Unable to delete. File with file_id " + file_id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		
		fileStorageService.deleteFileById(file_id);
		//return getBookAndFiles(book_id);
//		HttpHeaders headers = new HttpHeaders();
//	    URI uri = ucBuilder.path("/api/books/{id}/files/").buildAndExpand(book_id).toUri();
//	    System.out.println("uri = "+ uri);
//		headers.setLocation(uri);
//		return new ResponseEntity<String>(headers, HttpStatus.FOUND);
		           
		// Part 2
        List<DBFile> dbFiles = fileStorageService.getAllByBook(book_id, currentPrincipalName);
        int size = dbFiles == null ? 0 : dbFiles.size(); 
        System.out.println("dbFiles.# = "+ size);
        List<FileResponse> files = dbFiles.stream().map(file -> 
        	new FileResponse(file.getId(), file.getFileName(), ServletUriComponentsBuilder.fromCurrentContextPath()
        			.path("/api/files/downloadFile/id/").path(file.getId()).toUriString(),
        			file.getFileType(), file.getFileSize())).collect(Collectors.toList());
        
        Optional<Book> book = bookService.findById(book_id);
        BookFileResponse resp = new BookFileResponse(book, files);
        return new ResponseEntity<BookFileResponse>(resp, HttpStatus.OK);		
	}
	
	// ------------------- Delete All Books (no files) -----------------------------

    /**
	 * Метод удаляет все книги. Пока без файлов.
	 *
	 * @return the response entity
	 */
	@ApiOperation(value = "Удалить все книги ", response = Book.class, tags = "deleteAllBooks")
	@RequestMapping(value = "/", method = RequestMethod.DELETE)
	public ResponseEntity<Book> deleteAllBooks() {
		logger.info("Deleting All Books");

		bookService.deleteAllBooks();
		return new ResponseEntity<Book>(HttpStatus.NO_CONTENT);
	}
	
	/**
	 * Получить конкретную книгу по ее id.
	 *
	 * @param id 
	 * @return экземпляр книги
	 */
	// -------------------Retrieve Single Book------------------------------------------
    @ApiOperation(value = "Получить конкретную книгу ", response = Book.class, tags = "getBook")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getBook(@PathVariable("id") long id) {

    	logger.info("Fetching Book with id {}", id);
		Optional<Book> book = bookService.findById(id);
		if (book == null) {
			logger.error("Book with id {} not found.", id);
			return new ResponseEntity<CustomErrorType>(new CustomErrorType("Book with id " + id 
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Optional<Book>>(book, HttpStatus.OK);
	}


	/**
	 * Получить книгу с заданным isbn.
	 *
	 * @param isbn 
	 * @return экземпляр книги
	 */
	// -------------------Retrieve Single Book------------------------------------------
    @ApiOperation(value = "Получить книгу с заданным isbn", response = Book.class, tags = "getBookByIsbn")
    @GetMapping("/search/")
	public ResponseEntity<?> getBookByIsbn(@RequestParam("isbn") String isbn) {

    	logger.info("Fetching Book with isbn {}", isbn);
		Book book = bookService.findByIsbn(isbn);
		
		if (book == null) {
			logger.error("Book with isbn {} not found.", isbn);
			return new ResponseEntity<CustomErrorType>(new CustomErrorType("Book with isbn " + isbn 
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(book, HttpStatus.OK);
	}

    @GetMapping(value = "/{id}/files")
    public ResponseEntity<?> getBookAndFiles(@PathVariable("id") long book_id){
        
		logger.info("Fetching Book with id {}", book_id);

    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        System.out.println("currentPrincipalName = "+ currentPrincipalName);

            
        List<DBFile> dbFiles = fileStorageService.getAllByBook(book_id, currentPrincipalName);
        
        List<FileResponse> files = dbFiles.stream().map(file -> 
        	new FileResponse(file.getId(), file.getFileName(), ServletUriComponentsBuilder.fromCurrentContextPath()
        			.path("/api/files/downloadFile/id/").path(file.getId()).toUriString(),
        			file.getFileType(), file.getFileSize())).collect(Collectors.toList());
        
        Optional<Book> book = bookService.findById(book_id);
        BookFileResponse resp = new BookFileResponse(book, files);
        return new ResponseEntity<BookFileResponse>(resp, HttpStatus.OK);
    }
    
    /**
	 * Метод парсит JSON-объект модели данных книги запроса
	 * @param bookModel - объект модели данных книги
	 * @return book - экземпляр класса Book
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	private Book getBookFromJson (String bookModel) throws JsonParseException, JsonMappingException, IOException {

		// Parse JSON to Book
	    final ObjectMapper mapper = new ObjectMapper();
	    Book book = mapper.readValue(bookModel, Book.class);
		return book;
	}
	
	// -------------------Retrieve All Books---------------------------------------------

	/**
	 * Получить список книг.
	 *
	 * @return ResponseEntity со списком книг
	 */
	@ApiOperation(value = "Получить список книг", response = Iterable.class, tags = "listAllBooks")
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ResponseEntity<List<Book>> listAllBooks() {

		List<Book> books = bookService.findAllBooksByUser();
		if (books.isEmpty()) {
			return new ResponseEntity<List<Book>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<Book>>(books, HttpStatus.OK);
	}

	// ------------------- Get Books By Title---------------------------------------------

	@RequestMapping(value = "/title/{searchedTitle}", method = RequestMethod.GET)
	public ResponseEntity<List<Book>> listBooksByTitle(@PathVariable("searchedTitle") String searchedTitle) {
		
		List<Book> books = bookService.findBooksByTitle(searchedTitle);
		System.out.println("listBooksByTitle #: " + books.size());
		
		if (books.isEmpty()) {
			return new ResponseEntity<List<Book>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<Book>>(books, HttpStatus.OK);
	}

	// ------------------- Get Books By Author---------------------------------------------

	@RequestMapping(value = "/author/{searchedAuthor}", method = RequestMethod.GET)
	public ResponseEntity<List<Book>> listBooksByAuthor(@PathVariable("searchedAuthor") String searchedAuthor) {
		
		List<Book> books = bookService.findBooksByAuthor(searchedAuthor);
		System.out.println("listBooksByAuthor #: " + books.size());
		
		if (books.isEmpty()) {
			return new ResponseEntity<List<Book>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<Book>>(books, HttpStatus.OK);
	}
	
	// ------------------- Get Books By Isbn---------------------------------------------

	@RequestMapping(value = "/isbn/{searchedIsbn}", method = RequestMethod.GET)
	public ResponseEntity<List<Book>> listBooksByIsbn(@PathVariable("searchedIsbn") String searchedIsbn) {
		
		List<Book> books = bookService.findBooksByIsbn(searchedIsbn);
		System.out.println("listBooksByIsbn #: " + books.size());
		
		if (books.isEmpty()) {
			return new ResponseEntity<List<Book>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<Book>>(books, HttpStatus.OK);
	}
			
	/**
	 * Метод сохраняет изменения в метаинформации книги.
	 * (Пока не учитывает файлы. Пока. Предполагается к расширению)
	 *
	 * @param id - id книги
	 * @param book - экземпляр класса Book
	 * @return the response entity
	 */
	// ------------------- Update a Book ------------------------------------------------
    @ApiOperation(value = "Обновить книгу ", response = Book.class, tags = "updateBook")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MimeTypeUtils.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateBook(@PathVariable("id") long id, @RequestBody Book book) {
		logger.info("Updating Book with id {}", id);

		Optional<Book> currentBook = bookService.findById(id);

		if (currentBook == null) {
			logger.error("Unable to update. Book with id {} not found.", id);
			return new ResponseEntity<CustomErrorType>(new CustomErrorType("Unable to upate. Book with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}

		currentBook.get().setIsbn(book.getIsbn());
		currentBook.get().setAuthorName(book.getAuthorName());
		currentBook.get().setBookTitle(book.getBookTitle());

		// сохраняем книгу
		bookService.updateBook(currentBook.get());
		return new ResponseEntity<Optional<Book>>(currentBook, HttpStatus.OK);
	}

	/**
	 * Метод обрабатывает HTTP-запрос метода PUT 
	 * @param request - запрос, содержащий модель данных класса Book, 
	 * и возможно MultipartFile - файлы (>=0)
	 * @param response
	 * @param ucBuilder
	 * @return
	 * @throws IOException
	 * 
	 * Ref: https://stackoverflow.com/questions/33920248/angularjs-formdata-file-array-upload/33921749
	 */
	@RequestMapping(value = "/{id}/files", method = RequestMethod.PUT)
	public ResponseEntity<?> updateBookAndFiles(@PathVariable("id") long id, MultipartHttpServletRequest request, HttpServletResponse response, UriComponentsBuilder ucBuilder) throws IOException {
		
		logger.info("Updating a Book with id {}", id);

		// Parse JSON to Book
	    final String bookModel = request.getParameter("model");
	    Book book = getBookFromJson(bookModel);

		logger.info("Updating a Book: ", ""+book);

		// Check if book exists:
		if (!bookService.isBookExistWithThisId(book)) {
			logger.error("Unable to update. Book with id {} not found.", id);
			return new ResponseEntity<CustomErrorType>(new CustomErrorType("Unable to upate. Book with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		
		// Get MultipartFile files
	    Iterator<String> iterator = request.getFileNames();
	    MultipartFile multipartFile = null;
	    ArrayList<MultipartFile> mpfiles = new ArrayList<MultipartFile>();
	    
	    while (iterator.hasNext()) {
	        multipartFile = request.getFile(iterator.next());
	        //do something with the file.....
	        System.out.println("File: " + multipartFile);
	        System.out.println("Filename: " + multipartFile.getName());
	        mpfiles.add(multipartFile);
	    }

	    // Сохраняем данные в форме
		FormDataList fdl = new FormDataList();
		fdl.setBook(book);
		fdl.setFiles(mpfiles);
		
		// вызываем метод сервиса на сохранение книги
		Book returnedBook = bookService.saveBookForm(fdl);

	    HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/books/{id}").buildAndExpand(returnedBook).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.OK);	    
	}

}