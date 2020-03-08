package springbootseckill.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import springbootseckill.dao.MsgLogDao;
import springbootseckill.entity.MsgLog;
import springbootseckill.enumfinal.QueueName;
import springbootseckill.service.SeckillService;

import java.util.List;

@Configuration
public class MqConfig {


    Logger logger = LoggerFactory.getLogger(MqConfig.class);
    @Autowired
    private CachingConnectionFactory cachingConnectionFactory;

    @Autowired
    private MsgLogDao msgLogDao ;
    @Bean
    public Queue getQueue()
    {
        return new Queue(QueueName.Seckill_Queue.getName(),true);
    }

    @Bean
    public TopicExchange getExchange(){
        return new TopicExchange(QueueName.Exchange_Name.getName(),true,false);
    }

    @Bean
    public Binding seckillBind()
    {
        return BindingBuilder.bind(getQueue()).to(getExchange()).with(QueueName.Seckill_RouteKey.getName());
    }

    @Bean
    public RabbitTemplate rabbitTemplate()
    {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        rabbitTemplate.setMessageConverter(Convert());
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setConfirmCallback((data,ack,cause)->{
            if(ack)
            {
                logger.info("消息{}写入exchange成功",data.getId());
                msgLogDao.updateStatusMsgLog(Integer.parseInt(data.getId()));
            }
            else
            {
                logger.info("消息{}发送到exchange失败，cause{}",data,cause);
            }
        });
        rabbitTemplate.setReturnCallback((message,replyCode,replyText,exchange,routekey)->{
            logger.info("消息从交换机到路由失败message:{},replyCode:{},replyText:{},exchange:{},routekey:{}",
                    message,
                    replyCode,
                    replyText,
                    exchange,
                    routekey);
        });

        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter Convert()
    {
        return new Jackson2JsonMessageConverter();
    }


}
