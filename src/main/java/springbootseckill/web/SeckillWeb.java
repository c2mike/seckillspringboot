package springbootseckill.web;

import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springbootseckill.dto.CommitSeckillResult;
import springbootseckill.dto.ExcuteSeckillResult;
import springbootseckill.dto.Expose;
import springbootseckill.dto.TestCase;
import springbootseckill.entity.Seckill;
import springbootseckill.enumfinal.ErrorMessage;
import springbootseckill.mq.MessageProduce;
import springbootseckill.mq.SeckillMessage;
import springbootseckill.redis.RedisService;
import springbootseckill.service.SeckillService;

import java.util.*;


@Controller
public class SeckillWeb {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SeckillService seckillService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private MessageProduce produce;
   /* @RequestMapping(value = "seckill/{start}/{size}",method = RequestMethod.GET)
    public PageInfo<Object> getSeckill(@PathVariable("start") int start, @PathVariable("size") int size)
    {
        return seckillService.selectAll(start,size);
    }*/

    @RequestMapping(value = "seckill/{seckillId}" ,method = RequestMethod.GET)
    @ResponseBody
    public Seckill getSeckill(@PathVariable("seckillId")  int seckillId)
    {
        return seckillService.selectOne(seckillId);
    }

    @RequestMapping(value="seckill/timestamp", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Long> getTimeStamp()
    {
        Map<String,Long> result = new HashMap<String,Long>();
        result.put("time",seckillService.syncTime());
        return result;
    }

    @RequestMapping(value = "seckill/{seckillId}/exposeurl/{phone}",method=RequestMethod.GET)
    @ResponseBody
    public Expose ExposeUrl(@PathVariable("phone") long phone,@PathVariable("seckillId") int seckillId)
    {
        return seckillService.exposeSeckillUrl(seckillId,phone);
    }

    @RequestMapping(value = "seckill/testcase",method = RequestMethod.GET)
    @ResponseBody
    public List<TestCase> testCase() {
        int[] id = {1000,1001,1002};
        Random random = new Random();
        List<TestCase> t = new ArrayList<TestCase>();
        for (int i = 0; i < 1; i++) {
            TestCase c = new TestCase();
            c.setSeckillId(id[i%3]);
            c.setPhone(Math.abs(random.nextInt(99999)));
            c.setMd5(seckillService.getMd5(c.getPhone()));
            t.add(c);
        }
        return t;

    }



    @RequestMapping(value="seckill/commit",method = RequestMethod.POST)
    @ResponseBody
    public CommitSeckillResult commit(
           // @PathVariable("seckillId") int seckillId,@PathVariable("md5") String md5,@PathVariable("phone") long phone
           @RequestParam("seckillId")int seckillId,
           @RequestParam("md5")String md5,
           @RequestParam("phone")long phone
    )
    {
        logger.info("id:{},md5:{},phone:{}",seckillId,md5,phone);
        CommitSeckillResult excuteSeckillResult = new CommitSeckillResult();
        excuteSeckillResult.setSuccess(true);
        do{
            if(!seckillService.checkMd5(md5,phone))
            {
                excuteSeckillResult.setSuccess(false);
                excuteSeckillResult.setErrorMessage(ErrorMessage.DATA_UNSAFE.getMessage());
                break;
            }

            long remain = redisService.del(seckillId);
            if(remain<0)
            {
                excuteSeckillResult.setSuccess(false);
                excuteSeckillResult.setErrorMessage(ErrorMessage.Remain_Over.getMessage());
                break;
            }

            SeckillMessage seckillMessage = new SeckillMessage();
            seckillMessage.setSeckillId(seckillId);
            seckillMessage.setPhone(phone);
            produce.sendMessage(seckillMessage);

            excuteSeckillResult.setErrorMessage(ErrorMessage.SUCCESS_ENQUEUE.getMessage());

        }while(false);
        return excuteSeckillResult;
    }

    @RequestMapping("seckill/{seckillId}/{phone}")
    @ResponseBody
    public ExcuteSeckillResult QueryResult(@PathVariable("seckillId") int seckillId,@PathVariable("phone") long phone)
    {
        return seckillService.getSeckillResult(seckillId,phone);
    }

    @RequestMapping(value="seckill",method = RequestMethod.GET)
    public String list(Model model)
    {
        List<Seckill> data = seckillService.selectAll();
        model.addAttribute("list",data);
        return "list";
    }

    @RequestMapping("seckill/{seckillId}/detail")
    public String detail(@PathVariable("seckillId") int seckillId,Model model)
    {
        Seckill seckill = seckillService.selectOne(seckillId);
        model.addAttribute("seckill",seckill);
        return "detail";
    }

    @RequestMapping("seckill/{seckillId}/result/{phone}")
    @ResponseBody
    public ExcuteSeckillResult query(@PathVariable("seckillId") int seckillId,@PathVariable("phone") long phone)
    {
        return seckillService.getSeckillResult(seckillId,phone);
    }

}
