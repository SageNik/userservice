package userservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import userservice.dto.UserRegistryDto;
import userservice.enums.Role;
import userservice.exception.UserAlreadyExistsException;
import userservice.exception.VerificationCodeExpiredException;
import userservice.persistence.model.User;
import userservice.persistence.model.VerificationCode;
import userservice.persistence.repository.UserRepository;
import userservice.persistence.repository.VerificationCodeRepository;
import userservice.service.NotificationService;
import userservice.service.UserService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Value("${verification.code.expiration}")
    private Integer expiration;                           // days

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final NotificationService notificationService;
    private final VerificationCodeRepository verificationCodeRepository;


    @Override
    public Mono<Object> registerUser(@Valid UserRegistryDto registryDto) {
        return userRepository.findFirstByUsername(registryDto.getUsername())
                .flatMap(user -> Mono.error(new UserAlreadyExistsException()))
                .switchIfEmpty(createUser(registryDto));
    }

    private Mono<Object> createUser(UserRegistryDto registryDto){
        User user = modelMapper.map(registryDto, User.class);
        user.setPassword(passwordEncoder.encode(registryDto.getPassword()));
        user.setEnabled(false);
        user.setRole(Role.USER_ROLE);
        return Mono.just(user)
        .flatMap(userRepository::save)
                .flatMap(this::sendVerificationCode);
    }

    @Override
    public Mono<Object> verifyUser(String verificationCode) {
        return verificationCodeRepository.findFirstByToken(verificationCode)
                .map(verificCode -> {
                    if (new Date().after(verificCode.getExpiryDate())) {
                        return Mono.error(new VerificationCodeExpiredException());
                    } else {
                        return userRepository.findById(verificCode.getUserId())
                                .map(user -> {
                                    user.setEnabled(Boolean.TRUE);
                                    return user;
                                })
                                .flatMap(userRepository::save);
                    }
                });
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findFirstByUsername(username).blockOptional().orElseThrow(() -> new RuntimeException("User not found: " + username));
        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().name());
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), Arrays.asList(authority));
    }

    private Mono<User> sendVerificationCode(User user) {
            if(user.getUsername() != null && user.getUsername().matches("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")){
                sendVerificationCodeByEmail(getVerificationCode(user), user.getUsername());
            }else if(user.getUsername() != null && user.getUsername().matches("^\\+?3?8?(0[5-9][0-9]\\d{7})$")){
                sendVerificationCodeBySms(getVerificationCode(user), user.getUsername());
            }
        return Mono.just(user);
    }

    private void sendVerificationCodeBySms(Mono<VerificationCode> verificationCode, String username) {
        verificationCode.map(VerificationCode::getToken).subscribe(token -> {
            notificationService.sendSms(username, token);
            log.info("Verification code: " + token + " was sent by sms: " + username);
        });
           }

    private void sendVerificationCodeByEmail(Mono<VerificationCode> verificationCode, String username) {
        verificationCode.map(VerificationCode::getToken).subscribe(token -> {
            notificationService.sendEmail(username, token);
            log.info("Verification code: "+ token + " was sent by email: "+ username);
        });
    }

    private Mono<VerificationCode> getVerificationCode(User user) {
        String code = UUID.randomUUID().toString();
        VerificationCode verificationCode = VerificationCode.builder()
                .expiration(expiration)
                .token(code)
                .userId(user.getId())
                .build();
        verificationCode.setExpiryDate(Date.from(LocalDate.now().plusDays(expiration).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        return Mono.just(verificationCode)
        .flatMap(verificationCodeRepository::save);
    }
}
