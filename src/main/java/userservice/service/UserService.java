package userservice.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import reactor.core.publisher.Mono;
import userservice.dto.UserRegistryDto;

public interface UserService extends UserDetailsService {

    Mono<Object> registerUser(UserRegistryDto registryDto);

    Mono<Object> verifyUser(String verificationCode);
}
