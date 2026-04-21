package com.smartseason.modules.auth.service;

import com.smartseason.modules.auth.dto.LoginRequest;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public String login (LoginRequest request){
        return "TEMP-TOKEN";
    }

}
