package com.example.BlogSpring;

import org.springframework.security.core.Authentication;

public class Checking {

    public boolean adminAccess(Authentication auth){
        return  auth.getAuthorities().toString().contains("ADMIN");
    }
}
