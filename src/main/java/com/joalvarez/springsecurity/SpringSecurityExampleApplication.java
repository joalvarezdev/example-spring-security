package com.joalvarez.springsecurity;

import com.joalvarez.springsecurity.data.entity.Permission;
import com.joalvarez.springsecurity.data.entity.PrincipalUser;
import com.joalvarez.springsecurity.data.entity.Role;
import com.joalvarez.springsecurity.data.repository.PrincipalUserRepository;
import com.joalvarez.springsecurity.data.types.Permissions;
import com.joalvarez.springsecurity.data.types.Roles;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Set;

@SpringBootApplication
public class SpringSecurityExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityExampleApplication.class, args);
	}

	@Bean
	CommandLineRunner init(PrincipalUserRepository repository, PasswordEncoder bcrypt) {
		return args -> {
			var create = new Permission(Permissions.CREATE);
			var read = new Permission(Permissions.READ);
			var update = new Permission(Permissions.UPDATE);
			var delete = new Permission(Permissions.DELETE);

			var adminRole = new Role(Roles.ADMIN, Set.of(create, read, update));
			var userRole = new Role(Roles.USER, Set.of(create, read));
			var invitedRole = new Role(Roles.INVITED, Set.of(read));
			var devRole = new Role(Roles.DEVELOPER, Set.of(read, update, delete));

			var adminUser = new PrincipalUser("deppe", bcrypt.encode("deppe"), true, false, false, false, Set.of(adminRole, devRole));
			var user = new PrincipalUser("user", bcrypt.encode("user"), true, false, false, false, Set.of(userRole));
			var invited = new PrincipalUser("invited", bcrypt.encode("invited"), true, false, false, false, Set.of(invitedRole));

			repository.saveAll(List.of(adminUser, user, invited));
		};
	}
}
