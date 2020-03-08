package springbootseckill.enumfinal;

public enum QueueName {
    Seckill_Queue(1,"seckillQueue"),
    Exchange_Name(2,"seckillExchange"),
    Seckill_RouteKey(3,"seckillRouteKey.#");


    private int index;
    final private String name;

    QueueName(int index, String name) {
        this.index = index;
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }
}
