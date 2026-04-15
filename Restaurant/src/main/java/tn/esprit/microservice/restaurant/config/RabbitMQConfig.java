package tn.esprit.microservice.restaurant.config;


import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class RabbitMQConfig {

    // Nom de la queue
    public static final String USER_RESTAURANT_QUEUE = "userRestaurantQueue";

    // Déclarer la queue durable pour survivre au redémarrage du broker
    @Bean
    public Queue userRestaurantQueue() {
        return new Queue(USER_RESTAURANT_QUEUE, true);
    }

    // Converter JSON <-> POJO
    // setAlwaysConvertToInferredType = true : utilise le type du paramètre
    // de la méthode @RabbitListener pour désérialiser (pas besoin de __TypeId__ header)
    @Bean
    public MessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        typeMapper.setTrustedPackages("*");
        converter.setClassMapper(typeMapper);
        converter.setAlwaysConvertToInferredType(true);
        return converter;
    }

    // Factory utilisée par @RabbitListener
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory cf, MessageConverter converter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(cf);
        factory.setMessageConverter(converter);
        factory.setConcurrentConsumers(1);
        factory.setMaxConcurrentConsumers(3);
        return factory;
    }


}
