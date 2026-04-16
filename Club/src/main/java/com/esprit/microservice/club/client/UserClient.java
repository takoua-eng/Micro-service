package com.esprit.microservice.club.client;




import com.esprit.microservice.club.dto.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "user-service", url = "http://localhost:3000")
public interface UserClient {

    @GetMapping("/users/getuserbyid/{id}")
    User getUserById(@PathVariable("id") String id);
}
