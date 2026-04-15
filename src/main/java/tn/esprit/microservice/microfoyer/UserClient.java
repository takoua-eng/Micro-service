package tn.esprit.microservice.microfoyer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import tn.esprit.microservice.microfoyer.dto.UserDto;

import java.util.List;

@FeignClient(name = "user-service", url= "http://localhost:3000")
public interface UserClient {

    /*@RequestMapping("users")
    public List<UserDto> getAllUsers();*/
    @RequestMapping("users/getuserbyid/{id}")
    public UserDto getUserById(@PathVariable("id") String id);
}
