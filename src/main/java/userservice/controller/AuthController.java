package userservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    @GetMapping("/user/me")
    public ResponseEntity<?> getUser(Authentication authentication){
        return ResponseEntity.ok(authentication.getPrincipal());
    }
}
