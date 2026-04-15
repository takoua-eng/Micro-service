package tn.esprit.microservice.restaurant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.microservice.restaurant.Service.OrderConsumerService;
import tn.esprit.microservice.restaurant.Service.OrderService;
import tn.esprit.microservice.restaurant.config.RabbitMQConfig;
import tn.esprit.microservice.restaurant.DTO.UserDTO;

@Service
public class UserConsumer {


    private final OrderConsumerService orderConsumerService;  // ← changer

    private static final Logger log = LoggerFactory.getLogger(UserConsumer.class);

    public UserConsumer(OrderConsumerService orderConsumerService) {  // ← changer
        this.orderConsumerService = orderConsumerService;
    }


    @RabbitListener(
            queues = RabbitMQConfig.USER_RESTAURANT_QUEUE,
            containerFactory = "rabbitListenerContainerFactory"
    )
    public void receiveUser(UserDTO userDTO) {
        log.info("📥 User reçu depuis RabbitMQ : {}", userDTO.getName());
        orderConsumerService.processUser(userDTO);  // ← changer
    }





}
