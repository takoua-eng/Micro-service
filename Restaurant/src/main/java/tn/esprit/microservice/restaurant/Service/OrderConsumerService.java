package tn.esprit.microservice.restaurant.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tn.esprit.microservice.restaurant.DTO.UserDTO;
import tn.esprit.microservice.restaurant.Entity.OrderStatus;
import tn.esprit.microservice.restaurant.Entity.Orders;
import tn.esprit.microservice.restaurant.Repository.OrderRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderConsumerService {

    private final OrderRepository orderRepository;

    public OrderConsumerService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    private static final Logger log =
            LoggerFactory.getLogger(OrderConsumerService.class);

    // 🧠 cache mémoire (comme TP)
    private List<Orders> receivedOrders = new ArrayList<>();

    // 🔥 équivalent receiveJobService()
    public void processUser(UserDTO userDTO) {

        log.info("📥 User reçu pour Order : {}", userDTO.getName());

        // 1️⃣ créer order automatique
        Orders order = createOrderFromUser(userDTO);

        // 2️⃣ ajouter au cache
        addOrderToCache(order);

        // 3️⃣ notification
        sendNotification(userDTO);
    }

    // 🟢 création order (logique métier)
    private Orders createOrderFromUser(UserDTO userDTO) {

        Orders order = new Orders();

        order.setUserId(userDTO.getId());
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());
        order.setTotalPrice(0.0);

        Orders saved = orderRepository.save(order);

        log.info("🧾 Order créée pour user : {}", userDTO.getName());

        return saved;
    }

    // 🟢 cache mémoire (comme favoriteJobDTOS)
    private void addOrderToCache(Orders order) {

        receivedOrders.add(order);

        log.info("⭐ Order ajoutée au cache : {}", order.getId());
    }

    // 🟢 notification (simulation)
    private void sendNotification(UserDTO userDTO) {

        log.info("🔔 Notification : Order créée pour {}",
                userDTO.getName());
    }

    // 🟢 endpoint debug (optionnel TP)
    public List<Orders> getReceivedOrders() {
        return receivedOrders;
    }
}
