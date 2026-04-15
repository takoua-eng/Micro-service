package tn.esprit.microservice.microfoyer;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import tn.esprit.microservice.microfoyer.entity.Foyer;
import tn.esprit.microservice.microfoyer.repository.IFoyerRepository;

import java.util.Collections;
@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
public class MicrofoyerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicrofoyerApplication.class, args);
    }


    @Bean
    ApplicationRunner initData(
            IFoyerRepository foyerRepository
    ) {
        return args -> {
            // ---- FOYERS ----
            Foyer f1 = new Foyer(null, "foyer1", 220L, Collections.emptyList());
            Foyer f2 = new Foyer(null, "Foyer El Manan", 500L, Collections.emptyList());
            foyerRepository.save(f1);
            foyerRepository.save(f2);

            // affichage console
            foyerRepository.findAll().forEach(System.out::println);

        };
    }

}
