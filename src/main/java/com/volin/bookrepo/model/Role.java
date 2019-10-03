package com.volin.bookrepo.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

/**
 * Класс, который определяет образец для подражания для сохранения в базе данных
 *   *   * @Entity Указывает, что класс будет отображен в базе данных.  
 * * @Table Указывает таблицу, которая будет связана с сущностью   * @Data
 * Summarize Getter, Setters и другие элементы через Lombok  
 * * @NoArgsConstructor пустой конструктор через Lombok
 *
 * Constraints:
 * 
 * @Id Указывает, что поле является первичным ключом
 * @GeneratedValue Указывает, что поле будет сгенерировано самостоятельно
 * @Enumerated отображает значения перечисления в поле 
 * @Column Указывает прямые свойства поля таблицы
 */
@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@NaturalId
	@Column(length = 60)
	private RoleName name;

	/**
	 * Constructor 
	 * 
	 * @param ключ имени перечисления
	 */
	public Role(RoleName name) {
		this.name = name;
	}

}
