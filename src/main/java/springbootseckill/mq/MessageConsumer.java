package springbootseckill.mq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springbootseckill.dao.MsgLogDao;
import springbootseckill.enumfinal.QueueName;
import springbootseckill.redis.RedisService;
import springbootseckill.service.SeckillService;
import springbootseckill.util.SerializeHelp;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class MessageConsumer {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SeckillService seckillService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private MsgLogDao msgLogDao;
    @Autowired
    private ObjectMapper objectMapper;
    @RabbitListener(queues= "seckillQueue")
    public void consume(Message message, Channel channel)  {
        Map map = null;
        long tag = message.getMessageProperties().getDeliveryTag();
        try {
            map = objectMapper.readValue(new String(message.getBody()), Map.class);
            SeckillMessage seckillMessage = objectMapper.convertValue(map,SeckillMessage.class);
            int msgId = seckillMessage.getMsgId();
            int effect = msgLogDao.updateStatusMsgLog(msgId);
            if(0==effect)
            {
                logger.info("重复消费msgid：{}",msgId);
                channel.basicAck(tag,false);
                return;
            }
            seckillService.excuteSeckillResult(seckillMessage.getSeckillId(),seckillMessage.getPhone());
            msgLogDao.updateStatusMsgLog(msgId);
            channel.basicAck(tag,false);

        } catch (JsonProcessingException e) {
            logger.error("转换消息异常",e);
            try {
                channel.basicNack(tag,false,false);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        catch(Exception e)
        {
            logger.info("errorMsg:{}",e.getMessage());
            try {
                channel.basicNack(tag,false,false);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            redisService.initSeckillTable();
        }

    }
//    public void receive(byte[] data)
//    {
//
//        SeckillMessage obj = (SeckillMessage) SerializeHelp.deserialize(data,SeckillMessage.class);
//        logger.info("receive seckillMessage:{}",obj.toString());
//        try{
//            seckillService.excuteSeckillResult(obj.getSeckillId(),obj.getPhone());
//        }
//        catch (Exception e)
//        {
//            logger.info("errorMsg:{}",e.getMessage());
//            redisService.initSeckillTable();
//        }
//
//    }

}
