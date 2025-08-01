package com.yww.busuanzi.redis;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.filter.Filter;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * <p>
 *      Redis的FastJson2序列化工具类
 * </p>
 *
 * @author yww
 * @since 2025/7/22
 */
public class FastJson2JsonRedisSerializer<T> implements RedisSerializer<T> {

    /**
     * 默认字符集
     */
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    /**
     * 自动识别json对象白名单配置（仅允许解析的包名，范围越小越安全）
     */
    private static final Filter AUTO_TYPE_FILTER = JSONReader.autoTypeFilter("org.springframework", "com.ruoyi");

    private final  Class<T> clazz;

    public FastJson2JsonRedisSerializer(Class<T> clazz) {
        super();
        this.clazz = clazz;
    }

    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (t == null) {
            return new byte[0];
        }
        return JSON.toJSONString(t, JSONWriter.Feature.WriteClassName).getBytes(DEFAULT_CHARSET);
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        String str = new String(bytes, DEFAULT_CHARSET);
        return JSON.parseObject(str, clazz, AUTO_TYPE_FILTER);
    }

}
