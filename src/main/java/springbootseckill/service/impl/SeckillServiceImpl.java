package springbootseckill.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import springbootseckill.dao.MsgLogDao;
import springbootseckill.dao.SeckillDao;
import springbootseckill.dao.SeckillSuccessDao;
import springbootseckill.dto.ExcuteSeckillResult;
import springbootseckill.dto.Expose;
import springbootseckill.dto.SeckillResult;
import springbootseckill.entity.Seckill;
import springbootseckill.enumfinal.ErrorMessage;
import springbootseckill.exception.SeckillException;
import springbootseckill.redis.RedisService;
import springbootseckill.service.SeckillService;

import java.util.Date;
import java.util.List;
@Service
public class SeckillServiceImpl implements SeckillService {
    private static final String salt = "abc_123()";


    @Autowired
    private SeckillDao seckillDao;
    @Autowired
    private SeckillSuccessDao seckillSuccessDao;
    @Autowired
    private RedisService redisService;
    @Override
    public List<Seckill> selectAll()
    {

        List<Seckill> data = seckillDao.selectAll();
        return data;

    }

    public boolean test()
    {
        return seckillSuccessDao!=null;
    }

    @Override
    public Seckill selectOne(int seckillId) {
        return seckillDao.selectOne(seckillId);
    }

    @Override
    public Expose exposeSeckillUrl(int seckillID,long phone) {

        Expose expose = new Expose();
        expose.setSuccess(true);
        do{
            Seckill seckill= seckillDao.selectOne(seckillID);
            if(seckill==null)
            {
                expose.setSuccess(false);
                expose.setErrorMessage(ErrorMessage.INVAILD_ARGS.getMessage());
                break;
            }
            if(seckill.getStartTime().getTime()>System.currentTimeMillis()||seckill.getEndTime().getTime()<System.currentTimeMillis())
            {
                expose.setSuccess(false);
                expose.setNow(System.currentTimeMillis());
                expose.setStartTime(seckill.getStartTime().getTime());
                expose.setEndTime(seckill.getEndTime().getTime());
                expose.setErrorMessage(ErrorMessage.Time_Error.getMessage());
                break;
            }
            expose.setMd5(getMd5(phone));

        }while(false);
        return expose;
    }

    @Override
    @Transactional
    public SeckillResult excuteSeckillResult(int seckillId, long phone) throws SeckillException {
        SeckillResult excuteSeckillResult;

        do{
            Date date = new Date();
            int rows = seckillSuccessDao.addSeckillSuccess(seckillId,phone,date);
            if(rows==0)
            {
                throw new SeckillException(ErrorMessage.Repeated_Seckill.getMessage());
            }

            rows = seckillDao.updateQuantity(seckillId,date);
            if(rows==0)
            {
                throw new SeckillException(ErrorMessage.Del_Quantity_Error.getMessage());
            }
           excuteSeckillResult =  seckillSuccessDao.querySeckillResult(seckillId,phone);
        }while(false);
        return excuteSeckillResult;
    }

    @Override
    public Boolean repeatedSeckill(int seckillId, long phone) {
        return seckillSuccessDao.querySeckillResult(seckillId,phone) == null? false:true;
    }

    @Override
    public long syncTime() {
        return System.currentTimeMillis();
    }

    @Override
    public boolean checkMd5(String md5, long phone) {
        String digest = getMd5(phone);
        return digest.equals(md5);
    }

    public String getMd5(Object object)
    {
        String rstr = String.valueOf(object);
        return DigestUtils.md5DigestAsHex(rstr.getBytes());
    }

    @Override
    public ExcuteSeckillResult getSeckillResult(int seckillId, long phone) {
        ExcuteSeckillResult excuteSeckillResult = new ExcuteSeckillResult();
        SeckillResult seckillResult = seckillSuccessDao.querySeckillResult(seckillId,phone);
        if(seckillResult!=null)
        {
            excuteSeckillResult.setSuccess(true);
            return excuteSeckillResult;
        }
        else
        {
            if(redisService.checkItemSellDown(seckillId))
            {
                excuteSeckillResult.setSuccess(false);
                excuteSeckillResult.setErrorMessage(ErrorMessage.Remain_Over.getMessage());
                return excuteSeckillResult;
            }
            Seckill seckill = seckillDao.selectOne(seckillId);
            if(seckill==null)
            {
                excuteSeckillResult.setSuccess(false);
                excuteSeckillResult.setErrorMessage(ErrorMessage.INVAILD_ARGS.getMessage());
                return excuteSeckillResult;
            }
            if(seckill.getItemQuantity()<=0)
            {
                redisService.setItemSellDown(seckillId);
                excuteSeckillResult.setErrorMessage(ErrorMessage.Remain_Over.getMessage());
                excuteSeckillResult.setSuccess(false);
                return  excuteSeckillResult;
            }
            else{
               excuteSeckillResult.setSuccess(false);
               excuteSeckillResult.setErrorMessage(ErrorMessage.WAIT_IN_QUEUE.getMessage());
               return excuteSeckillResult;
            }
        }
    }
}
