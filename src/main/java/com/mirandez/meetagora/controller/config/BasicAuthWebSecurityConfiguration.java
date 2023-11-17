package com.mirandez.meetagora.controller.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class BasicAuthWebSecurityConfiguration {

    @Value("${api.username}")
    String userCredential ;

    @Value("${api.password}")
    String pass;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

//        String[] staticResources  =  {
//                "/css/**",
//                "/img/**",
//                "/on-boarding/v1/validation/**",
//                "/login/**",
//                "/attenance/**",
//                "/validation/**",
//                "/gcp/images/**",
//        };

        String[] staticResources  =  {
                "/",
                "/meetagora-services/css/**",
                "/meetagora-services/img/**",
                "/meetagora-services/on-boarding/v1/validation/**",
                "/meetagora-services/login/**",
                "/meetagora-services/attendance/**",
                "/meetagora-services/validation/**",
                "/meetagora-services/gcp/images/**",
                "/meetagora-services/send-message",
                "/send-message"
        };
                http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(staticResources).permitAll()
                .and()
                .httpBasic();

        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        PasswordEncoder passwordEncoder = this.passwordEncoder();
        UserDetails user = User
                .withUsername(userCredential)
                .password(passwordEncoder.encode(pass))
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Utiliza BCrypt para codificar contraseñas
    }


}
