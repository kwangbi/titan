package com.greece.titan.common.redis;

import com.greece.titan.dto.RedisEntity;
import org.springframework.data.repository.CrudRepository;

public interface RedisRepository extends CrudRepository<RedisEntity, Long> {
    public RedisEntity findByFirstname(String firstname);
}
