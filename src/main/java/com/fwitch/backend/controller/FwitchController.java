package com.fwitch.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FwitchController {
	
	@GetMapping(value = {"/", "/fwitch"})
	public String forward() {
		return "forward:/index.html";
	}

}
