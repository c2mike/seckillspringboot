package springbootseckill.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Seckill {

    private int seckillId;
    private String itemName;
    private int itemQuantity;
    private double itemPrice;
    private Date creatTime;
    private Date endTime;
    private Date startTime;
}
