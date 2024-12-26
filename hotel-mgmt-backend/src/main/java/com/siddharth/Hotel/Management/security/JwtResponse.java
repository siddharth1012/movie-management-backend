package com.siddharth.Hotel.Management.security;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
public class JwtResponse {
    private String jwtToken;
    private String name;
}
