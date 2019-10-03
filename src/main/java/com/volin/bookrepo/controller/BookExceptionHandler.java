package com.volin.bookrepo.controller;

import com.volin.bookrepo.exceptions.NonExistingBookException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Отображает исключения на HTTP-коды.
 * @author sergei.volin
 */
@RestControllerAdvice
public class BookExceptionHandler {

    @ExceptionHandler(NonExistingBookException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleNonExistingBook() {
    }
}
