package org.hishmalif.supporthelperbot;

import org.hishmalif.supporthelperbot.service.GeocodeYandex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Service
@SpringBootApplication
@RestController
public class SupportHelperBotApplication {
//    @Autowired
//    public GeocodeYandex geocodeYandex;
    public static void main(String[] args) {
        SpringApplication.run(SupportHelperBotApplication.class, args);
    }

//    @GetMapping("/")
//    public String a(@RequestParam("address") String address) {
//        return geocodeYandex.getCoordinates(address);
//    }
}