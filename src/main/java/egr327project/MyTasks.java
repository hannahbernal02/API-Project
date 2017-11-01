package com.example.demorestapi;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@Component
public class MyTasks {

    RestTemplate restTemplate = new RestTemplate();

    //get request
    private void getGreeting() {
        String getUrl = "http://localhost:8080/greeting";
        Greeting g = restTemplate.getForObject(getUrl, Greeting.class);
        System.out.println(g.getContent());
    }


    //put request
    @Scheduled(cron="*/4 * * * * *")
    public void updateGreeting() {
        String url = "http://localhost:8080/updateGreeting";
        restTemplate.put(url, "Bye World", Greeting.class);
        getGreeting();
    }

    @Scheduled(cron="*/5 * * * * *")
    public void secondUpdateGreeting() {
        String url = "http://localhost:8080/updateGreeting";
        restTemplate.put(url, "Hello World", Greeting.class);
        getGreeting();
    }
}
