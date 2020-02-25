package springbootseckill.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springbootseckill.enumfinal.QueueName;
import springbootseckill.util.SerializeHelp;

@Service
public class MessageProduce {

    Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sendMessage(Object obj)
    {
        byte[] data = SerializeHelp.serialize(obj);
        amqpTemplate.convertAndSend(QueueName.Seckill_Queue.getName(),data);
        logger.info("seckillMessage:{}",obj.toString());
    }
}
