package com.joalvarez.springsecurity.service;

import com.joalvarez.springsecurity.data.repository.PrincipalUserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final PrincipalUserRepository repository;

	public UserDetailsServiceImpl(PrincipalUserRepository repository) {
		this.repository = repository;
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

}
