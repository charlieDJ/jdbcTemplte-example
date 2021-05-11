package com.example.template;

import com.example.template.model.Person;
import com.example.template.service.RedisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

/**
 * @author dj
 * @date 2021/5/10
 */
public class RedisTest extends TemplateApplicationTests {

    @Autowired
    private RedisService redisService;

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



}
