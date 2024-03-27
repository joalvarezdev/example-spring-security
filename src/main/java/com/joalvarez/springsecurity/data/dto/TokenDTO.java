package com.joalvarez.springsecurity.data.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"username", "token"})
public record TokenDTO(String username, String token) {
}
