package com.joalvarez.springsecurity.data.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.time.LocalDateTime;

@JsonPropertyOrder({"username", "token", "expiration"})
public record TokenDTO(String username, String token, @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss") LocalDateTime expiration) {
}
