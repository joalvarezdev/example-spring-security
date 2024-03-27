package com.joalvarez.springsecurity.controller;

import com.joalvarez.springsecurity.data.dto.LoginDTO;
import com.joalvarez.springsecurity.data.dto.TokenDTO;
import com.joalvarez.springsecurity.service.UserDetailsServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

	private final UserDetailsServiceImpl service;

	public AuthenticationController (UserDetailsServiceImpl service) {
		this.service = service;
	}

	public ResponseEntity<TokenDTO> login(LoginDTO dto) {
		return ResponseEntity.ok(this.service.login(dto));
	}
}
