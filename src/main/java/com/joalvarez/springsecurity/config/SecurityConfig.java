package com.joalvarez.springsecurity.config;

import com.joalvarez.springsecurity.config.jwt.JwtTokenFilter;
import com.joalvarez.springsecurity.config.jwt.Jwts;
import com.joalvarez.springsecurity.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final Jwts utils;

	public SecurityConfig(Jwts utils) {
		this.utils = utils;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
			.csrf(AbstractHttpConfigurer::disable)
			.httpBasic(Customizer.withDefaults())
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(request -> {

				// * Configurar endpoints publicos
				request.requestMatchers(HttpMethod.POST, "/auth/login").permitAll();

				request.requestMatchers(HttpMethod.GET, "/test/hello").permitAll();

				// * Configurar endpoints privados
				request.requestMatchers(HttpMethod.GET, "/test/hello-secured").hasAuthority("READ");

				// * Configuracion por defecto para los demas endpoints
				request.anyRequest().denyAll();
			})
			.addFilterBefore(new JwtTokenFilter(utils), BasicAuthenticationFilter.class)
			.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public AuthenticationProvider authenticationProvider(UserDetailsServiceImpl service) {
		var provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(this.passwordEncoder());
		provider.setUserDetailsService(service);

		return provider;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
