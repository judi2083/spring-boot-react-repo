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
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import org.springframework.security.config.Customizer;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                 // 👇 Allow public frontend assets
                .requestMatchers(
                    "/",
                    "/index.html",
                    "/manifest.json",
                    "/favicon.ico",
                    "/logo192.png",
                    "/logo512.png",
                    "/static/**"
                ).permitAll()
                // ✅ Allow open access to token & register endpoints
                .requestMatchers("/auth/token", "/auth/register").permitAll()
                .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/employees/**").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt
                    .jwtAuthenticationConverter(jwtAuthenticationConverter())  // 👈 Your custom converter
                )
            )
            .formLogin(form -> form.disable()) // no form login
            .httpBasic(Customizer.withDefaults()); // enable basic auth for /auth/token if needed

            
            //.httpBasic(Customizer.withDefaults()) // use Basic Auth for /auth/token
            //.formLogin(form -> form.disable());   // ✅ disable default login redirect

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

    // @Bean
    // public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
    //     UserDetails user = User.builder()
    //             .username("anil")
    //             .password(passwordEncoder.encode("mysecret123"))
    //             .roles("ADMIN")
    //             .build();

    //     System.out.println("user:::"+user);

    //     return new InMemoryUserDetailsManager(user);
    // }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles");

        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtConverter;
    }


}
