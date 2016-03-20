package com.st.config.server.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private DaoAuthenticationProvider daoAuthenticationProvider;

    @RequestMapping(path="/authenticate", method= RequestMethod.POST)
    public ResponseEntity<?> validLoginParams(@RequestParam("username") String username, @RequestParam("password") String password) {
        Authentication auth = new UsernamePasswordAuthenticationToken(username, password);
        Authentication postAuth = daoAuthenticationProvider.authenticate(auth);

        if(postAuth.isAuthenticated()) {
            Map<String, String> response = new HashMap<>();
            String token = "Basic " + new String(Base64.getEncoder().encode((username + ":" + password).getBytes()));
            response.put("token", token);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
    }


}
