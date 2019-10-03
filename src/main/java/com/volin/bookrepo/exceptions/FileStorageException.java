package com.volin.bookrepo.exceptions;

/**
 * Класс для определения сообщений об ошибках работы с файлами
 */
public class FileStorageException extends RuntimeException{
    public FileStorageException(String message) {
        super(message);
    }

    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
