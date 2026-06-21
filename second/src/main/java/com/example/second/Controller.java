package com.example.second;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class Controller {

    private final Serv serv;

    public Controller(Serv service) {
        this.serv = service;
    }

    @GetMapping("/{id}")
    public Entity getEntity(@PathVariable Integer id) {
        return serv.getEntity(id);
    }

    @PostMapping("/post")
    public Entity saveEntity(@RequestBody Entity entity) {
        return serv.saveEntity(entity);
    }

    @GetMapping("/getusername")
    public String getName() {
        return serv.getUserName();
    }

}
