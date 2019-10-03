package com.volin.bookrepo.model;

import com.volin.bookrepo.model.audit.DateAudit;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

/**
* Класс, определяющий модель пользователя, которая будет сохранена в базе данных
*
* @Entity Указывает, что класс будет отображен в базе данных.
* @Table Указывает таблицу, которая будет связана с сущностью
* @UniqueConstraint Указывает, что поле должно быть уникальным
* @Data  Getters, Setters и другие элементы через Lombok
* @NoArgsConstructor  пустой конструктор через Lombok
*
* Ограничения:
* @Id Указывает, что поле является первичным ключом
* @GeneratedValue Указывает, что поле будет сгенерировано самостоятельно
* @NotBlank Убедитесь, что поле не пустое
* @Size Проверка длины поля в заданном диапазоне.
* @Email Проверяет, что поле соответствует почтовому формату.
*/
@Entity
@Table(name="users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "username"
        }),
        @UniqueConstraint(columnNames = {
                "email"
        })
})
@Data
@NoArgsConstructor
public class User extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 40)
    private String name;

    @NotBlank
    @Size(max = 15)
    private String username;

    @NaturalId
    @NotBlank
    @Size(max = 40)
    @Email
    private String email;

    @NotBlank
    @Size(max = 100)
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public User(String name, String username, String email, String password) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
