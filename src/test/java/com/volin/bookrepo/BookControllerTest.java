package com.volin.bookrepo;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.json.JacksonTester;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.mock.web.MockHttpServletResponse;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
//import static org.mockito.ArgumentMatchers;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.given;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.volin.bookrepo.service.BookService;
import com.volin.bookrepo.controller.BookController;
import com.volin.bookrepo.controller.BookExceptionHandler;
import com.volin.bookrepo.model.Book;

@RunWith(MockitoJUnitRunner.class)
public class BookControllerTest {

    private static final ObjectMapper om = new ObjectMapper();

    private MockMvc mockMvc;

	@Mock
	private BookService mockService;
	private Book book;

	@InjectMocks
	private BookController bookController;
	
	// This object will be magically initialized by the initFields method below.
	private JacksonTester<Book> jsonBook;

	@Before
    public void init() throws Exception {
		
		// Initializes the JacksonTester
		JacksonTester.initFields(this, new ObjectMapper());
		
		// MockMvc standalone approach
		mockMvc = MockMvcBuilders.standaloneSetup(bookController)
				.setControllerAdvice(new BookExceptionHandler())
				// .addFilters(new BookFilter())
				.build();
		
		book = new Book("Шекспир Уильям", "Гамлет", "978-5-9268-2813-6");
    	book.setId(1L);
    	
    	Mockito.when(mockService.findByIsbn("978-5-9268-2813-6")).thenReturn(book);
    	Mockito.when(mockService.findById(1L)).thenReturn(Optional.of(book));
   	
    }
    
    @Test
    public void find_bookId_OK() throws Exception {

    	mockMvc.perform(get("/api/book/1/"))
    		.andDo(print())
    		.andExpect(status()
    		.isOk());

    }
	
    @Test
    public void find_bookByIsbn_OK() throws Exception {

    	// given
    	book = new Book("Шекспир Уильям", "Гамлет", "978-5-9268-2813-6");
    	book.setId(1L);
		given(mockService.findByIsbn("978-5-9268-2813-6")).willReturn(book);

		// when
		MockHttpServletResponse response = 
				mockMvc
				.perform(get("/api/book/search/?isbn=978-5-9268-2813-6")
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andReturn().getResponse();

		// then
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		assertThat(response.getContentAsString())
				.isEqualTo(jsonBook.write(book).getJson());

    }

    // Далее идут 3 теста, которые надо отлаживать
    
//    @Test
//    public void find_allBook_OK() throws Exception {
//
//    	book = new Book("Author A", "Book A", "ISBN-A");
//    	book.setId(2L);
//    	Book book1 = new Book("Author B", "Book B", "ISBN-B");
//    	book1.setId(3L);
//    	
//        List<Book> books = Arrays.asList(book, book1);
//        
//        when(mockService.findAllBooks()).thenReturn(books);
//
//        mockMvc.perform(get("/api/book/"))
//        		.andDo(print())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(2)))
//                .andExpect(jsonPath("$[0].id", is(2)))
//                .andExpect(jsonPath("$[0].authorName", is("Author A")))
//                .andExpect(jsonPath("$[0].bookTitle", is("Book A")))
//                .andExpect(jsonPath("$[0].isbn", is("ISBN-A")))
//                .andExpect(jsonPath("$[1].id", is(3)))
//                .andExpect(jsonPath("$[1].authorName", is("Author B")))
//                .andExpect(jsonPath("$[1].bookTitle", is("Book B")))
//                .andExpect(jsonPath("$[1].isbn", is("ISBN-B")));
//
//        verify(mockService, times(1)).findAllBooks();
//
//    }
    
//    Test failed! Check it up!
//    @Test
//    public void find_bookIdNotFound_404() throws Exception {
//        mockMvc.perform(get("/api/book/5/")).andExpect(status().isNotFound());
//    }
    
//    @Test
//    public void save_book_OK() throws Exception {
//
//        Book newBook = new Book("Author C", "Book C", "ISBN-C");
//
//        Mockito.doNothing()
//			.when(mockService)
//			.saveBook(Mockito.any(Book.class));
//
//        mockMvc.perform(post("/api/book/")
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .content(om.writeValueAsString(newBook)))
//                .andDo(print())
//                .andExpect(status().isCreated());
//	
//        verify(mockService, times(1)).saveBook(any(Book.class));
//
//    }
//    
    @Test
    public void update_book_OK() throws Exception {

        Book updateBook = new Book("Author E", "Book E", "ISBN-E");
        
        Mockito.doNothing().when(mockService).updateBook(any(Book.class));
        updateBook.setId(1L);
        mockMvc.perform(put("/api/book/{id}/", 1L)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(om.writeValueAsString(updateBook)))
                .andDo(print())                
                .andExpect(status().isOk());

    }
    
    @Test
    public void delete_book_OK() throws Exception {

    	Mockito.doNothing().when(mockService).deleteBookById(1L);

        mockMvc.perform(delete("/api/book/1"))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(mockService, times(1)).deleteBookById(1L);
    }
}
