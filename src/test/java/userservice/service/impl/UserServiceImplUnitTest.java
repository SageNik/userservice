//package userservice.service.impl;
//
//import org.junit.*;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.testcontainers.elasticsearch.ElasticsearchContainer;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//import userservice.dto.UserRegistryDto;
//import userservice.persistence.model.User;
//import userservice.service.UserService;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class UserServiceImplUnitTest {
//
//
//    @Autowired
//    private UserService userService;
//
//
//
//    @Test
//    public void registerUser(){
//        UserRegistryDto userRegistryDto = new UserRegistryDto();
//        userRegistryDto.setUsername("user");
//        userRegistryDto.setPassword("12345");
//        Mono<Object> objectMono = userService.registerUser(userRegistryDto);
//        Assert.assertNotNull(objectMono);
//    }
//
//    @Ignore
//    @Test
//    public void testFindAll() {
//        Flux<User> users = userService.getAllUsers();
//        Assert.assertNotNull(users);
//        Assert.assertTrue(users.count().block() > 0);
//    }
//}
