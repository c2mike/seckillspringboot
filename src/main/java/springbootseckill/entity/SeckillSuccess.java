package springbootseckill.entity;

import lombok.Data;

import java.util.Date;

@Data
public class SeckillSuccess {
    private int seckillId;
    private long phone;
    private Date createTime;

}
