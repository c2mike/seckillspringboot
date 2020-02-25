package springbootseckill.enumfinal;

public enum QueueName {
    Seckill_Queue(1,"seckillQueue");


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
