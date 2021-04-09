package com.greece.titan.controller;


import com.greece.titan.common.response.ResponseBase;
import com.greece.titan.common.util.RestTemplateUtil;
import com.greece.titan.service.DataService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.util.concurrent.TimeUnit;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("v1")
public class TestController {
    private final RestTemplate restTemplate;
    private final RestTemplateUtil restTemplateUtil;
    private final DataService dataService;

    @GetMapping("/test1")
    public ResponseBase<Object> getTest1() {
        // 화면에 모니터링 로그 남기기
        log.debug(restTemplateUtil.createHttpInfo());

        return ResponseBase.success();
    }

    /**
     * resttemplate connection pool test 응답지연
     */
    @GetMapping("/rest/dummy")
    public String getDummy() {

        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }

        return "dummy";
    }

    @GetMapping("redisSessionTest")
    public ResponseBase<Object> redisSessionTest(HttpSession session){

        dataService.test();
        //session.setAttribute("session","kwangbi");

        return ResponseBase.success();
    }

}
