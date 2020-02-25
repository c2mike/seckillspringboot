package springbootseckill.dto;

import lombok.Data;
import springbootseckill.entity.Seckill;
import springbootseckill.entity.SeckillSuccess;

@Data
public class SeckillResult {
    private Seckill seckill;
    private SeckillSuccess seckillSuccess;
}
