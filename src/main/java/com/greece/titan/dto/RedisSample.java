package com.greece.titan.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@RedisHash("point")
public class RedisSample implements Serializable {

    @Id
    private String id;
    private Long amount;
    private LocalDateTime refreshTime;

    @Builder
    public RedisSample(String id,Long amount,LocalDateTime refreshTime){
        this.id=id;
        this.amount = amount;
        this.refreshTime = refreshTime;
    }

    public void refresh(Long amount,LocalDateTime refreshTime){
        if(refreshTime.isAfter(this.refreshTime)){
            this.amount = amount;
            this.refreshTime = refreshTime;

        }
    }
}
