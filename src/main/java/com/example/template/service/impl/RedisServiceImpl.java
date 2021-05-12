package com.example.template.service.impl;

import com.example.template.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author dj
 * @date 2021/5/10
 */
@Slf4j
@Service
public class RedisServiceImpl implements RedisService {

    @Resource(name = "myRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;
    // inject the template as ListOperations
    @Resource(name = "myRedisTemplate")
    private ListOperations<String, Object> listOpts;
    @Resource(name = "myRedisTemplate")
    private ValueOperations<String, Object> valueOpts;
    @Resource(name = "myRedisTemplate")
    HashOperations<String, Object, Object> hashOpts;
    @Resource(name = "myRedisTemplate")
    SetOperations<String, Object> setOpts;

    @Override
    public void set(String key, Object value, long time) {
        if (time > 0) {
            valueOpts.set(key, value, time, TimeUnit.SECONDS);
        } else {
            valueOpts.set(key, value);
        }
    }

    @Override
    public void set(String key, Object value) {
        valueOpts.set(key, value);
    }

    @Override
    public Object get(String key) {
        return key == null ? null : valueOpts.get(key);
    }

    @Override
    public Boolean del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(Arrays.asList(key));
            }
        }
        return false;
    }

    @Override
    public Boolean expire(String key, long time) {
        if (time < 0) {
            return true;
        }
        return redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }

    @Override
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    @Override
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public Long incr(String key, long delta) {
        validateDelta(delta);
        return valueOpts.increment(key, delta);
    }

    private void validateDelta(long delta) {
        Assert.state(delta > 0, "因子必须大于0");
    }

    @Override
    public Long decr(String key, long delta) {
        validateDelta(delta);
        return valueOpts.increment(key, -delta);
    }

    @Override
    public Object hGet(String key, String hashKey) {
        return hashOpts.get(key, hashKey);
    }

    @Override
    public Boolean hSet(String key, String hashKey, Object value, long time) {
        hashOpts.put(key, hashKey, value);
        return expire(key, time);
    }

    @Override
    public void hSet(String key, String hashKey, Object value) {
        hashOpts.put(key, hashKey, value);
    }

    @Override
    public Map<Object, Object> hGetAll(String key) {
        return hashOpts.entries(key);
    }

    @Override
    public Boolean hSetAll(String key, Map<String, Object> map, long time) {
        hashOpts.putAll(key, map);
        return expire(key, time);
    }

    @Override
    public void hSetAll(String key, Map<String, Object> map) {
        hashOpts.putAll(key, map);
    }

    @Override
    public void hDel(String key, Object... hashKey) {
        hashOpts.delete(key, hashKey);
    }

    @Override
    public Boolean hHasKey(String key, String hashKey) {
        return hashOpts.hasKey(key, hashKey);
    }

    @Override
    public Long hIncr(String key, String hashKey, Long delta) {
        return hashOpts.increment(key, hashKey, delta);
    }

    @Override
    public Long hDecr(String key, String hashKey, Long delta) {
        return hashOpts.increment(key, hashKey, -delta);
    }

    @Override
    public Set<Object> sMembers(String key) {
        return setOpts.members(key);
    }

    @Override
    public Long sAdd(String key, Object... values) {
        return setOpts.add(key, values);
    }

    @Override
    public Long sAdd(String key, long time, Object... values) {
        Long count = setOpts.add(key, values);
        expire(key, time);
        return count;
    }

    @Override
    public Boolean sIsMember(String key, Object value) {
        return setOpts.isMember(key, value);
    }

    @Override
    public Long sSize(String key) {
        return setOpts.size(key);
    }

    @Override
    public Long sRemove(String key, Object... values) {
        return setOpts.remove(key, values);
    }

    @Override
    public List<Object> lRange(String key, long start, long end) {
        return listOpts.range(key, start, end);
    }

    @Override
    public Long lSize(String key) {
        return listOpts.size(key);
    }

    @Override
    public Object lIndex(String key, long index) {
        return listOpts.index(key, index);
    }

    @Override
    public Long lPush(String key, Object value) {
        return listOpts.rightPush(key, value);
    }

    @Override
    public Long lPush(String key, Object value, long time) {
        Long index = listOpts.rightPush(key, value);
        expire(key, time);
        return index;
    }

    @Override
    public Long lPushAll(String key, Object... values) {
        return listOpts.rightPushAll(key, values);
    }

    @Override
    public Long lPushAll(String key, Long time, Object... values) {
        Long count = listOpts.rightPushAll(key, values);
        expire(key, time);
        return count;
    }

    @Override
    public Long lRemove(String key, long count, Object value) {
        return listOpts.remove(key, count, value);
    }


}
