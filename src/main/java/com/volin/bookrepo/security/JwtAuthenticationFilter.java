package com.volin.bookrepo.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Класс, который обрабатывает токен JWT: 
 * - читает токен из атрибута Authorization заголовка запроса 
 * - проверяет токен   
 * - загружает данные
 * пользователя, связанные с токеном   * - изменить данные пользователя в Spring
 * Security
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	@Autowired
	private JwtTokenProvider tokenProvider;

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

	/**
	 *
	 * Método que valida el token JWT, carga el usuario autenticado y lo pasa a
	 * Spring Security
	 *
	 * @param request
	 * @param response
	 * @param filterChain
	 *
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			// получаем JWT токен из запроса
			String jwt = getJwtFromRequest(request);

			// токен-провайдер проверяет токен 
			if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {

				//и если все ок, то извлекаем user_id, 
				Long userId = tokenProvider.getUserIdFromJWT(jwt);

				// по нему userDetails:
				UserDetails userDetails = customUserDetailsService.loadUserById(userId);
				
				// Релизуем интерфейс Authentication , предназначенный для простого представления 
				// имени пользователя и пароля:
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				// записываем в него запрос  
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				// сохраняем информацию в контексте для дальнейшего использования
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (Exception ex) {
			logger.error("Could not set user authentication in security context", ex);
		}

		filterChain.doFilter(request, response);
	}

	/**
	 * Получить токен JWT из заголовка запроса.
	 *
	 * @param request
	 *
	 * @return токен JWT
	 */
	private String getJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7, bearerToken.length());
		}
		return null;
	}
}
