package tn.esprit.microservice.restaurant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import tn.esprit.microservice.restaurant.Entity.Meal;
import tn.esprit.microservice.restaurant.Repository.MealRepository;

import java.time.LocalDate;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
@EnableRabbit
public class RestaurantApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestaurantApplication.class, args);
    }


    @Autowired
    private MealRepository mealRepository;

    @Bean
    ApplicationRunner init() {
        return args -> {

            Meal pizza = new Meal();
            pizza.setName("Pizza");
            pizza.setPrice(15.0);
            pizza.setAvailable(true);
            mealRepository.save(pizza);

            Meal burger = new Meal();
            burger.setName("Burger");
            burger.setPrice(12.0);
            burger.setAvailable(true);
            mealRepository.save(burger);

            Meal croissant = new Meal();
            croissant.setName("Croissant");
            croissant.setPrice(5.0);
            croissant.setAvailable(true);
            mealRepository.save(croissant);

            Meal salade = new Meal();
            salade.setName("Salade");
            salade.setPrice(8.0);
            salade.setAvailable(true);
            mealRepository.save(salade);

            System.out.println("=== Meals enregistrés ===");
            mealRepository.findAll().forEach(System.out::println);
        };
    }

}
