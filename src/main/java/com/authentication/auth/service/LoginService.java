package com.authentication.auth.service;

import com.authentication.auth.repository.LoginRepository;
import com.authentication.auth.utilities.AppConstant;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LoginService {
    @Autowired
    JavaMailSender javaMailSender;


    @Autowired
    LoginRepository loginRepository;

    public ResponseEntity<Map<String,String>> registerUser(Map<String,Object> body){
        String username = (String)body.get("username");
        String password = (String)body.get("password");
        String eamil = (String)body.get("email");
        String fullname = (String)body.get("fullname");
        String mobile = (String)body.get("mobile");
        int insertedRows = loginRepository.registerUser(username,password,eamil,fullname,mobile);
        if (insertedRows>0){
            return ResponseEntity.ok(Map.of("status","Successful"));
        }
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(Map.of("status","Failed"));
    }

    public Map<String,Object> loginUser(Map<String,Object> body){
        String username = (String)body.get("username");
        String password = (String)body.get("password");
        return loginRepository.loginUser(username,password);

    }

    public boolean isTokenValid(HttpServletRequest httpServletRequest){
        //extract cookies
        Cookie[] cookies = httpServletRequest.getCookies();
        if(cookies==null){
            return false;
        }
        Map<String,String> cookieMap = getCookiesAsHashMap(cookies);
        Map<String,Object> result = loginRepository.validateToken(Integer.parseInt(cookieMap.get("userid")),cookieMap.get("token"));

        Integer validYn=(Integer) result.get("validYN");

        return validYn==1;

    }
    private Map<String,String> getCookiesAsHashMap(Cookie[] cookies){
        Map<String,String> cookieMap = new HashMap<>();
        for(Cookie c:cookies){
            cookieMap.put(c.getName(),c.getValue());
            System.out.println(c.getName()+" "+c.getValue());
        }
        return cookieMap;
    }
    public ResponseEntity<List<String>> getStudentName(HttpServletRequest httpServletRequest){
        boolean isValid=isTokenValid(httpServletRequest);
        System.out.println(isValid);
        if(isValid){
            return ResponseEntity.ok(List.of("feeling","Something","Something","Hello","Honey","bunny"));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.emptyList());
    }

    public void checkAndGenearate(Map<String,Object> body){
//        validate Email
        String email = (String) body.get("emailid");
        boolean validYN = validateEmail(email);
        if(!validYN){

        }
        String token = generateToken(email);

        String link = generateLink(token);
        sendMail(email,link);

//        generate token
//        send Email
    }
    public String generateLink(String token){
        return AppConstant.RESET_FRONT_END_URL+token;
    }
    public boolean validateEmail(String email){
        Map<String,Object> result = loginRepository.validateEmail(email);
        return (Integer)result.get("validYN")==1;
    }
    public String generateToken(String email){
        Map<String,Object> ans = loginRepository.generateToken(email);
        return (String)ans.get("token");
    }

    public void sendMail(String email,String link){
        MimeMessage mineMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mineMessage);
        try{
            mimeMessageHelper.setSubject("Forgot password : medium");
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setText("Forgot password link - "+link);
        }catch (MessagingException e){
            throw new RuntimeException(e);
        }
        javaMailSender.send(mineMessage);
    }
    public Map<String,Object> validateResetToken(Map<String,Object> body){
        String fpToken = (String) body.get("fpToken");
        return loginRepository.validateResetToken(fpToken);
    }
    public Map<String,Object> changePassword(Map<String,Object> body){
        String password = (String) body.get("password");
        String fpToken = (String) body.get("fpToken");
        return loginRepository.changePassword(password,fpToken);
    }
}
