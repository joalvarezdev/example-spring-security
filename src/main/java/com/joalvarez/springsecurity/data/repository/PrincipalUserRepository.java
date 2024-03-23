package com.joalvarez.springsecurity.data.repository;

import com.joalvarez.springsecurity.data.entity.PrincipalUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrincipalUserRepository extends JpaRepository<PrincipalUser, Long> {

	Optional<PrincipalUser> findByUsername(String username);
}
