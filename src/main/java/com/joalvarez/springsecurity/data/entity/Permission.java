package com.joalvarez.springsecurity.data.entity;

import com.joalvarez.springsecurity.data.types.Permissions;
import jakarta.persistence.*;

@Entity
@Table(name = "permissions")
public class Permission {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true, updatable = false, nullable = false)
	@Enumerated(EnumType.STRING)
	private Permissions name;

	public Permission() {}

	public Permission(Permissions name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Permissions getName() {
		return name;
	}

	public void setName(Permissions name) {
		this.name = name;
	}
}
