package com.juzi.domain.p;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

@CacheConfig(cacheNames = "users")
public interface UserRepository extends JpaRepository<User, String> {

    @Cacheable(key = "#p0", condition = "#p0.length() < 10")
    User findByName(String name);

    @CachePut(key = "#p0.name")
    User save(User user);
}
