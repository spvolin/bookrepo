package com.volin.bookrepo.configuration;

import com.volin.bookrepo.security.CustomUserDetailsService;
import com.volin.bookrepo.security.JwtAuthenticationEntryPoint;
import com.volin.bookrepo.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Клас конфигурирует параметры безопасности spring security
 *
 * @Configuration Указывает, что это класс конфигурации.
 * @EnableWebSecurity Включить безопасность Web
 * @EnableGlobalMethodSecurity Включить аннотации безопасности
 *      securedEnabled: включить аннотацию @Secured
 *      jsr250Enabled: включить аннотацию @RolesAllowed
 *      prePostEnabled: включить аннотациb @PreAuthorize b @PostAuthorize
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // Сервис обработки аутентификационных параметров пользователя 
    @Autowired
    CustomUserDetailsService customUserDetailsService;

    // Возвращает ошибку 401 в ответ на неавторизованные запросы к защищенным ресурсам
    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    // Фильтр-бин обработки JWT-токенов
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    /**
     * Класс управляет настройками аутентификации
     *
     * @param authenticationManagerBuilder
     *
     * @throws Exception
     */
    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        // Se define la clase que recupera los usuarios y el algoritmo para procesar las contraseñas
        authenticationManagerBuilder
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    /**
     * @return
     *
     * @throws Exception
     */
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * Método que devuelve el algoritmo que se utilizará para procesar las contraseñas
     *
     * @return BCryptPasswordEncoder Objeto que procesara las contraseñas
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * HTTP-конфигуратор выполняет настройки CORS, CSRF, управляет сессиями, 
     * добавляет правила защиты ресрусов приложения:
     * Использование куки отключено
     * Настройка CORS активирована
     * Фильтр CSRF деактивирован
     * Настройка маршрутов, которые требуют авторизации
     * Публичные маршруты настроены
     * Доступ к статическим файлам настроен
     * Задан фильтр JWT-аутентификации, который выполняет 
     * проверку на аутентификацию пользователя  
     * @param http
     *
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(unauthorizedHandler)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/",
                        "/favicon.ico",
                        "/**/*.png",
                        "/**/*.gif",
                        "/**/*.svg",
                        "/**/*.jpg",
                        "/**/*.html",
                        "/**/*.ftl",                        
                        "/**/*.css",
                        "/**/*.js")
                .permitAll()
                .antMatchers("/v2/api-docs*")
                .permitAll()                
                .antMatchers("/webjars/**")
                .permitAll()                
                .antMatchers("/csrf*")
                .permitAll()                
                .antMatchers("/**/swagger*/**")
                .permitAll()                
                .antMatchers("/**/home*")
                .permitAll()                
                .antMatchers("/**/login*")
                .permitAll()               
                .antMatchers("/**/signup*")
                .permitAll()                                
                .antMatchers("/**/book/**")
                .permitAll()                
                .antMatchers("/api/auth/**")
                .permitAll()
                .antMatchers("/api/user/checkUsernameAvailability", "/api/user/checkEmailAvailability")
                .permitAll()
                .antMatchers("/api/book/**", "/api/files/**", "/api/users/**")
                .permitAll()
                .anyRequest()
                .authenticated();

        // Add our custom JWT security filter
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

    }

}
