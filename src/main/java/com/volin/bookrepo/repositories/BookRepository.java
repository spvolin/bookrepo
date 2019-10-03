package com.volin.bookrepo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.volin.bookrepo.model.Book;
import com.volin.bookrepo.model.User;

/**
 * Интерфейс BookRepository.
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * Find by isbn.
     *
     * @param isbn the isbn
     * @return the book
     */
	@Query("select b from Book b where b.isbn = ?1")
    //Optional<Book> findByIsbn(String isbn);
	Book findByIsbn(String isbn);

	@Query("select b from Book b where b.id = ?1 and b.user.id=?2")
	List<Book> doesBookBelongToUser(Long book_id, Long user_id);
	
//	@Query("select b from Book b where b.user_id=?1")
//	List<Book> findAllBooksByUser(Long user_id);

	List<Book> findAllBooksByUser(User user);
	
	@Query("select b from Book b where b.bookTitle like %?1% and b.user.id=?2")
	List<Book> findBooksByTitle(@Param("searchedTitle") String searchedTitle, Long user_id);

	@Query("select b from Book b where b.authorName like %?1% and b.user.id=?2")
	List<Book> findBooksByAuthor(@Param("searchedAuthor") String searchedAuthor, Long user_id);

	@Query("select b from Book b where b.isbn like %?1% and b.user.id=?2")
	List<Book> findBooksByIsbn(@Param("searchedIsbn") String searchedIsbn, Long user_id);
}
