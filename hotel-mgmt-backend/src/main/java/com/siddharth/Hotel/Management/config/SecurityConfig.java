package com.siddharth.Hotel.Management.config;


import com.siddharth.Hotel.Management.security.JwtAuthEntryPoint;
import com.siddharth.Hotel.Management.security.JwtAuthFilter;
import jakarta.servlet.DispatcherType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthEntryPoint point;

    @Autowired
    private JwtAuthFilter filter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/bookings/**", "/search/**").hasAnyAuthority("User", "Admin")
                                .requestMatchers("/bookings/**", "/events/**", "/paginatedView/**", "/report/**", "/coupons/**").hasAuthority("Admin")
                                .requestMatchers("/login", "/register", "/v3/api-docs/**", "/swagger-ui/**").permitAll()
                                .requestMatchers("/error").permitAll()
                                .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
                                .anyRequest().authenticated()
                )
                .exceptionHandling(excpt -> excpt.authenticationEntryPoint(point))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
