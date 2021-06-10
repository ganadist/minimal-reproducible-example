package com.example.myapplication;

import com.google.auto.service.AutoService;

@AutoService(TestService.class)
public class TestServiceImpl implements TestService {
    @Override
    public String test() {
        return "Hello";
    }
}
