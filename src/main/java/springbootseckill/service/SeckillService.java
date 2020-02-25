package springbootseckill.service;

import com.github.pagehelper.PageInfo;
import springbootseckill.dto.ExcuteSeckillResult;
import springbootseckill.dto.Expose;
import springbootseckill.dto.SeckillResult;
import springbootseckill.entity.Seckill;
import springbootseckill.exception.SeckillException;

import java.util.List;

public interface SeckillService {
    public List<Seckill> selectAll();

    public Seckill selectOne(int seckillId);

    public Expose exposeSeckillUrl(int seckillID, long phone);

    public SeckillResult excuteSeckillResult(int seckillId, long phone) throws SeckillException;

    public Boolean repeatedSeckill(int seckillId, long phone);

    public long syncTime();

    public boolean checkMd5(String md5, long phone);

    public String getMd5(Object object);

    public ExcuteSeckillResult getSeckillResult(int seckillId, long phone);
}
