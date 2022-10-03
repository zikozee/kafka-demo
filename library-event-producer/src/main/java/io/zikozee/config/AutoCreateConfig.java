package io.zikozee.config;


import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.TopicBuilder;

/**
 * @author: Ezekiel Eromosei
 * @created: 03 October 2022
 */

@Configuration
@Profile(value = "local")
public class AutoCreateConfig {

    @Bean // not recommended for production
    public NewTopic libraryEvents(){
        return TopicBuilder.name("library-events")
                .partitions(3)
                .replicas(3)
                .build();
    }
}
