package springbootseckill.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import springbootseckill.dao.SeckillDao;
import springbootseckill.entity.Seckill;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
   // private Map<Integer,Boolean> endFlag = new ConcurrentHashMap<Integer,Boolean>();
    @Autowired
    private SeckillDao seckillDao;

    public void initSeckillTable()
    {
        List<Seckill> seckillList = seckillDao.selectAll();

        for(Seckill seckill:seckillList)
        {
            String key = generatorKey(seckill.getSeckillId());
            ValueOperations ops = stringRedisTemplate.opsForValue();
            ops.set(key,""+seckill.getItemQuantity(),60*60, TimeUnit.SECONDS);

        }
    }

    public Long del(int seckillId)
    {
        String key = generatorKey(seckillId);
        ValueOperations ops = stringRedisTemplate.opsForValue();
        return ops.decrement(key);
    }

    public void increment(int seckillId)
    {
        String key = generatorKey(seckillId);
        ValueOperations ops = stringRedisTemplate.opsForValue();
        ops.increment(key);
    }

    private String generatorKey(int id)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("seckillId_");
        builder.append(id);
        return builder.toString();
    }





   /* public boolean checkItemSellDown()
    {

    }*/

   public void setItemSellDown(int seckillId)
   {
       StringBuilder builder = new StringBuilder();
       builder.append("endFlag_");
       builder.append(seckillId);
       ValueOperations vop = stringRedisTemplate.opsForValue();
       vop.setIfAbsent(builder.toString(),1,60*60,TimeUnit.SECONDS);
   }

    public boolean checkItemSellDown(int seckillId)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("endFlag_");
        builder.append(seckillId);
        ValueOperations vop = stringRedisTemplate.opsForValue();
        String flag = (String) vop.get(builder.toString());
        if(flag!=null&&flag.equals("1"))
        {
            return true;
        }
        else{
            return false;
        }
    }
}
