package com.example;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.resps.Tuple;

public class MemoryDBSampleApp {

    static JedisCluster jedis = null;
    public static void main(String[] args) {

        Map<String, String> envVars = System.getenv();

        String endpointURL = envVars.get("MEMORYDB_ENDPOINT_URL");
        if (endpointURL == null || endpointURL.isBlank()) {
            throw new RuntimeException("Missing environment variable MEMORYDB_ENDPOINT_URL");
        }
        
        System.out.println("MemoryDB endpoint URL - " + endpointURL);

        String username = envVars.get("MEMORYDB_USERNAME");
        if (username == null || username.isBlank()) {
            throw new RuntimeException("Missing environment variable MEMORYDB_USERNAME");
        }
        String password = envVars.get("MEMORYDB_PASSWORD");
        if (password == null || password.isBlank()) {
            throw new RuntimeException("Missing environment variable MEMORYDB_PASSWORD");
        }
    
        Set<HostAndPort> jedisClusterNodes = new HashSet<>();
        String host = endpointURL.split(":")[0];
        Integer port = Integer.valueOf(endpointURL.split(":")[1]);

        jedisClusterNodes.add(new HostAndPort(host, port));

        jedis = new JedisCluster(jedisClusterNodes,DefaultJedisClientConfig.builder().ssl(true).user(username).password(password).build());

       setAndGet();
       leaderboard(5);

       jedis.close();
    }
    public static void setAndGet() {
        jedis.set("foo", "bar");
        System.out.println("foo="+jedis.get("foo"));
    }

    static String topPlayersSortedSet = "players-leaderboard";

    public static void leaderboard(int topN) {

        System.out.println("adding player scores");

        for(int i = 1; i <= 20; i++) {
            jedis.zadd(topPlayersSortedSet, Math.random()*10, "player-"+i);
        }

        System.out.println("fetching top "+ topN+" players!");

        List<Tuple> topPlayers = jedis.zrevrangeWithScores(topPlayersSortedSet, 0, (topN-1));
        for(Tuple topPlayer: topPlayers){
            System.out.println(topPlayer);
        }
    }

}

