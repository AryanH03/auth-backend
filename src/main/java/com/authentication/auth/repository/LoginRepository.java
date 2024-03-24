package com.authentication.auth.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class LoginRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;
    public int registerUser(String username,String password,String eamil,String fullname,String mobile){
        return jdbcTemplate.update("EXEC [dbo].[sp_insert_users] ?,?,?,?,?",username,password,eamil,fullname,mobile);
    }
    public Map<String,Object> loginUser(String username, String password){
        return jdbcTemplate.queryForMap("EXEC [dbo].[sp_login] ?,?",username,password);
    }

    public Map<String,Object> validateToken(int userId,String token){
        return jdbcTemplate.queryForMap("EXEC [dbo].[sp_validate_token] ?,?",userId,token);
    }

    public  Map<String,Object> validateEmail(String email){
        return jdbcTemplate.queryForMap("EXEC dbo.sp_check_email ?",email);
    }
    public Map<String,Object> generateToken(String email){
        return  jdbcTemplate.queryForMap("EXEC dbo.generate_token ?",email);
    }
    public Map<String,Object> validateResetToken(String fpToken){
        return jdbcTemplate.queryForMap("EXEC dbo.sp_fp_check_token ?",fpToken);
    }
    public Map<String,Object> changePassword(String password,String fpToken){
        return jdbcTemplate.queryForMap("EXEC dbo.sp_check_token_update ?,?",password,fpToken);
    }
}
