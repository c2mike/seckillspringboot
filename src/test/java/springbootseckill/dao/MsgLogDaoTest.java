package springbootseckill.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import springbootseckill.entity.MsgLog;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MsgLogDaoTest {

    @Autowired
    MsgLogDao msgLogDao;
    @Test
    void selectNeedPostMsg() {
        System.out.println(msgLogDao.selectNeedPostMsg((int) (System.currentTimeMillis()/1000)));
    }

    @Test
    void insertOneMsgLog() {
        MsgLog msgLog= new MsgLog();
        msgLog.setMsgId(1);
        msgLog.setContext("2");
        msgLog.setCreateTime(new Date());
        msgLog.setNextTryTime((int) (System.currentTimeMillis()/1000+60));
        msgLog.setStatus(0);
        msgLog.setTryCount(0);
        msgLogDao.insertOneMsgLog(msgLog);

    }

    @Test
    void updateStatusMsgLog() {
        MsgLog msgLog= new MsgLog();
        msgLog.setMsgId(1);
        msgLog.setContext("2");
        msgLog.setCreateTime(new Date());
        msgLog.setNextTryTime((int) (System.currentTimeMillis()/1000+60));
        msgLog.setStatus(1);
        msgLog.setTryCount(0);
        msgLogDao.updateStatusMsgLog(1);
    }

    @Test
    void againCommitMsgLog() {
        MsgLog msgLog= new MsgLog();
        msgLog.setMsgId(1);
        msgLog.setContext("2");
        msgLog.setCreateTime(new Date());
        msgLog.setNextTryTime((int) (System.currentTimeMillis()/1000+60));
        msgLog.setStatus(0);
        msgLog.setTryCount(1);
        msgLogDao.againCommitMsgLog(msgLog);
    }
}