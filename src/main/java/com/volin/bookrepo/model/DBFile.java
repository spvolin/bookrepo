package com.volin.bookrepo.model;

import com.volin.bookrepo.model.audit.DateAudit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * Класс, определяющий модель файла для сохранения в базе данных   
 * @Entity Указывает, что класс будет отображен в базе данных. 
 * @Table Указывает таблицу, которая будет связана с сущностью 
 * @Data Summarize Getters, Setters и другие элементы через Lombok
 * @NoArgsConstructor - пустой конструктор через Lombok
 * @AllArgsConstructor - конструктор через Lombok со всеми атрибутами класса в качестве аргументов.  
 * Ограничения:   
 * 		@Id Указывает, что поле является первичным ключом  
 * 		@GeneratedValue Указывает, что поле будет сгенерировано самостоятельно  
 * 		@ManyToOne Указывает отношение многие-к-одному, в этом случае многие файлы
 * 			(DBFile) принадлежат пользователю (пользователю)
 * 		@JoinColumn Указывает поле отношения
 */
@Entity
@Table(name = "files")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DBFile extends DateAudit {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;

	private String fileName;
	private String fileType;
	private long fileSize;
	private String urlFile;

	@Lob
	@Type(type = "org.hibernate.type.MaterializedBlobType")
	private byte[] data;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "book_id", nullable = false)
	private Book book;

	/**
	 * Constructor
	 * 
	 * @param fileName имя файла
	 * @param fileType тип файла
	 * @param fileSize размер файла
	 * @param urlFile  URI для скачивания файла
	 */
	public DBFile(String fileName, String fileType, long fileSize, String urlFile) {
		this.fileName = fileName;
		this.fileType = fileType;
		this.fileSize = fileSize;
		this.urlFile = urlFile;
	}
}
