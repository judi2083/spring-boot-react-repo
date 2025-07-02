// package com.example.springboot.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.Customizer;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.core.userdetails.User;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.provisioning.InMemoryUserDetailsManager;
// import org.springframework.security.web.SecurityFilterChain;

// @Configuration
// public class SecurityConfig {

//     // 👤 Define in-memory users with roles
//     @Bean
//     public InMemoryUserDetailsManager userDetailsService() {
//         UserDetails admin = User.withUsername("admin")
//             .password(passwordEncoder().encode("admin123"))
//             .roles("ADMIN")
//             .build();

//         UserDetails user = User.withUsername("user")
//             .password(passwordEncoder().encode("user123"))
//             .roles("USER")
//             .build();

//         return new InMemoryUserDetailsManager(admin, user);
//     }

//     // 🔐 Secure endpoints based on roles
//     @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//         http
//             .csrf(csrf -> csrf.disable())
//             .authorizeHttpRequests(auth -> auth
//                 // allow Swagger endpoints for all
//                 .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
//                 // only ADMIN can access employee CRUD
//                 .requestMatchers("/api/employees/**").hasRole("ADMIN")
//                 // anything else requires auth
//                 .anyRequest().authenticated()
//             )
//             .httpBasic(Customizer.withDefaults());

//         return http.build();
//     }

//     @Bean
//     public BCryptPasswordEncoder passwordEncoder() {
//         return new BCryptPasswordEncoder();
//     }
// }

package com.example.springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import org.springframework.security.config.Customizer;

import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;



@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // @Bean
    // public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    //     http
    //         .csrf(csrf -> csrf.disable())
    //         .authorizeHttpRequests(auth -> auth
    //             // 🔓 Public endpoints
    //            // .requestMatchers("/auth/token").permitAll()
    //             // 🔓 Public endpoints
    //             .requestMatchers(
    //                 "/",
    //                 "/login",
    //                 "/index.html",
    //                 "/favicon.ico",
    //                 "/manifest.json",
    //                 "/logo192.png",
    //                 "/logo512.png",
    //                 "/static/**",
    //                 "/robots.txt"
    //             ).permitAll()
    //             .requestMatchers("/auth/token").permitAll()
    //             .requestMatchers(HttpMethod.GET, "/api","/api/","/").permitAll()
    //             .requestMatchers(HttpMethod.GET, "/employees/**").permitAll()
    //             .requestMatchers(HttpMethod.GET, "/api/actuator/**","/actuator/**").permitAll()
    //             .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()

    //             // 🔐 Secure everything else
    //             .anyRequest().authenticated()
    //         )
    //         // 🔐 Use JWT authentication
    //         .oauth2ResourceServer(oauth2 -> oauth2
    //             .jwt(Customizer.withDefaults())
    //         );

    //     return http.build();
    // }

    // @Bean
    // public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    //     http
    //         .csrf(csrf -> csrf.disable())
    //         .authorizeHttpRequests(auth -> auth
    //             .requestMatchers("/auth/token", "/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
    //             .anyRequest().authenticated()
    //         )
    //         .formLogin(Customizer.withDefaults()); // 👈 enables /login form

    //     return http.build();
    // }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/token").permitAll()
                .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/employees/**").permitAll()
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults()) // use Basic Auth for /auth/token
            .formLogin(form -> form.disable());   // ✅ disable default login redirect

        return http.build();
    }



    @Bean
    public JwtDecoder jwtDecoder(ResourceLoader resourceLoader) throws Exception {
        Resource resource = resourceLoader.getResource("classpath:public.key");
        try (InputStream inputStream = resource.getInputStream()) {
            String publicKeyPEM = new String(inputStream.readAllBytes())
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");

            byte[] decoded = Base64.getDecoder().decode(publicKeyPEM);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            RSAPublicKey publicKey = (RSAPublicKey) kf.generatePublic(keySpec);

            return NimbusJwtDecoder.withPublicKey(publicKey).build();
        }
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user = User.builder()
                .username("anil")
                .password(passwordEncoder.encode("mysecret123"))
                .roles("USER")
                .build();
        System.out.println("user:::"+user);

        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
