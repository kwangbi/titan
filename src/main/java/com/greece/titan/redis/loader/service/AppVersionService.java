package com.greece.titan.redis.loader.service;

import com.greece.titan.common.exception.BusinessException;
import com.greece.titan.common.redis.CacheConstants;
import com.greece.titan.common.redis.loader.service.MainBaseCacheService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.greece.titan.common.redis.util.RedisLoaderUtils;
import com.greece.titan.redis.loader.dto.AppVersionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j
@Service
public class AppVersionService extends MainBaseCacheService {

    @Override
    public int createKeysWithEntryName(final String metaSet, final CacheConstants.RedisType redisType) throws BusinessException {
        final List<Object> results = getRedisTemplate(CacheConstants.RedisType.MS).executePipelined(
                new RedisCallback<Object>() {
                    @Override
                    public Object doInRedis(final RedisConnection connection) throws DataAccessException {
                        try{

                            Map map = new HashMap();
                            AppVersionDTO dto = new AppVersionDTO();
                            dto.setOsType("apple");
                            dto.setImgId("1");
                            dto.setWidth("500");
                            dto.setHeight("400");
                            dto.setImgSrc("/src/img/logo.png");
                            dto.setStartDt("20210401");
                            dto.setEndDt("20211231");
                            map.put("appVersion",dto);

                            String key = getEntryName(metaSet)+":appLoad";
                            final ObjectMapper mapper = new ObjectMapper();
                            String categoryFiltersString = null;

                            try{
                                map.put("signGateGW", "61.250.20.26:9014");
                                categoryFiltersString = mapper.writeValueAsString(map);
                            }catch(JsonProcessingException e){
                                log.error("JsonProcessingException : ",e);
                            }
                            connection.set(
                                    RedisLoaderUtils.serialize(key,getRedisTemplate(redisType).getKeySerializer()),
                                    RedisLoaderUtils.serialize(categoryFiltersString,getRedisTemplate(redisType).getValueSerializer())
                            );
                        }catch (Exception e){
                            throw e;
                        }
                        return null;
                    }
        });

        int count = 0;
        for(final Object rs:results) {
            if (rs.getClass() == Boolean.class) {
                count++;
            }
        }

        return count;
    }

    //TODO 임시데이터
    @Deprecated
    private List<Map<String,String>> convertToMap(final List<AppVersionDTO> src) {
        List<Map<String,String>> list = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        String value = "{ \"signGateGW\":\"61.250.20.204:9014\", \"notice\":[ { \"osType\":\"I\", \"title\" :\"서비스점검 중입니다.\", \"cont\":\"T world 리뉴얼 개선 작업으로 인해 서비스가 점검 중입니다. 서비스점검 이후 이용해 주세요.\", \"startDt\":\"201810101159\", \"endDt\":\"201810112359\", }, { \"osType\":\"A\", \"title\" :\"서비스점검 중입니다.\", \"cont\":\"T world 리뉴얼 개선 작업으로 인해 서비스가 점검 중입니다. 서비스점검 이후 이용해 주세요.\", \"startDt\":\"201810101159\", \"endDt\":\"201810112359\", }, ], \"splash\":[ { \"osType\":\"I\", \"imgId\" :\"1\", \"imgSrc\":\"http://cdnm.tworld.co.kr/img/app/splash/default_splash_A_640x960.png\", }, { \"osType\":\"I\", \"imgId\" :\"2\", \"imgSrc\":\"http://cdnm.tworld.co.kr/img/app/splash/default_splash_A_640x1136.png\", }, { \"osType\":\"I\", \"imgId\" :\"3\", \"imgSrc\":\"http://cdnm.tworld.co.kr/img/app/splash/default_splash_A_750x1334.png\" }, { \"osType\":\"I\", \"imgId\" :\"4\", \"imgSrc\":\"http://cdnm.tworld.co.kr/img/app/splash/default_splash_A_1080x1920.png\" }, { \"osType\":\"I\", \"imgId\" :\"5\", \"imgSrc\":\"http://cdnm.tworld.co.kr/img/app/splash/default_splash_A_1125x2436.png\" }, { \"osType\":\"A\", \"imgId\" :\"6\", \"imgSrc\":\"http://cdnm.tworld.co.kr/img/app/splash/default_splash_A_640x960.png\" }, { \"osType\":\"A\", \"imgId\" :\"7\", \"imgSrc\":\"http://cdnm.tworld.co.kr/img/app/splash/default_splash_A_640x1136.png\" }, { \"osType\":\"A\", \"imgId\" :\"8\", \"imgSrc\":\"http://cdnm.tworld.co.kr/img/app/splash/default_splash_A_750x1334.png\" } ], \"ver\": [ { \"osType\":\"A\", \"new\":\"1.0.0\", \"update\":\"0.0.2\", \"end\":\"0.0.1\", \"popYn\":\"Y\", }, { \"osType\":\"I\", \"new\":\"1.0.0\", \"update\":\"0.0.2\", \"end\":\"0.0.1\", \"popYn\":\"Y\" } ] }";
        map.put("key", "appLoad");
        map.put("value", value);
        list.add(map);
        return list;
    }
}
