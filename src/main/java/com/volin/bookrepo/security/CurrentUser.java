package com.volin.bookrepo.security;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.*;

/**
* Интерфейс, который работает как пользовательская аннотация
* Это позволяет нам сократить все аннотации, указанные до одного
*
* Следующие аннотации будут сгруппированы в одну:
* @Target
* @Retention
* @Documented
*
* @AuthenticationPrincipal эта запись позволяет получить доступ к аутентифицированному пользователю
*
*/
@Target({ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AuthenticationPrincipal
public @interface CurrentUser {

}
