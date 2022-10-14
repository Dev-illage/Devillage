package com.devillage.teamproject.service.auth;

import org.springframework.stereotype.Service;

public interface EmailAuthService {
    void sendEmail(String email);
    boolean verifyAuthKey(String email,String authKey);

}
