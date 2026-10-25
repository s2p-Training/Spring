package com.s2p.dto;

public record LoginResponseDto(
        String message,
        UserDto user,
        String jwtToken)
{

}