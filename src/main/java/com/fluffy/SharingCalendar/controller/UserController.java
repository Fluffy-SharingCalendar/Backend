package com.fluffy.SharingCalendar.controller;

import com.fluffy.SharingCalendar.model.User;
import com.fluffy.SharingCalendar.service.UserDetailsService;
import com.fluffy.SharingCalendar.service.UserService;
import com.fluffy.SharingCalendar.util.JwtUtil;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:8080") // 클라이언트의 주소
@Log4j2
@RestController
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;




}
