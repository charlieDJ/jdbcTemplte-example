package com.example.template.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The interface Base redis service.
 *
 * @author dj
 * @date 2021/05/14
 */
public interface BaseRedisService {

    /**
     * 保存元素，并设置过期时间
     *
     * @param key   键
     * @param value 值
     * @param time  距离过期还剩多少时间，单位为秒
     */
    void set(String key, Object value, long time);

    /**
     * 保存元素
     *
     * @param key   键
     * @param value 值
     */
    void set(String key, Object value);

    /**
     * 获取元素
     *
     * @param key 键
     * @return 返回元素
     */
    Object get(String key);

    /**
     * 删除元素
     *
     * @param key 键
     * @return 是否成功
     */
    Boolean del(String... key);

    /**
     * 设置过期时间
     *
     * @param key  键
     * @param time 距离过期还剩多少时间，单位为秒
     * @return 是否成功
     */
    Boolean expire(String key, long time);

    /**
     * 获取过期时间
     *
     * @param key 键
     * @return 过期时间
     */
    Long getExpire(String key);

    /**
     * 判断Hash结构中是否有该元素
     *
     * @param key 键
     * @return 是否包含
     */
    Boolean hasKey(String key);

    /**
     * 按delta递增
     *
     * @param key   键
     * @param delta 递增因子
     * @return 递增后的数字
     */
    Long incr(String key, long delta);

    /**
     * 按delta递减
     *
     * @param key   键
     * @param delta 递减因子
     * @return 递减后的数字
     */
    Long decr(String key, long delta);

    /**
     * 获取Hash结构中的元素
     *
     * @param key     键
     * @param hashKey hash键
     * @return 返回元素
     */
    Object hGet(String key, String hashKey);

    /**
     * 向Hash结构中存放一个元素，并设置过期时间
     *
     * @param key     键
     * @param hashKey hash键
     * @param value   值
     * @param time    距离过期还剩多少时间，单位为秒
     * @return 是否成功
     */
    Boolean hSet(String key, String hashKey, Object value, long time);

    /**
     * 向Hash结构中放入一个元素
     *
     * @param key     键
     * @param hashKey hash键
     * @param value   值
     */
    void hSet(String key, String hashKey, Object value);

    /**
     * 直接获取整个Hash结构
     *
     * @param key 键
     * @return Map
     */
    Map<Object, Object> hGetAll(String key);

    /**
     * 设置整个Hash结构，并设置过期时间
     *
     * @param key  键
     * @param map  the map
     * @param time 距离过期还剩多少时间，单位为秒
     * @return 是否成功
     */
    Boolean hSetAll(String key, Map<String, Object> map, long time);

    /**
     * 设置整个Hash结构
     *
     * @param key 键
     * @param map the map
     */
    void hSetAll(String key, Map<String, Object> map);

    /**
     * 删除Hash结构中的属性
     *
     * @param key     键
     * @param hashKey hash键
     */
    void hDel(String key, Object... hashKey);

    /**
     * 判断Hash结构中是否有该属性
     *
     * @param key     键
     * @param hashKey hash键
     * @return 是否成功
     */
    Boolean hHasKey(String key, String hashKey);

    /**
     * Hash结构中元素递增
     *
     * @param key     键
     * @param hashKey hash键
     * @param delta   递增或递减因子
     * @return 递增或递减后的数字
     */
    Long hIncr(String key, String hashKey, Long delta);

    /**
     * Hash结构中元素递减
     *
     * @param key     键
     * @param hashKey hash键
     * @param delta   递增或递减因子
     * @return 递增或递减后的数字
     */
    Long hDecr(String key, String hashKey, Long delta);

    /**
     * 与key对应的set集合
     *
     * @param key 键
     * @return 元素集合
     */
    Set<Object> sMembers(String key);

    /**
     * 向Set集合添加元素
     *
     * @param key    键
     * @param values 值
     * @return 递增或递减后的数字
     */
    Long sAdd(String key, Object... values);

    /**
     * 向Set集合添加元素
     *
     * @param key    键
     * @param time   距离过期还剩多少时间，单位为秒
     * @param values 值s
     * @return 加入了多少个元素
     */
    Long sAdd(String key, long time, Object... values);

    /**
     * 是否为Set中的元素
     *
     * @param key   键
     * @param value 值
     * @return 是否包含
     */
    Boolean sIsMember(String key, Object value);

    /**
     * 获取Set集合的长度
     *
     * @param key 键
     * @return 集合长度
     */
    Long sSize(String key);

    /**
     * 删除Set集合中的元素
     *
     * @param key    键
     * @param values 值s
     * @return 删除个数
     */
    Long sRemove(String key, Object... values);

    /**
     * 获取List列表区间长度
     *
     * @param key   键
     * @param start 开始索引
     * @param end   结束索引
     * @return 元素列表
     */
    List<Object> lRange(String key, long start, long end);

    /**
     * 获取List列表长度
     *
     * @param key 键
     * @return 列表长度
     */
    Long lSize(String key);

    /**
     * 根据索引获取List列表元素
     *
     * @param key   键
     * @param index 索引
     * @return 元素
     */
    Object lIndex(String key, long index);

    /**
     * 向List列表中添加元素
     *
     * @param key   键
     * @param value 值
     * @return 添加元素的个数
     */
    Long lPush(String key, Object value);

    /**
     * 向List列表中添加元素，并设置过期时间
     *
     * @param key   键
     * @param value 值
     * @param time  距离过期还剩多少时间，单位为秒
     * @return 添加元素的个数
     */
    Long lPush(String key, Object value, long time);

    /**
     * 向List列表中批量添加元素
     *
     * @param key    键
     * @param values 值
     * @return 添加元素的个数
     */
    Long lPushAll(String key, Object... values);

    /**
     * 向List列表中批量添加元素
     *
     * @param key    键
     * @param time   距离过期还剩多少时间，单位为秒
     * @param values 值
     * @return 添加元素的个数
     */
    Long lPushAll(String key, Long time, Object... values);

    /**
     * 从List列表中移除元素
     *
     * @param key   键
     * @param count 移除个数
     * @param value 值
     * @return 删除元素的个数
     */
    Long lRemove(String key, long count, Object value);
}
