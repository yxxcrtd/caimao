package com.caimao.bana.jobs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
@EnableScheduling
public class MainServer {
    public static void main(String[] args) throws Exception {
        SpringApplication app = new SpringApplication(MainServer.class);
        Set<Object> set = new HashSet<Object>();
        set.add("classpath:/spring/application.xml");
        set.add("classpath:/spring/application-dubbo.xml");
        set.add("classpath:/spring/application-redis.xml");
        app.setSources(set);
        app.run(args);
    }
}