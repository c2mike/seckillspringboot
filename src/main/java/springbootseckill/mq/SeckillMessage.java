package springbootseckill.mq;

import lombok.Data;

import java.io.Serializable;

@Data
public class SeckillMessage implements Serializable {
    private int seckillId;
    private long phone;
    private int msgId;
}
