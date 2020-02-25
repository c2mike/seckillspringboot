package springbootseckill.util;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SerializeHelp {
    private static LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);

    private static Map<Class<?>, Schema<?>> schemaMap = new ConcurrentHashMap<Class<?>,Schema<?>>();

    private static <T> Schema<T> getSchema(Class<T> clazz)
    {
        Schema<T> schema = (Schema<T>) schemaMap.get(clazz);
        if(schema==null)
        {
            schema = RuntimeSchema.getSchema(clazz);
            schemaMap.putIfAbsent(clazz,schema);
        }
        return schema;
    }

    public static <T> byte[] serialize(T obj)
    {
        Class<T> clazz = (Class<T>) obj.getClass();
        Schema<T> schema = getSchema(clazz);
        byte[] data;
        try
        {
            data = ProtostuffIOUtil.toByteArray(obj,schema,buffer);
        }
        finally{
            buffer.clear();
        }
        return data;
    }

    public static <T> T deserialize(byte[] data,Class<T> clazz)
    {
        Schema<T> schema = getSchema(clazz);
        T obj = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(data,obj,schema);
        return obj;
    }

    Logger logger = LoggerFactory.getLogger(getClass());
    public void test()
    {
        logger.debug("asd");
    }

}
