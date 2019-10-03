package com.volin.bookrepo.controller;

import com.volin.bookrepo.exceptions.MyFileNotFoundException;
import com.volin.bookrepo.model.DBFile;
import com.volin.bookrepo.service.DBFileStorageService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
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
@RequestMapping("/api/files")
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private DBFileStorageService fileStorageService;

    
    /**
     * Метод возвращает пользователю файл книги по его запросу
     * @param uuid - идентификатор файла
     * @return файл книги в ResponseEntity<Resource>
     */
    @GetMapping("/downloadFile/id/{uuid}")
    public ResponseEntity<Resource> downloadFile(@PathVariable(name="uuid") String uuid) {

    	DBFile dbFile = fileStorageService.getFileByUuid(uuid).
    			orElseThrow(() -> new MyFileNotFoundException("DBFile not found with id " + uuid));

    	logger.info("Fetching file with FileName: {}", dbFile.getFileName());

    	return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(dbFile.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dbFile.getFileName() + "\"")
                .body(new ByteArrayResource(dbFile.getData()));
    }

 }
