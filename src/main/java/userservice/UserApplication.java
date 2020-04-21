package userservice;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.repository.config.EnableReactiveElasticsearchRepositories;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Slf4j
@SpringBootApplication
@EnableR2dbcRepositories(basePackages = "userservice.persistence.repository.r2dbc")
@EnableReactiveElasticsearchRepositories(basePackages = "userservice.persistence.repository.elastic")
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

//    @Bean
//    public ApplicationRunner seeder(DatabaseClient client) {
//        return args -> getSchema()
//                .flatMap(sql -> executeSql(client, sql))
//                .subscribe(count -> log.info("Init-schema created"));
//    }
//
//    private Mono<Integer> executeSql(DatabaseClient client, String sql) {
//        return client.execute(sql).fetch().rowsUpdated();
//    }
//
//    private Mono<String> getSchema() throws URISyntaxException {
//        Path path = Paths.get(ClassLoader.getSystemResource("db/migration/V1__init_schema.sql").toURI());
//        return Flux.using(() -> Files.lines(path), Flux::fromStream, BaseStream::close)
//                .reduce((line1, line2) -> line1 + "\n" + line2);
//    }
}
