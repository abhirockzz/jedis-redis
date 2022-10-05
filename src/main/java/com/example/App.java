package com.example;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.DefaultJedisClientConfig;
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

        Map<String, String> envVars = System.getenv();

        String endpointURL = envVars.get("MEMORYDB_ENDPOINT_URL");
        if (endpointURL == null) {
            throw new RuntimeException("Missing environment variable MEMORYDB_ENDPOINT_URL");
        }
        
        System.out.println("MemoryDB endpoint URL - " + endpointURL);

        String username = envVars.get("MEMORYDB_USERNAME");
        if (username == null) {
            throw new RuntimeException("Missing environment variable MEMORYDB_USERNAME");
        }
        String password = envVars.get("MEMORYDB_PASSWORD");
        if (password == null) {
            throw new RuntimeException("Missing environment variable MEMORYDB_PASSWORD");
        }
    
        Set<HostAndPort> jedisClusterNodes = new HashSet<>();
        //"clustercfg.tutorial-getting-started-redis.smtjf4.memorydb.us-east-1.amazonaws.com"
        String host = endpointURL.split(":")[0];
        Integer port = Integer.valueOf(endpointURL.split(":")[1]);

        jedisClusterNodes.add(new HostAndPort(host, port));

        JedisCluster jedis = new JedisCluster(jedisClusterNodes,DefaultJedisClientConfig.builder().ssl(true).user(username).password(password).build());

        String result = jedis.set("foo", "bar");
        System.out.println("set result"+ result);

        System.out.println("foo=="+jedis.get("foo"));
    }
}
