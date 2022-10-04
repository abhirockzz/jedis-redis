package com.example;

import java.util.HashSet;
import java.util.Set;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        System.out.println("Hello World!!!!");
        Set<HostAndPort> jedisClusterNodes = new HashSet<>();
        
        jedisClusterNodes.add(new HostAndPort("clustercfg.tutorial-getting-started-redis.smtjf4.memorydb.us-east-1.amazonaws.com", 6379));

        JedisCluster jedis = new JedisCluster(jedisClusterNodes);

        String result = jedis.set("foo", "bar");
        System.out.println("set result"+ result);

        System.out.println("foo=="+jedis.get("foo"));
        
    }
}
