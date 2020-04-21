package userservice.persistence.repository.r2dbc;

import org.springframework.data.r2dbc.repository.query.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import userservice.persistence.model.User;

public interface UserRepository extends ReactiveCrudRepository<User, Long> {

    @Query("select * from main.users u where u.username =  :username")
    Mono<User> findFirstByUsername(String username);

}
