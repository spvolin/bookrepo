package com.volin.bookrepo.service;

import com.volin.bookrepo.exceptions.MyFileNotFoundException;
import com.volin.bookrepo.model.Book;
import com.volin.bookrepo.model.DBFile;
import com.volin.bookrepo.model.User;
import com.volin.bookrepo.repositories.BookRepository;
import com.volin.bookrepo.repositories.DBFileRepository;
import com.volin.bookrepo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Класс, который выступает в качестве промежуточного звена между контроллером и
 * хранилищами, в дополнение содержит основную логику
 * 
 * @Service Указывает, что созданный класс является службой  
 * @Transactional Указывает, что метод выполнит транзакцию в базе данных.
 */
@Service
public class DBFileStorageService {

	@Autowired
	private DBFileRepository dbFileRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BookRepository bookRepository;

	/**
	 * Метод получает информацию о файле по его имени 
	 * @param fileName имя файла, по которо будет извлечена метаинформация о файле
	 *
	 * @return
	 */
	public DBFile getFile(String fileName) {
		return dbFileRepository.findByFileName(fileName)
				.orElseThrow(() -> new MyFileNotFoundException("DBFile not found with id " + fileName));
	}

	
	/**
	 * Транзакционный (! это не ошибка) метод извлечения файла из БД
	 * Транзакционность нужна на тот случай если файл большой и записан в БД частями 
	 * (так, например, делает Postgresql)
	 * Метод используется для выполнения загрузки файла на сторону клиента
	 * @param id - UUID файла
	 * @return экземпляр DBFile, если он найден, иначе null
	 */
	@Transactional
	public Optional<DBFile> getFileById(String id) {
		return dbFileRepository.findByUuid(id);
	}
	
	/**
	 * Транзакционный метод извлечения всех файлов из БД
	 * @return экземпляры DBFile
	 */
	@Transactional
	public List<DBFile> getAll() {
		return dbFileRepository.findAll();
	}

	/**
	 * Транзакционный метод извлечения всех файлов книги из БД
	 * Параметры:
	 * @param book_id - идентификатор книги
	 * @param name - имя пользователя, которому должна принадлежать книга
	 * @return экземпляры DBFile книги 
	 */

	@Transactional
	public List<DBFile> getAllByBook(Long book_id, String name) {
		User user = userRepository.findByUsernameOrEmail(name, name)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with username or email : " + name));
		List<Book> books = bookRepository.doesBookBelongToUser(book_id, user.getId());
		return books != null & books.size() == 1 ? dbFileRepository.findByBookId(book_id) : null;
	}

	/**
	 * Метод удаляет файл из БД по его имени
	 * Лучше удалять по id, поскольку имена не уникальны
	 * Метод должен быть удален после создания нового метода
	 *
	 * @param fileName - имя файла
	 */
	@Transactional
	public void deleteFile(String fileName) {
		dbFileRepository.removeAllByFileName(fileName);
	}
	
	/**
	 * Метод удаляет файл из БД по id
	 *
	 * @param id - uuid файла
	 */
	@Transactional
	public void deleteFileById(String id) {
		dbFileRepository.deleteById(id);
	}

}
