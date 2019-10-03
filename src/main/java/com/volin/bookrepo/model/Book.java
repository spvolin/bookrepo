package com.volin.bookrepo.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * The Class Book - класс представляет сущность "Book", экземпляры которой подлежат 
 * сохранению в таблице "APP_BOOK" БД.
 */
@Entity
@Table(name="APP_BOOK")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class Book implements Serializable{

    @ApiModelProperty(notes = "Идентификатор книги",name="id",required=true,value="")
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;

    //@ApiModelProperty(notes = "Кому принадлежит книга", name="username", required=true, value="")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ApiModelProperty(notes = "Автор книги",name="authorName",required=true,value="")
	@NonNull
	@Column(name="author", nullable=false)
	private String authorName;

    @ApiModelProperty(notes = "Название книги",name="bookTitle",required=true,value="")
	@NonNull
	@Column(name="title", nullable=false)
	private String bookTitle;

    @ApiModelProperty(notes = "ISBN книги",name="isbn",required=true,value="")
	@NonNull
	@Column(name="isbn", nullable=false)
	private String isbn;

//    Свойство нужно для каскадного удаления книги вместе с файлами. Требует реализации
//    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<DBFile> files;
    

	/**
	 * Equals.
	 *
	 * @param o the o
	 * @return true, if successful
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Book book = (Book) o;

		if (id != null ? !id.equals(book.id) : book.id != null) return false;
		if (authorName != null ? !authorName.equals(book.authorName) : book.authorName != null) return false;
		if (bookTitle != null ? !bookTitle.equals(book.bookTitle) : book.bookTitle != null) return false;
		return isbn != null ? isbn.equals(book.isbn) : book.isbn == null;
	}

	/**
	 * Hash code.
	 *
	 * @return the int
	 */
	@Override
	public int hashCode() {
	    return Objects.hash(id, authorName, bookTitle, isbn);
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "Book: [id=" + id + ", authorName=" + authorName + ", bookTitle=" + bookTitle
				+ ", isbn=" + isbn + "]";
	}


}
