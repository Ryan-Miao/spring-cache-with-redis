package com.test.springcachewithredis.controller;

import com.test.springcachewithredis.entity.Room;
import com.test.springcachewithredis.entity.User;
import com.test.springcachewithredis.service.HelloService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "spring-cache-redis", description = "spring cache 集成 redis")
@RestController
@RequestMapping("/api/v1")
public class HelloController {

    private final HelloService service;

    @Autowired
    public HelloController(HelloService service) {
        this.service = service;
    }

    @GetMapping("/hi-cache/{id}")
    public User hi(@PathVariable String id) {
        return service.sayHi(id);
    }

    @GetMapping("/hello-cache/{id}")
    public User hello(@PathVariable String id) {
        return service.sayHello(id);
    }

    @GetMapping("/rooms/{id}")
    public Room getRoom(@PathVariable String id) {
        return service.getRoom(id);
    }

}
