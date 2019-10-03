package com.volin.bookrepo.security;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Класс, который генерирует токен JWT после успешной аутентификации пользователя   
 * @Component Указывает, что класс будет компонентом Spring
 */
@Component
public class JwtTokenProvider {
	private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

	/**
	 * Атрибуты, извлеченные из файла свойств - application.properties
	 */
	// Секретный ключ для генерации токена
	@Value("${app.jwtSecret}")
	private String jwtSecret;

	// Время жизни токена JWT
	@Value("${app.jwtExpirationInMs}")
	private int jwtExpirationInMs;

	/**
	 * Метод, который генерирует токен JWT от аутентифицированного пользователя
	 *
	 * @param authentication
	 *
	 * @return JSON-сериализованный объект токена
	 */
	public String generateToken(Authentication authentication) {

		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

		// Создаем токен, в который записана информация:
		// - id пользователя
		// - дата и время выдачи токена
		// - дата и время "протухания" токена
		// Вес это шифруется SignatureAlgorithm.HS512 с помощью ключа jwtSecret и сериализуется (JSON)
		return Jwts.builder().setSubject(Long.toString(userPrincipal.getId())).setIssuedAt(new Date())
				.setExpiration(expiryDate).signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
	}

	/**
	 * Метод, который получает информацию о пользователе из токена JWT
	 *
	 * @param token
	 *
	 * @return
	 */
	public Long getUserIdFromJWT(String token) {
		Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();

		return Long.parseLong(claims.getSubject());
	}

	/**
	 * Метод, который проверяет, является ли токен JWT правильным или действительным
	 *
	 * @param authToken
	 *
	 * @return
	 */
	public boolean validateToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException ex) {
			logger.error("Invalid JWT signature");
		} catch (MalformedJwtException ex) {
			logger.error("Invalid JWT token");
		} catch (ExpiredJwtException ex) {
			logger.error("Expired JWT token");
		} catch (UnsupportedJwtException ex) {
			logger.error("Unsupported JWT token");
		} catch (IllegalArgumentException ex) {
			logger.error("JWT claims string is empty.");
		}
		return false;
	}
}
