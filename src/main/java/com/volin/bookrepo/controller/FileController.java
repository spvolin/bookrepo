package com.volin.bookrepo.controller;

import com.volin.bookrepo.exceptions.MyFileNotFoundException;
import com.volin.bookrepo.model.Book;
import com.volin.bookrepo.model.DBFile;
import com.volin.bookrepo.payload.FileResponse;
import com.volin.bookrepo.service.DBFileStorageService;
import com.volin.bookrepo.util.CustomErrorType;

import io.swagger.annotations.ApiOperation;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Класс. 
 *
 * @RestController REST-контроллер. Обрабатывает HTTP-запросы на выполнение операций 
 * с файлами. Пока содержит один метод загрузки файла на клиента.
 * @RequestMapping Указывает URL запросов, которые будет обрабатывать контроллер 
 */
@RestController
@RequestMapping("/api")
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private DBFileStorageService fileStorageService;

    
    /**
     * Метод возвращает пользователю файл книги по его запросу
     * @param id - uuid идентификатор файла
     * @return файл книги в ResponseEntity<Resource>
     */
    @GetMapping("/files/downloadFile/id/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable(name="id") String id) {

    	DBFile dbFile = fileStorageService.getFileById(id).
    			orElseThrow(() -> new MyFileNotFoundException("DBFile not found with id " + id));

    	logger.info("Fetching file with FileName: {}", dbFile.getFileName());

    	return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(dbFile.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dbFile.getFileName() + "\"")
                .body(new ByteArrayResource(dbFile.getData()));
    }

	// ------------------- Delete a File -----------------------------------------

    /**
	 * Метод удаляет файл.
	 *
	 * @param id - UUID файла
	 * @return the response entity
	 */
	@ApiOperation(value = "Удалить файл книги ", response = DBFile.class, tags = "deleteFile")
	@RequestMapping(value = "/files/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> deleteFile(@PathVariable("id") String id) {
		logger.info("Fetching & Deleting File with id {}", id);

		Optional<DBFile> file = fileStorageService.getFileById(id);
		if (file == null) {
			logger.error("Unable to delete. File with id {} not found.", id);
			return new ResponseEntity<CustomErrorType>(new CustomErrorType("Unable to delete. File with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		fileStorageService.deleteFileById(id);
		file = fileStorageService.getFileById(id);
		System.out.println(""+file);
		return new ResponseEntity<FileResponse>(HttpStatus.NO_CONTENT);
	}
 }
