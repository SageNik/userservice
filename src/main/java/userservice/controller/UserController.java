package userservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import userservice.dto.UserRegistryDto;
import userservice.service.UserService;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono registrationUser(@RequestBody UserRegistryDto userRegistryDto){
      return userService.registerUser(userRegistryDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/verify/{verificationCode}")
    public Mono<Object> verificationUser(@PathVariable String verificationCode){
        return userService.verifyUser(verificationCode);
    }

    @GetMapping(value = "/something")
    public Mono<String> getSomething(){
        return Mono.just("Hello body!");
    }
}
