package com.greece.titan;

import com.greece.titan.common.redis.RedisRepository;
import com.greece.titan.dto.RedisEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisRepository repository;

    @Test
    public void redisRepository() {
        RedisEntity entity = new RedisEntity();
        entity.setFirstname("kwangmo");
        entity.setLastname("yang");
        entity.setAge(28);
        repository.save(entity);
        RedisEntity findEntity = repository.findByFirstname(entity.getFirstname());
        log.debug("findEntity : {}",findEntity.toString());
        //System.out.println(findEntity.toString());
    }
}
