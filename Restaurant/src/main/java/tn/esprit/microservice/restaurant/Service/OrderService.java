package tn.esprit.microservice.restaurant.Service;

import org.springframework.stereotype.Service;
import tn.esprit.microservice.restaurant.DTO.UserDTO;
import tn.esprit.microservice.restaurant.Entity.Orders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public interface OrderService {

    // use String for userId (matches User microservice)
    Orders createOrder(String userId, Map<Long, Integer> mealQuantities);
    Orders getOrderById(Long id);
    List<Orders> getAllOrders();
    List<Orders> getOrdersByUser(String userId);
    List<Orders> getOrdersByDate(LocalDateTime date);
    Orders updateOrder(Long id, Map<Long, Integer> mealQuantities);
    void deleteOrder(Long id);

    UserDTO getUserById(String id);
    List<UserDTO> getUsersByRole(String role);
    List<UserDTO> getUsersByName(String name);
    List<UserDTO> getAllUsers();

}
