
package com.example.MsTesting;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/")
public class Controller {


    private final ServiceLayer serviceLayer;


    public Controller(ServiceLayer serviceLayer)
{
    this.serviceLayer = serviceLayer;
}    
    @GetMapping("/id")
    public Integer getId(){
        return serviceLayer.getId();
    }

    @GetMapping("/name")
    public String getName(){
        return serviceLayer.getUserName();
    }
   

    
}
