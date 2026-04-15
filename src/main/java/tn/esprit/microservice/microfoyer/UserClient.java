package tn.esprit.microservice.microfoyer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tn.esprit.microservice.microfoyer.dto.UserDto;

import java.util.List;

@FeignClient(name = "user-ms") // Nom du service enregistré dans Eureka
public interface UserClient {

    @GetMapping("/users")
    List<UserDto> getAllUsers();

    @GetMapping("/users/{id}")
    UserDto getUserById(@PathVariable("id") String id);

    @GetMapping("/users/role/{role}")
    List<UserDto> getUsersByRole(@PathVariable("role") String role);

    @GetMapping("/users/name/{name}")
    List<UserDto> getUsersByName(@PathVariable("name") String name);
}
