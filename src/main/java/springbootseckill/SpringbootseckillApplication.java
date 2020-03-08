package springbootseckill;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springbootseckill.mq.MessageProduce;

@EnableScheduling
@SpringBootApplication
@MapperScan("springbootseckill.dao")
public class SpringbootseckillApplication {


    public static void main(String[] args) {
        SpringApplication.run(SpringbootseckillApplication.class, args);
    }

}
