package com.volin.bookrepo.repositories;

import com.volin.bookrepo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
* Интерфейс, который взаимодействует с базой данных и с моделью User через JPA
*
* @Repository указывает, что это репозиторий для Spring приложения 
 */
@Repository
public interface UserRepository  extends JpaRepository<User,Long> {
    
	Optional<User> findByEmail(String email);
    
	//@Query("select u from User u where u.username = ?1 or u.email =?2")
    Optional<User> findByUsernameOrEmail(String username, String email);	
	//@Query("select u from User u where u.username = ?1")
    Optional<User> findByUsername(String username);
    List<User> findByIdIn(List<Long> userIds);
	Optional<User> findById(Long id);  
	Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}
