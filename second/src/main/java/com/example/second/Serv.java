package com.example.second;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class Serv {

    private final Repo repo;

    private final RestTemplate restTemplate;

    public Serv(Repo repo, RestTemplate restTemplate) {
        this.repo = repo;
        this.restTemplate = restTemplate;
    }

    public Entity getEntity(Integer id) {
        return repo.findById(id).orElse(null);
    }

    public Entity saveEntity(Entity entity) {
        return repo.save(entity);
    }

    public String getUserName() {
        String res = restTemplate.getForObject("http://mstesting/name", String.class);
        return res;
    }

}
