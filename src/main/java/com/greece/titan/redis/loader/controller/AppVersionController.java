package com.greece.titan.redis.loader.controller;

import com.greece.titan.common.redis.CacheConstants;
import com.greece.titan.redis.loader.service.AppVersionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("redis")
public class AppVersionController {

    private final AppVersionService appVersionService;

    @GetMapping("/appVersion")
    public int setRedisAppVersion() {

        int i = appVersionService.createKeysWithEntryName("appVersion", CacheConstants.RedisType.MS);

        return i;
    }
}
