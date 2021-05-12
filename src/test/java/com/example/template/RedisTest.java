package com.example.template;

import com.example.template.model.Person;
import com.example.template.service.RedisService;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author dj
 * @date 2021/5/10
 */
public class RedisTest extends TemplateApplicationTests {

    @Autowired
    private RedisService redisService;
    @Autowired
    private RedissonClient redissonClient;

    @Test
    public void set(){
        redisService.set("1","2");
        System.out.println(redisService.get("1"));
    }

    @Test
    public void get(){
        Person person = (Person) redisService.get("person");
        System.out.println(person);
    }

    @Test
    public void write(){
        Person person = new Person();
        person.setFirstname("yang");
        redisService.set("person", person);
    }

    @Test
    public void hSet(){
        Person person = new Person();
        person.setFirstname("yang");
        person.setLastname("chao");
        Person person2 = new Person();
        person2.setFirstname("zhao");
        person2.setLastname("se");
        redisService.hSet("hash", "pseron", person);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("1", person);
        hashMap.put("2", person2);
        redisService.hSetAll("hashAll", hashMap);
    }

    @Test
    public void redissonGet(){
        RLock lock = redissonClient.getLock("lock");
        lock.lock(30, TimeUnit.SECONDS);
    }

    @Test
    public void setNull(){
        redisService.set(null,"1");
    }

    @Test
    public void setNullValue(){
        redisService.set("1",null);
    }

    @Test
    public void setHashNull(){
        redisService.hSet("h:",null,"武威");
    }



}
