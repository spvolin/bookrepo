package com.volin.bookrepo;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

/**
 * BookRepoApp - исполняемый класс приложения.
 */
@SpringBootApplication
@EntityScan(basePackageClasses = {
		BookRepoApp.class,
        Jsr310JpaConverters.class
})
public class BookRepoApp {

    @PostConstruct
    void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(BookRepoApp.class, args);
	}
}
