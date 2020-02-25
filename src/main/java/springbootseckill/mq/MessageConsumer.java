package springbootseckill.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springbootseckill.enumfinal.QueueName;
import springbootseckill.redis.RedisService;
import springbootseckill.service.SeckillService;
import springbootseckill.util.SerializeHelp;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class MessageConsumer {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SeckillService seckillService;
    @Autowired
    private RedisService redisService;
    @RabbitListener(queues= "seckillQueue")
    public void receive(byte[] data)
    {
        SeckillMessage obj = (SeckillMessage) SerializeHelp.deserialize(data,SeckillMessage.class);
        logger.info("receive seckillMessage:{}",obj.toString());
        try{
            seckillService.excuteSeckillResult(obj.getSeckillId(),obj.getPhone());
        }
        catch (Exception e)
        {
            logger.info("errorMsg:{}",e.getMessage());
            redisService.initSeckillTable();
        }

    }

}
