package com.volin.bookrepo;

//import com.fasterxml.jackson.core.JsonProcessingException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.volin.bookrepo.controller.BookController;
import com.volin.bookrepo.controller.BookExceptionHandler;
import com.volin.bookrepo.model.Book;
import com.volin.bookrepo.repositories.BookRepository;
import com.volin.bookrepo.service.BookService;

/**
 * @class BookControllerMockMvcStandalongTest тестирует некоторые методы BookController
 * Используется MockMvc standalone approach
 * 
 * Ref: https://thepracticaldeveloper.com/2017/07/31/guide-spring-boot-controller-tests/#Strategy_1_MockMVC_in_Standalone_Mode
 */
@RunWith(MockitoJUnitRunner.class)
public class BookControllerMockMvcStandalongTest {

	private MockMvc mvc;

	@Mock
	private BookRepository bookRepository;

	@Mock
	private BookService mockService;

	@InjectMocks
	private BookController bookController;

	// This object will be magically initialized by the initFields method below.
	private JacksonTester<Book> jsonBook;

	
	/**
	 * Этот метод инициализирует: 
	 * - JacksonTester
	 * - mvc
	 * Выполняется перед всеми тестами
	 */
	@Before
	public void setup() {
		
		// We would need this line if we would not use MockitoJUnitRunner
		// MockitoAnnotations.initMocks(this);
		
		// Initializes the JacksonTester
		JacksonTester.initFields(this, new ObjectMapper());
		
		// MockMvc standalone approach
		mvc = MockMvcBuilders.standaloneSetup(bookController)
				.setControllerAdvice(new BookExceptionHandler())
				// .addFilters(new BookFilter())
				.build();
	}

	
	/**
	 * Метод тестирует выполнение приложением запроса:  "/api/book/1"
	 * 
	 * @throws Exception
	 */
	@Test
	public void canRetrieveByIdWhenExists() throws Exception {
		
    	Book book = new Book("Шекспир Уильям", "Гамлет", "978-5-9268-2813-6");
    	book.setId(1L);
		// given
		given(mockService.findById(1L)).willReturn(Optional.of(book));

		// when
		MockHttpServletResponse response = mvc
			.perform(get("/api/book/1")
			.accept(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andReturn()
			.getResponse();

		// then
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		assertThat(response.getContentAsString())
				.isEqualTo(jsonBook.write(book).getJson());
	}

	/**
	 * Метод тестирует выполнение приложением запроса:  "/api/book/search/?isbn=ISBN-A"
	 * В интерфейсе метод не реализован но запрос к серверу выполнить можно
	 * 
	 * @throws Exception
	 */
	@Test
	public void canRetrieveByIsbnWhenExists() throws Exception {

    	Book book = new Book("Author A", "Title A", "ISBN-A");
    	book.setId(1L);
		// given
		given(mockService.findByIsbn("ISBN-A"))
			.willReturn(book);

		// when
		MockHttpServletResponse response = mvc
			.perform(get("/api/book/search/?isbn=ISBN-A")
			.accept(MediaType.APPLICATION_JSON_UTF8))
			.andDo(print())
			.andReturn()
			.getResponse();

		// then
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		assertThat(response.getContentAsString())
				.isEqualTo(jsonBook.write(book).getJson());
	}

	/**
	 * Метод тестирует выполнение приложением POST- запроса:  "/api/book/" на создание новой книги
	 * 
	 * @throws Exception
	 */
	@Test
	public void canCreateANewBook() throws Exception {
		// when
		MockHttpServletResponse response = mvc
			.perform(post("/api/book/")
			.contentType(MediaType.APPLICATION_JSON_UTF8)
			.content(jsonBook.write(new Book("Шекспир Уильям", "Гамлет", "978-5-9268-2813-6")).getJson()))
			.andDo(print())
			.andReturn()
			.getResponse();

		// then
		assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
	}
}
