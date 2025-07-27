package com.bookstore.api.model;

import java.util.concurrent.ThreadLocalRandom;

import com.bookstore.api.pojo.User;

public class UserTestDataFactory {

    public static User createValidUser() {
        int id = ThreadLocalRandom.current().nextInt(100, 999);
        String email = "testuser" + id + "@positive.com";
        String password = "Test123";

        return User.builder()
                .email(email)
                .password(password)
                .build();
    }

    public static User createValidNewUser() {
        int id = ThreadLocalRandom.current().nextInt(100, 999);
        String email = "testuser" + id + "@more.com";
        String password = "Test123";

        return User.builder()
                .email(email)
                .password(password)
                .build();
    }
    
    public static User createInvalidUser() {
        return User.builder()
                .email("") // Empty email
                .password("") // Empty password
                .build();
    }

    public static User generate500ErrorData() {
        return User.builder()
                .email("") // Empty email
                .password("") // Empty password
                .build();
    }
    
    public static User createUserWithoutId() {
        String email = "nouserid" + ThreadLocalRandom.current().nextInt(1000, 9999) + "@test.com";
        return User.builder()
                .email(email)
                .password("Test123")
                .build();
    }
}
