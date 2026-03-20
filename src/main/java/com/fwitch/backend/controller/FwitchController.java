package com.fwitch.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FwitchController {
	
	@GetMapping(value = {"/", "/fwitch", "/index.html"})
	public String forward() {
		return "forward:/index.html";
	}

}
