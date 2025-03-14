package cmf.commitField.global.config;

import org.redisson.Redisson;
import org.redisson.config.Config;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Value("${spring.redis.host}")
    public String host;

    @Value("${spring.redis.port}")
    public int port;

    @Value("${spring.redis.password}")
    public String password;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        String redisAddress = "redis://" + host + ":" + port;
        config.useSingleServer()
                .setAddress(redisAddress)
                .setPassword(password); // 비밀번호 추가
        return Redisson.create(config);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer()); // Redis 키를 문자열로 직렬화
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer()); // 값의 직렬화를 위해 -> Redis (JSON)
        redisTemplate.setConnectionFactory(connectionFactory); // 연결 다 된 Redis -> Factory와 연결
        return redisTemplate;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(host);
        configuration.setPort(port);
        configuration.setPassword(password);  // 비밀번호 설정
        return new LettuceConnectionFactory(configuration);
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

}
