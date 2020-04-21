package userservice.persistence.repository.elastic;

import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import org.springframework.data.r2dbc.repository.query.Query;
import reactor.core.publisher.Mono;
import userservice.persistence.model.User;

public interface UserElasticRepository extends ReactiveElasticsearchRepository<User, Long> {

    @Query("select * from main.users u where u.username =  :username")
    Mono<User> findFirstByUsername(String username);

}
