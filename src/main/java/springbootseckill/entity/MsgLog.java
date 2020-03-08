package springbootseckill.entity;

import lombok.Data;

import java.util.Date;


public class MsgLog {
    private int msgId;
    private String context;
    private int status;
    private int tryCount;

    @Override
    public String toString() {
        return "MsgLog{" +
                "msgId=" + msgId +
                ", context='" + context + '\'' +
                ", status=" + status +
                ", tryCount=" + tryCount +
                ", createTime=" + createTime +
                ", nextTryTime=" + nextTryTime +
                '}';
    }

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTryCount() {
        return tryCount;
    }

    public void setTryCount(int tryCount) {
        this.tryCount = tryCount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getNextTryTime(long l) {
        return nextTryTime;
    }

    public void setNextTryTime(int nextTryTime) {
        this.nextTryTime = nextTryTime;
    }

    private Date createTime;
    private int nextTryTime;


}
