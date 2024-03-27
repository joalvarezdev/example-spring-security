package com.joalvarez.springsecurity.service;

import com.joalvarez.springsecurity.config.jwt.Jwts;
import com.joalvarez.springsecurity.data.dto.LoginDTO;
import com.joalvarez.springsecurity.data.dto.TokenDTO;
import com.joalvarez.springsecurity.data.repository.PrincipalUserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final PrincipalUserRepository repository;
	private final Jwts utils;
	private final PasswordEncoder passwordEncoder;

	public UserDetailsServiceImpl(PrincipalUserRepository repository, Jwts utils, PasswordEncoder passwordEncoder) {
		this.repository = repository;
		this.utils = utils;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		var user = this.repository.findByUsername(username)
			.orElseThrow(() -> new UsernameNotFoundException("Username or Password do not exists."));

		var authorities = new ArrayList<SimpleGrantedAuthority>();

		user.getRoles().forEach(role -> {
			authorities.add(new SimpleGrantedAuthority("ROLE_".concat(role.getName().name())));
			role.getPermissions().forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission.getName().name())));
		});

		return new User(
			user.getUsername(),
			user.getPassword(),
			user.isActive(),
			!user.isAccountExpired(),
			!user.isCredentialExpired(),
			!user.isAccountLocked(),
			authorities
		);
	}

	public TokenDTO login(LoginDTO dto) {

		SecurityContextHolder.getContext().setAuthentication(this.authenticate(dto.username(), dto.password()));

		var token = this.utils.generateToken(SecurityContextHolder.getContext().getAuthentication());

		return new TokenDTO(
			this.utils.getUsername(token),
			token
		);
	}

	private Authentication authenticate(String username, String password) {
		var user = this.loadUserByUsername(username);

		if (!this.passwordEncoder.matches(password, user.getPassword())) {
			throw new BadCredentialsException("Username or Password do not exists.");
		}

		return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
	}
}
