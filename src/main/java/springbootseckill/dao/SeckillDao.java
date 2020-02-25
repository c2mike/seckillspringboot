package springbootseckill.dao;

import springbootseckill.entity.Seckill;

import java.util.Date;
import java.util.List;

public interface SeckillDao {

    public List<Seckill> selectAll();

    public Seckill selectOne(int seckillId);

    public int updateQuantity(int seckillId, Date createTime);

}
