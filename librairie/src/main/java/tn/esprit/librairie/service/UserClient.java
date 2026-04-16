package tn.esprit.librairie.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "http://localhost:3000")
public interface UserClient {

    @GetMapping("/users/{id}")
    UserDto getUserById(@PathVariable("id") String id);

    @GetMapping("/users/{id}/role")
    String getUserRole(@PathVariable("id") String id);
}
