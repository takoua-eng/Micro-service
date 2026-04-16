package tn.esprit.microservice.restaurant.Controller;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.microservice.restaurant.DTO.UserDTO;
import tn.esprit.microservice.restaurant.Entity.Orders;
import tn.esprit.microservice.restaurant.Service.OrderConsumerService;
import tn.esprit.microservice.restaurant.Service.OrderService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {


    private final OrderService orderService;

    private final OrderConsumerService orderConsumerService;


    // ✅ Créer une commande
    @PostMapping
    public ResponseEntity<Orders> createOrder(
            @RequestParam String userId,  //orders?userId=69a0
            @RequestBody Map<Long, Integer> mealQuantities) //{ "1"(mealId): 2(quantity), "3": 1 }
    {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.createOrder(userId, mealQuantities));
    }


    // ✅ Récupérer une commande par ID
    // GET /api/orders/8
    @GetMapping("/{id}")
    public ResponseEntity<Orders> getById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }


    // ✅ Récupérer toutes les commandes
    // GET /api/orders
    @GetMapping
    public ResponseEntity<List<Orders>> getAll() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }


    // ✅ Récupérer les commandes d'un utilisateur
    // GET /api/orders/user/1
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Orders>> getByUser(@PathVariable String userId) {
        return ResponseEntity.ok(orderService.getOrdersByUser(userId));
    }


    // ✅ Récupérer les commandes par date
    // GET /api/orders/date?date=2024-01-15T00:00:00
    @GetMapping("/date")
    public ResponseEntity<List<Orders>> getByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        return ResponseEntity.ok(orderService.getOrdersByDate(date));
    }



    // ✅ Modifier une commande
    // PUT /api/orders/1
    // Body : { "2": 5, "3": 2 }
    @PutMapping("/{id}")
    public ResponseEntity<Orders> updateOrder(
            @PathVariable Long id, //id dordre à modifier
            @RequestBody Map<Long, Integer> mealQuantities) {
        return ResponseEntity.ok(orderService.updateOrder(id, mealQuantities));
    }

    // ✅ Supprimer une commande
    // DELETE /api/orders/1
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok("Order deleted successfully");
    }







    //*****pour communication entre ms openfeign****

    @GetMapping("/users")
    public List<UserDTO> getAllUsers() {
        return orderService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public UserDTO getUserById(@PathVariable String id) {
        return orderService.getUserById(id);
    }

    @GetMapping("/users/role/{role}")
    public List<UserDTO> getUsersByRole(@PathVariable String role) {
        return orderService.getUsersByRole(role);
    }

    @GetMapping("/users/name/{name}")
    public List<UserDTO> getUsersByName(@PathVariable String name) {
        return orderService.getUsersByName(name);
    }

    // Health check / quick mapping test
    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }




//******************for rabbit mq ******************
    @GetMapping("/from-rabbit")
    public List<Orders> getOrdersFromRabbit() {
        return orderConsumerService.getReceivedOrders();
    }
}
