package tn.esprit.microservice.restaurant.Controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tn.esprit.microservice.restaurant.DTO.UserDTO;

import java.util.List;

@FeignClient(name = "user-service", url = "http://localhost:3000")

public interface UserClient {


    // Récupérer un user par ID
    @GetMapping("/users/{id}")
    UserDTO getUserById(@PathVariable("id") String id);


    // Récupérer tous les utilisateurs
    @GetMapping("/users")
    List<UserDTO> getAllUsers();


    // Récupérer les utilisateurs par rôle
    @GetMapping("/users/role/{role}")
    List<UserDTO> getUsersByRole(@PathVariable("role") String role);


    // Récupérer les utilisateurs par nom
    @GetMapping("/users/name/{name}")
    List<UserDTO> getUsersByName(@PathVariable("name") String name);


}
