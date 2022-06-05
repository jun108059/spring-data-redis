package practice.springdata.config;

import java.util.Optional;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import redis.embedded.RedisServer;

/**
 * 로컬 환경의 경우 내장 Redis 실행
 */
@Profile({"local", "default"})
@Configuration
public class EmbeddedRedisConfig implements InitializingBean, DisposableBean {

    @Value("${spring.redis.port}")
    private int redisPort;

    private RedisServer redisServer;

    @Override
    public void destroy() {
        Optional.ofNullable(redisServer).ifPresent(RedisServer::stop);
    }

    @Override
    public void afterPropertiesSet() {
        redisServer = RedisServer.builder()
            .port(redisPort)
            .setting("maxmemory 256M")
            .build();

        redisServer.start();
    }
}
