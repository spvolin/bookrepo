package com.volin.bookrepo.security;

import com.volin.bookrepo.model.User;
import com.volin.bookrepo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Сервис, который будет загружать аутентифицированные данные пользователей
 * Этот класс расширяет {@link UserDetailsService}, который определяет метод
 * loadUserByUsername   *   * @Service Указывает, что созданный класс является
 * службой   * @Transactional Указывает, что метод выполнит транзакцию в базе
 * данных.  
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	UserRepository userRepository;

	/**
	 * Метод, используемый Spring Security для создания экземпляра UserDetails  
	 * @param usernameOrEmail - имя пользователя или адрес электронной почты 
	 * пользователя, по которому метод определяет, существует ли он 
	 * @return  экземпляр UserDetails, который потом будет использоваться для 
	 * объекта Authentication
	 * @throws
	 * UsernameNotFoundException - выбрасывается если пользователь с именем или 
	 * адресом почты usernameOrEmail не найден
	 */
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
		// Deja que la autenticación sea por usuario o email
		User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail).orElseThrow(
				() -> new UsernameNotFoundException("User not found with username or email : " + usernameOrEmail));

		return UserPrincipal.create(user);
	}

	/**
	 * Метод используется для фильтра JWTAuthenticationFilter
	 *
	 * @param id - идентификатор пользователя
	 *
	 * @return объект UserDetails
	 */
	@Transactional
	public UserDetails loadUserById(Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with id : " + id));

		return UserPrincipal.create(user);
	}

}
