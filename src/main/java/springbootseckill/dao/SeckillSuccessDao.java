package springbootseckill.dao;

import springbootseckill.dto.SeckillResult;

import java.util.Date;

public interface SeckillSuccessDao {
        public int addSeckillSuccess(int seckillId, long phone, Date createTime);

        public SeckillResult querySeckillResult(int seckillId,long phone);

}
