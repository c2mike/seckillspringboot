package springbootseckill.enumfinal;

public enum ErrorMessage {
    INVAILD_ARGS("秒杀商品的id错误"),
    Time_Error("秒杀商品未开启或者已结束"),
    Repeated_Seckill("商品重复秒杀"),
    Del_Quantity_Error("减少库存错误"),
    Remain_Over("秒杀完了"),
    DATA_UNSAFE("数据异常"),
    WAIT_IN_QUEUE("秒杀中。。。"),
    SUCCESS_ENQUEUE("提交成功");
    private String message;

    public String getMessage() {
        return message;
    }

    ErrorMessage(String message) {
        this.message = message;
    }
}
