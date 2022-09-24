package com.tajdingurdal.app.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tajdingurdal.app.SpringApplicationContext;
import com.tajdingurdal.app.service.UserService;
import com.tajdingurdal.app.shared.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.tajdingurdal.app.model.request.UserLoginRequestModel;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * when User sign in to our application then we will return an Authentication Token.
 * And this class create an Authentication Token to return User when she/he sign in to our app.
 */
@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {


    private AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    /*
    Spring Framework will trigger this method when User try to log in with her username and password.
    Spring Framework take her username and password and bring them to this method.
    Then, this method will convert her username and password as JSON to Java Object. And check username, password information through AuthenticationManager class.
    Because AuthenticationManager using loadUserByUsername() method which in UserDetailsService class.
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {

        try {
            // convert JSON to Java object and return UserLoginRequestModel object.
            UserLoginRequestModel loginRequestModel = new ObjectMapper().readValue(req.getInputStream(), UserLoginRequestModel.class);
            log.info("email, password: {} {}", loginRequestModel.getEmail(), loginRequestModel.getPassword());
            return authenticationManager.authenticate( // using loadUserByUsername() method to validate username and password of User.
                    new UsernamePasswordAuthenticationToken(
                            loginRequestModel.getEmail(),
                            loginRequestModel.getPassword(),
                            new ArrayList<>()
                    )
            );

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /*
    if first step is success I mean that if attemptAuthentication() method validate username and password of User,
    then we call successfulAuthentication() method.
    if username and password not correct then successfulAuthentication() method won't be called.
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        String username = ((User) auth.getPrincipal()).getUsername(); // email

        String token = Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret())
                .compact();

        UserService userService = (UserService) SpringApplicationContext.getBean("userServiceImpl"); // we get access to UserServiceImpl bean through SpringApplicationContext class.
        UserDto userDto = userService.getUserByEmail(username);

        res.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
        res.addHeader("UserID", userDto.getUserId());
    }

}
/*
 We write this filter, and we need to configure it to WebSecurity class.
*/