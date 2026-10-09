package io.github.rubyswe.job_scheduler;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// first sprint API endpoint
@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello World, name is Rubens Laurore";
    }
}
