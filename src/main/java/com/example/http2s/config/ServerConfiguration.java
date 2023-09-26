package com.example.http2s.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.util.CollectionUtils;

import java.util.List;


@Configuration
@Slf4j
public class ServerConfiguration {
    @Value("${server.ssl.valid-commonname}")
    private List<String> validCommonNameList;

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .anyRequest().authenticated()
                )
                .x509(x509 ->
                        x509.subjectPrincipalRegex("CN=(.*?)(?:,|$)")
                                .userDetailsService(userDetailsService())
                );
        log.debug("http = {} ......", http);
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            log.info("Current Common Name = {}", username);
            log.info("CommonNameList = {}", validCommonNameList);
            return new ValidCert(username, CollectionUtils.contains(validCommonNameList.listIterator(), username));
        };
    }
}
