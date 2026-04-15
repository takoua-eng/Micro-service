package com.esprit.microservice.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;


@EnableDiscoveryClient

@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
//    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder){
//        return builder.routes()
//                .route("Candidat",r->r.path("/candidats/**")
//                        .uri("http://localhost:8087") )
//                .route("Job", r->r.path("/jobs/**")
//                        .uri("http://localhost:8081") )
//                .build();
//    }


    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder){
        return builder.routes()
                // MS Candidat
                .route("Candidat", r -> r.path("/candidats/**")
                        .uri("lb://candidat"))

                // MS Club
                .route("Club", r -> r.path("/clubs/**")
                        .uri("lb://club"))
                //  MS Microfoyer (Reservation)
                .route("microfoyer", r -> r.path("/reservation/**")
                        .uri("lb://microfoyer"))

                //MS Restaurant

                .route("Restaurant", r -> r
                        .path("/orders/**")
                        .uri("lb://restaurant"))

                //MS Cours

                .route("ms", r -> r.path("/api/courses/**")
                        .uri("lb://cours-ms"))
//                .route("Job", r -> r.path("/jobs/**")
//                        .uri("http://localhost:8081"))
                .route("cours-ms-extra", r -> r.path("/cours/**")
                        .uri("lb://cours-ms"))

                //MS librairies
                .route("books", r -> r.path("/api/books/**")
                        .uri("lb://bibliotheque"))

                .route("categories", r -> r.path("/api/categories/**")
                        .uri("lb://bibliotheque"))

                .route("type-categories", r -> r.path("/api/type-categories/**")
                        .uri("lb://bibliotheque"))

                .route("borrowings", r -> r.path("/api/borrowings/**")
                        .uri("lb://bibliotheque"))

                .route("reservations", r -> r.path("/api/reservations/**")
                        .uri("lb://bibliotheque"))

                .route("notifications", r -> r.path("/api/notifications/**")
                        .uri("lb://bibliotheque"))
                .route("dashboard", r -> r.path("/api/dashboard/**")
                        .uri("lb://bibliotheque"))






                .build();
    }

}
