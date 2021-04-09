package com.greece.titan.common.delegator;

import java.net.URI;
import java.nio.charset.Charset;

import com.greece.titan.common.config.WebTConfiguration;
import com.greece.titan.common.exception.BusinessException;
import com.greece.titan.common.util.MultiValueMapConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class callWebTService<T> {

    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;
    private WebTConfiguration webTConfiguration;

    @Autowired
    public callWebTService(RestTemplate restTemplate, ObjectMapper objectMapper, WebTConfiguration webTConfiguration) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.webTConfiguration = webTConfiguration;
    }

    @SuppressWarnings("unchecked")
    public T get(String url) {
        return callApiExchange(url, HttpMethod.GET, null, (Class<T>) Object.class);
    }

    public T get(String url, Object body, Class<T> clazz) {
        //log.info("webT : GetEntity : domain : [{}] / api_key : [{}]",this.webTConfiguration.getDomain(),this.webTConfiguration.getApiKey());

        MultiValueMap<String, String> params = MultiValueMapConverter.convert(objectMapper, body);
        URI targetUrl = UriComponentsBuilder.fromUriString(url).queryParams(params).build().encode().toUri();

        // custom header
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        httpHeaders.add("Authorization", "Bearer " + this.webTConfiguration.getApiKey()); //API KEY

        String jsonBody = restTemplate.exchange(targetUrl, HttpMethod.GET, new HttpEntity<>(body, httpHeaders), String.class).getBody();

        // 복호화 필요함.
        T responseBody = decryptBody(jsonBody, clazz);

        return responseBody;
    }

    @SuppressWarnings("unchecked")
    public T post(String url, Object body) {
        return callApiExchange(url, HttpMethod.POST, body, (Class<T>) Object.class);
    }

    public T post(String url, Object body, Class<T> clazz) {
        //log.info("webT : PostEntity : domain : [{}] / api_key : [{}]",this.webTConfiguration.getDomain(),this.webTConfiguration.getApiKey());
        return callApiExchange(url, HttpMethod.POST, body, clazz);
    }

    private T callApiExchange(String url, HttpMethod httpMethod, Object body, Class<T> clazz) {
        // custom header
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + this.webTConfiguration.getApiKey());// API KEY

        String jsonBody = restTemplate.exchange(url, httpMethod, new HttpEntity<>(body, httpHeaders), String.class).getBody();

        // 복호화 필요함.
        T responseBody = decryptBody(jsonBody, clazz);

        return responseBody;
    }


    /*
     * body 복호화 처리
     */
    private T decryptBody(String jsonBody, Class<T> clazz) {

        log.debug("jsonBody : {}", jsonBody);

        ObjectMapper mapper = new ObjectMapper();
        try {

            T responseBody = mapper.readValue(jsonBody, clazz);

            return responseBody;
        } catch (Exception e) {
            throw new BusinessException("COM001");
        }

    }

    /*
     * txcode로 redis에서 api url 조회
     *
     */
    public String getRedisAPIUrl(String txcode) {
        String url = "";
        String domain = this.webTConfiguration.getDomain();

        if ("API001".equals(txcode)) {
            url = domain + "/rtn/getTest";
        } else if ("API002".equals(txcode)) {
            url = domain + "/rtn/postTest";
        }

        log.info("url : {}", url);

        return url;
    }


}

