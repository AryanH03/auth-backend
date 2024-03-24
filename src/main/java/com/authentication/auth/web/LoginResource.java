package com.authentication.auth.web;

import com.authentication.auth.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200" , allowCredentials = "true")
public class LoginResource {
    @Autowired
    LoginService loginService;

    @PostMapping("/register")
    public ResponseEntity<Map<String,String>> registerUser(@RequestBody Map<String,Object> body){
        return loginService.registerUser(body);
    }
    @PostMapping("/login")
    public Map<String,Object> loginUser(@RequestBody Map<String,Object> body){
        return loginService.loginUser(body);
    }

    @GetMapping("/student")
    public ResponseEntity<List<String>> getStudentName(HttpServletRequest httpServletRequest){

        return loginService.getStudentName(httpServletRequest);
    }
    @PostMapping("/sendMail")
    public void checkAndGenearate(@RequestBody Map<String ,Object> body){
        System.out.println(body);
        loginService.checkAndGenearate(body);
    }
    @PostMapping("/validateResetToken")
    public Map<String,Object> validateResetToken(@RequestBody Map<String,Object> body){
        return loginService.validateResetToken(body);
    }
    @PostMapping("/changePassword")
    public Map<String,Object> changePassword(@RequestBody Map<String,Object> body){
        return loginService.changePassword(body);
    }
}
