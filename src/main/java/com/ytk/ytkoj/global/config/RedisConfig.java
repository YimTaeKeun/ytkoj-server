package com.ytk.ytkoj.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {
    /*
    RedisConnectionFactory는 SpringDataRedis에서 Redis와의 연결을 관리하는 인터페이스이다.
    구현체로는 LettuceConnectionFactory (디폴트), JedisConnectionFactory가 있으며, 전자는 비동기 방식을 지원한다.
    해당 구현체에서는 레디스의 호스트 및 포트, 비밀번호, 데이터베이스 인덱스, 타임아웃을 지정할 수 있다.
    * */

    /*
    Redis Template은 레디스와 상호작용할 수 있는 설정 및 기능들의 인터페이스를 제공해주는 클래스이다.
    이를 상속하는 클래스가 StringRedisTemplate인데, 문자열을 효과적으로 직렬화하는 클래스이다.
    * */

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        return redisTemplate;
    }
}
