package com.javaacademy.cinema.service.impl;

import com.javaacademy.cinema.exception.UserTokenException;
import com.javaacademy.cinema.service.ValidationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ValidationServiceImpl implements ValidationService {
    @Value("${application.admin-auth-header}")
    private String correctUserToken;

    @Override
    public void validateUserToken(String userToken) {
        if (!isTokenCorrect(userToken)) {
            throw new UserTokenException();
        }
    }

    private boolean isTokenCorrect(String userToken) {
        return correctUserToken.equals(userToken);
    }
}
