package userservice.persistence.repository.r2dbc;

import org.springframework.data.r2dbc.repository.query.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import userservice.persistence.model.VerificationCode;

public interface VerificationCodeRepository extends ReactiveCrudRepository<VerificationCode, Long> {

    @Query("select * from main.verification_code where token =:token")
    Mono<VerificationCode> findFirstByToken(String token);
}
