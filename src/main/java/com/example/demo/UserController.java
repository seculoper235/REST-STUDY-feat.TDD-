package com.example.demo;

import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
/* consumes와 produces란?
 * REST API를 좀더 Type Safe하게 만들어주는 것으로써,
 * consumes는 요청 헤더의 Conten-Type이 해당 미디어 타입과 일치할 때, 즉 올바른 타입일 경우만 매핑하고,
 * produces는 요청 헤더의 Accept에 해당 미디어 타입이 존재할 때, 즉 응답을 받을 수 있을 때 매핑이 된다
 */
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaTypes.HAL_JSON_VALUE)
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String home() {
        return "<h1>Hello World!</h1>";
    }

    @GetMapping("/finduser/{id}")
    public ResponseEntity<User> findUser(@PathVariable Integer id) {
        User user = userService.findUserById(id);

        return ResponseEntity.ok(user);
    }

    @GetMapping("/getuser/{id}")
    public ResponseEntity<User> getUser(@PathVariable Integer id) {
        User user = userService.getUserById(id);
        System.out.println("사용자 ID = " + user.getId() + "\n사용자 이름 = " + user.getName() + "\n사용자 설명 = " + user.getDescription());
        return ResponseEntity.ok(user);
    }

    @PostMapping("/user")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User newuser = userService.createUser(user);
        URI createUri = linkTo(methodOn(UserController.class).createUser(newuser)).slash(user.getId()).toUri();

        return ResponseEntity.created(createUri).body(newuser);
    }
}
