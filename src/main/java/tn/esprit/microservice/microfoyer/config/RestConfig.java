package tn.esprit.microservice.microfoyer.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import tn.esprit.microservice.microfoyer.entity.Bloc;
import tn.esprit.microservice.microfoyer.entity.Chambre;
import tn.esprit.microservice.microfoyer.entity.Foyer;
import tn.esprit.microservice.microfoyer.entity.Reservation;

@Configuration
public class RestConfig implements RepositoryRestConfigurer {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        // Expose les IDs dans les réponses JSON
        config.exposeIdsFor(Foyer.class, Bloc.class, Chambre.class, Reservation.class);
    }
}
