package springbootseckill.ComandLineRunner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import springbootseckill.redis.RedisService;

@Component
public class InitRunner implements CommandLineRunner {

    @Autowired
    private RedisService redisService;
    @Override
    public void run(String... args) throws Exception {
        redisService.initSeckillTable();
    }
}
