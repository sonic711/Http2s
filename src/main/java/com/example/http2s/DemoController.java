package com.example.http2s;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@RestController
@RequestMapping(value = "/demo")
@Slf4j
public class DemoController {

    @Autowired
    private RestTemplate myRestTemplate;

    @PostMapping(value = "/go")
    public String goHttps() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>("Hello World", headers);

        ResponseEntity<String> b = myRestTemplate.exchange("https://172.20.10.102:8080/demo/testHttps", HttpMethod.POST, request, String.class);

        return b.getBody();
    }

    @PostMapping(value = "/testHttps")
    public String testHttps(@RequestBody String str) {

        return LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME) + str;

    }

}
