package com.greece.titan.controller;


import com.greece.titan.common.response.ResponseBase;
import com.greece.titan.common.util.RestTemplateUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("v1")
public class TestController {
    private final RestTemplate restTemplate;
    private final RestTemplateUtil restTemplateUtil;

    @GetMapping("/test1")
    public ResponseBase<Object> getTest1(){

        String msg = "3333";
        log.debug("2222");


        return ResponseBase.success();
    }

}
