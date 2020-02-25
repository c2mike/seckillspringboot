package springbootseckill.redis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest

class RedisServiceTest {

    @Autowired
    private RedisService redisService;
    @Test
    void checkItemSellDown() {
        redisService.checkItemSellDown(1000);
    }
}