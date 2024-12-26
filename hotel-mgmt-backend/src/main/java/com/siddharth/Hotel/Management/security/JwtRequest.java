package com.siddharth.Hotel.Management.security;

import com.siddharth.Hotel.Management.model.Role;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class JwtRequest {
    private String name;
    private String password;
    private Role roles;
}
