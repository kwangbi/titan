package com.greece.titan.redis.loader.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greece.titan.common.redis.CacheConstants;
import com.greece.titan.common.redis.util.CacheUtils;
import com.greece.titan.redis.loader.service.AppVersionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;



/** 
* @packageName : com.greece.titan.redis.loader.controller 
* @fileName : AppVersionController.java 
* @author : Jerry Yang 
* @date : 2021.04.23 
* @description : 
* =========================================================== 
* DATE 					AUTHOR 			NOTE 
* ----------------------------------------------------------- 
* 2021.04.23		Jerry Yang 		최초 생성 
*/
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("redis")
public class AppVersionController {

    private final AppVersionService appVersionService;

    /** 
    * @methodName : setRedisAppVersion 
    * @author : Jerry Yang
    * @date : 2021.04.23 
    * @return 
    */
    @GetMapping("/appVersion")
    public int setRedisAppVersion() {

        int i = appVersionService.createKeysWithEntryName("appVersion", CacheConstants.RedisType.MS);
        
        String jsonStr = CacheUtils.getCacheData("AppVersion", "appLoad", String.class, CacheConstants.RedisType.MS);
        log.debug("jsonStr  : {}",jsonStr );
        
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(jsonStr);
            JSONObject jsonObj = (JSONObject) obj;
            Object appObj = jsonObj.get("appVersion");
            log.debug("appObj  : {}",appObj.toString());
            
            ObjectMapper mapper = new ObjectMapper();
            try {
            	Map<String, Object> dataMap = null;
            	
            	dataMap = mapper.readValue(appObj.toString(), Map.class);
            	
            	log.debug("dataMap  : {}",dataMap.toString());
                	
            }catch(Exception e) {
            	
            }
            
            //String appVersion = (String) jsonObj.get("appVersion");
            String signGateGW = (String) jsonObj.get("signGateGW");
            
            //log.debug("appVersion  : {}",appVersion );
            log.debug("signGateGW  : {}",signGateGW );
        }catch(Exception e) {
        	e.printStackTrace();
        }
        /*
        Map<String, Object> dataMap = null;
        dataMap = CacheUtils.getCacheData("AppVersion", "appLoad", Map.class, CacheConstants.RedisType.MS);
        
        log.debug("dataMap : {}",dataMap.toString());
        */

        return i;
    }
}
