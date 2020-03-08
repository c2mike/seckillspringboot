package springbootseckill.dao;

import org.springframework.stereotype.Repository;
import springbootseckill.entity.MsgLog;

import java.util.List;

@Repository
public interface MsgLogDao {
        public List<MsgLog> selectNeedPostMsg(int now);

        public int insertOneMsgLog(MsgLog msgLog);

        public int updateStatusMsgLog(int msgId);

        public int againCommitMsgLog(MsgLog msglog);
}
