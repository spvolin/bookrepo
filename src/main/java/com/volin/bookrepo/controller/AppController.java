package com.volin.bookrepo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.annotations.Api;

/**
 * The Class AppController - контроллер приложения
 */
@Controller
@Api(value = "AppController")
public class AppController {

	@RequestMapping("/")
	String home(ModelMap modal) {
		modal.addAttribute("title","Book Repository");
		return "index";
	}
	
	@RequestMapping("/partials/v2/{page}")
	String partialHandler(@PathVariable("page") final String page) {
		return page;
	}
}
