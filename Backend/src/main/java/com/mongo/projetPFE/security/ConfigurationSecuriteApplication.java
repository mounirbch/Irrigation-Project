package com.mongo.projetPFE.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity

public class ConfigurationSecuriteApplication  {


    private final  JwtFilter jwtFilter;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;



    public ConfigurationSecuriteApplication(JwtFilter jwtFilter, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.jwtFilter = jwtFilter;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;

    }

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity httpSecurity) throws Exception {
        return  httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        authorize -> authorize.requestMatchers(HttpMethod.POST,"/mounir").permitAll()
                                .requestMatchers(HttpMethod.POST,"/activation").permitAll()
                                .requestMatchers(HttpMethod.POST,"/connexion").permitAll()
                                .requestMatchers(HttpMethod.POST,"/modifier-mot-de-passe").permitAll()
                                .requestMatchers(HttpMethod.POST,"/nouveau-mot-de-passe").permitAll()
                                .requestMatchers(HttpMethod.POST,"/nouveau-mot-de-passe").permitAll()
                                .requestMatchers(HttpMethod.GET,"/login").permitAll()
                                .requestMatchers(HttpMethod.GET,"/aa").permitAll()
                                .requestMatchers(HttpMethod.POST,"/support").permitAll()
                                .requestMatchers(HttpMethod.POST,"/chat-socket/**").permitAll()
                                .requestMatchers(HttpMethod.GET,"/chat-socket/**").permitAll()
                                .requestMatchers(HttpMethod.DELETE,"/chat-socket/**").permitAll()
                                .requestMatchers(HttpMethod.HEAD,"/chat-socket/**").permitAll()
                                .requestMatchers(HttpMethod.POST,"/api/iot").permitAll()
                                .requestMatchers(HttpMethod.POST,"/calendar/tempsDirrigation").permitAll()
                                .requestMatchers(HttpMethod.GET,"/calendar/tempsDirrigation").permitAll()
                                .requestMatchers(HttpMethod.POST,"/utilisateursGoogle").permitAll()







                                .anyRequest().authenticated()

                )





                .sessionManagement(httpSecuritySessionManagementConfigurer ->
                        httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                )

                .addFilterBefore( jwtFilter, UsernamePasswordAuthenticationFilter.class)

                .build();

    }




    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws  Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService){
        DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(this.bCryptPasswordEncoder);

        return  daoAuthenticationProvider;

    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:4200")
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowedHeaders(HttpHeaders.CONTENT_TYPE, HttpHeaders.AUTHORIZATION)
                        .allowCredentials(true);
            }
        };
    }



}