package springbootseckill.mq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springbootseckill.enumfinal.QueueName;

@Configuration
public class MqConfig {


    @Bean
    public Queue getQueue()
    {
        return new Queue(QueueName.Seckill_Queue.getName(),true);
    }

}
