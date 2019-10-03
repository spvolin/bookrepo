package com.volin.bookrepo.repositories;

import com.volin.bookrepo.model.Role;
import com.volin.bookrepo.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
* Интерфейс, который взаимодействует с базой данных и с моделью Role через JPA
*
* @Repository указывает, что это репозиторий для Spring приложения 
*/
@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByName(RoleName roleName);
}
