package com.joalvarez.springsecurity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestAuthController {

	@GetMapping("hello")
	public String hello() {
		return "Hello World";
	}

	@GetMapping("hello-secured")
	public String helloSecured() {
		return "Hello World Secured";
	}

	@GetMapping("hello-secured-2")
	public String helloSecuredTwo() {
		return "Hello World Secured Two";
	}
}
