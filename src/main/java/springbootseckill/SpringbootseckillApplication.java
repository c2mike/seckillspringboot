package springbootseckill;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springbootseckill.mq.MessageProduce;

@SpringBootApplication
@MapperScan("springbootseckill.dao")
public class SpringbootseckillApplication {


    public static void main(String[] args) {
        SpringApplication.run(SpringbootseckillApplication.class, args);
    }

}
