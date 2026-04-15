package tn.esprit.microservice.restaurant.ServiceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.microservice.restaurant.Controller.UserClient;
import tn.esprit.microservice.restaurant.DTO.UserDTO;
import tn.esprit.microservice.restaurant.Entity.Meal;
import tn.esprit.microservice.restaurant.Entity.OrderMeal;
import tn.esprit.microservice.restaurant.Entity.OrderStatus;
import tn.esprit.microservice.restaurant.Entity.Orders;
import tn.esprit.microservice.restaurant.Repository.MealRepository;
import tn.esprit.microservice.restaurant.Repository.OrderMealRepository;
import tn.esprit.microservice.restaurant.Repository.OrderRepository;
import tn.esprit.microservice.restaurant.Service.OrderService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final MealRepository mealRepository;
    private final OrderMealRepository orderMealRepository;
    private final UserClient userClient;


    //  Créer une commande
    @Override
    public Orders createOrder(String userId, Map<Long, Integer> mealQuantities) {

        // ✅ Étape 1 : Vérifier si l’utilisateur existe
        UserDTO user = userClient.getUserById(userId);
        if (user == null) {
            throw new RuntimeException("User not found with id: " + userId);
        }

        // Step 2 — Créer Order
        Orders order = new Orders();
        order.setUserId(userId);
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());
        order.setTotalPrice(0.0);
        order = orderRepository.save(order);

        // Step 3 & 4 — Créer OrderMeal + calculer totalPrice
        double total = 0.0;
        List<OrderMeal> orderMeals = new ArrayList<>();

        for (Map.Entry<Long, Integer> entry : mealQuantities.entrySet()) {
            Meal meal = mealRepository.findById(entry.getKey())
                    .orElseThrow(() -> new RuntimeException("Meal not found: " + entry.getKey()));

            total += meal.getPrice() * entry.getValue();

            OrderMeal orderMeal = new OrderMeal();
            orderMeal.setOrders(order);
            orderMeal.setMeal(meal);
            orderMeal.setQuantity(entry.getValue());
            orderMeal.setPrice(meal.getPrice());
            orderMeals.add(orderMealRepository.save(orderMeal));
        }

        order.setTotalPrice(total);
        order.setOrderMeals(orderMeals);
        return orderRepository.save(order);
    }



    //  Récupérer une commande par ID
    @Override
    public Orders getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found: " + id));
    }


    //  Récupérer toutes les commandes
    @Override
    public List<Orders> getAllOrders() {
        return orderRepository.findAll();
    }

    //  Récupérer les commandes d'un utilisateur
    @Override
    public List<Orders> getOrdersByUser(String userId) {
        return orderRepository.findByUserId(userId);
    }

    //  Récupérer les commandes par date
    @Override
    public List<Orders> getOrdersByDate(LocalDateTime date) {
        return orderRepository.findByCreatedAtBetween(
                date.toLocalDate().atStartOfDay(),
                date.toLocalDate().atTime(23, 59, 59)
        );
    }

    //  Modifier une commande (recalcul complet)
    @Override
    public Orders updateOrder(Long id, Map<Long, Integer> mealQuantities) {

        Orders order = getOrderById(id);

        // Supprimer les anciens OrderMeal
        List<OrderMeal> oldMeals = orderMealRepository.findByOrdersId(id);
        orderMealRepository.deleteAll(oldMeals);

        // Recréer les nouveaux OrderMeal
        double total = 0.0;
        List<OrderMeal> newMeals = new ArrayList<>();

        for (Map.Entry<Long, Integer> entry : mealQuantities.entrySet()) {
            Meal meal = mealRepository.findById(entry.getKey())
                    .orElseThrow(() -> new RuntimeException("Meal not found: " + entry.getKey()));

            total += meal.getPrice() * entry.getValue();

            OrderMeal orderMeal = new OrderMeal();
            orderMeal.setOrders(order);
            orderMeal.setMeal(meal);
            orderMeal.setQuantity(entry.getValue());
            orderMeal.setPrice(meal.getPrice());
            newMeals.add(orderMealRepository.save(orderMeal));
        }

        order.setTotalPrice(total);
        order.setOrderMeals(newMeals);
        return orderRepository.save(order);
    }

    //  Supprimer une commande
    @Override
    public void deleteOrder(Long id) {
        Orders order = getOrderById(id);
        orderMealRepository.deleteAll(orderMealRepository.findByOrdersId(id));
        orderRepository.delete(order);
    }


//pour communication ms openfeing
    @Override
    public List<UserDTO> getAllUsers() {
        return userClient.getAllUsers();
    }


    @Override
    public UserDTO getUserById(String id) {
        return userClient.getUserById(id);
    }

    @Override
    public List<UserDTO> getUsersByRole(String role) {
        return userClient.getUsersByRole(role);
    }

    @Override
    public List<UserDTO> getUsersByName(String name) {
        return userClient.getUsersByName(name);

    }


//********************Rabbit mq*******************



}