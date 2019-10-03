package com.volin.bookrepo.repositories;

import com.volin.bookrepo.model.DBFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
* Интерфейс, который взаимодействует с базой данных и с моделью DBFile через JPA
*
* @Repository указывает, что это репозиторий для Spring приложения 
*/
@Repository
public interface DBFileRepository extends JpaRepository<DBFile, String> {
    //List<DBFile> findByUser(User user);
    Optional<?> removeAllByFileName(String fileName);
    Optional<DBFile> findByFileName(String fileName);

    @Query("select f from DBFile f where f.id=?1")
    Optional<DBFile> findByUuid(String id);

    @Query("select f from DBFile f where f.book.id=?1")
    List<DBFile> findByBookId(Long bookId);
    
    @Modifying
    @Query("DELETE DBFile f WHERE f.book.id = ?1")
    void deleteByBookId(Long bookId);
    
}