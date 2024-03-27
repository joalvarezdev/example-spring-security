package com.joalvarez.springsecurity.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

public class JwtTokenFilter extends OncePerRequestFilter {

	private final Jwts utils;

	public JwtTokenFilter(Jwts utils) {
		this.utils = utils;
	}

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

		var token = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (Objects.nonNull(token)) {
			token = token.replace("Bearer", "");

			this.utils.validateToken(token);

			var username = this.utils.getUsername(token);
			var stringAuthorities = this.utils.getSpecificClaim(token, "authorities").asString();

			var authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(stringAuthorities);

			var authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);

			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		filterChain.doFilter(request, response);
	}
}
