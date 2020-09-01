package userservice;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.repository.config.EnableReactiveElasticsearchRepositories;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import userservice.config.KafkaConnectorInitializer;

@Slf4j
@SpringBootApplication
@EnableR2dbcRepositories(basePackages = "userservice.persistence.repository.r2dbc")
@EnableReactiveElasticsearchRepositories(basePackages = "userservice.persistence.repository.elastic")
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
        KafkaConnectorInitializer.setConnectors();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
