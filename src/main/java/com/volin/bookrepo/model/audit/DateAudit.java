package com.volin.bookrepo.model.audit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.Instant;

/**
* Класс, который проверяет даты моделей
*
* @EntityListeners указывает на слушателя, который назначит значения
* @JsonIgnoreProperties Указывает значения, которые будут игнорироваться в ответах JSON.
* @Data Getters, Setters и другие элементы через Lombok
*
* Ограничения:
* @CreatedDate Указывает, что поле содержит дату создания записи
* @LastModifiedDate Указывает, что поле содержит дату изменения записи.
* @Column Указывает прямые свойства поля
*/
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(
        value = {"createdAt", "updatedAt"},
        allowGetters = true
)
@Data
public abstract class DateAudit {
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedAt;
}
