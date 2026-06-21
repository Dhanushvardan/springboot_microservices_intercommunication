package com.example.MsTesting;
import org.springframework.stereotype.Service;

@Service
public class ServiceLayer {

    public Integer getId() {
        return 123;
    }

    public String getUserName() {
        return "Dhanush";
    }
    
}
