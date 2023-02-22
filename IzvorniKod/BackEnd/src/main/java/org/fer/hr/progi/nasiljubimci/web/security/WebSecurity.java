package org.fer.hr.progi.nasiljubimci.web.security;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.fer.hr.progi.nasiljubimci.web.security.SecurityConstants.SIGN_UP_URL;

@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, SIGN_UP_URL).permitAll()
                .antMatchers(HttpMethod.GET, "/users/*").permitAll()
                .antMatchers(HttpMethod.POST, "/users/*").permitAll()
                .antMatchers(HttpMethod.GET, "/posts/*").permitAll()
                .antMatchers(HttpMethod.POST, "/posts/*").permitAll()
                .antMatchers(HttpMethod.GET, "/events/*").permitAll()
                .antMatchers(HttpMethod.POST, "/events/*").permitAll()
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .antMatchers(HttpMethod.POST, "/error").permitAll()
                .antMatchers(HttpMethod.GET, "/demo").hasRole("USER")
                .antMatchers(HttpMethod.GET, "/demoAdmin").hasRole("MAINTAINER")
                .antMatchers(HttpMethod.GET, "/demoSuperAdmin").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/admin/*").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/admin/*").hasRole("ADMIN")
                .antMatchers(HttpMethod.PATCH, "/admin/*").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/admin/*").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), userDetailsService))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }
}