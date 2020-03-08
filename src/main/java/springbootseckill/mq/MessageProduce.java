package springbootseckill.mq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import springbootseckill.dao.MsgLogDao;
import springbootseckill.entity.MsgLog;
import springbootseckill.enumfinal.QueueName;
import springbootseckill.util.SerializeHelp;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class MessageProduce {

    Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private MsgLogDao msgLogDao ;

    @Autowired
    private ObjectMapper objectMapper;
    public void sendMessage(Object obj)
    {
        MsgLog msgLog = new MsgLog();
        int timestamp = (int) (System.currentTimeMillis()/1000);
        msgLog.setMsgId(timestamp);
        msgLog.setNextTryTime(timestamp);
        try {
            String context = obj instanceof  String? (String) obj :objectMapper.writeValueAsString(obj);
            msgLog.setContext(obj.toString());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        msgLog.setCreateTime(new Date(timestamp*1000));
        CorrelationData correlationData = new CorrelationData(""+timestamp);
        SeckillMessage seckillMessage = (SeckillMessage) obj;
        seckillMessage.setMsgId(timestamp);
        rabbitTemplate.convertAndSend(QueueName.Exchange_Name.getName(),QueueName.Seckill_RouteKey.getName(),obj,correlationData);
        msgLogDao.insertOneMsgLog(msgLog);
    }

    @Scheduled(cron = "*/30 * * * * ?")
    public void execute()
    {
        List<MsgLog> task = msgLogDao.selectNeedPostMsg((int) (System.currentTimeMillis()/1000));
        for(MsgLog msgLog:task)
        {

            Map map = null;
            try {
                map = objectMapper.readValue( msgLog.getContext(), Map.class);
                SeckillMessage seckillMessage = objectMapper.convertValue(map,SeckillMessage.class);
                int msgID = seckillMessage.getMsgId();
                CorrelationData correlationData = new CorrelationData(""+msgID);
                rabbitTemplate.convertAndSend(QueueName.Exchange_Name.getName(),QueueName.Seckill_RouteKey.getName(),seckillMessage,correlationData);
                msgLog.setTryCount(msgLog.getTryCount()+1);
                msgLog.getNextTryTime(System.currentTimeMillis()/1000 + 60);
                msgLogDao.againCommitMsgLog(msgLog);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }
}
