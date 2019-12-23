import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class testRedisConnect {

    @Test
    public void testConnection(){
        JedisPoolConfig jedisPoolCfg = new JedisPoolConfig();
        // your pool configurations.
        JedisPool pool = new JedisPool(jedisPoolCfg, "localhost", 6379, 10000);
        try (Jedis jedis = pool.getResource()) {
            jedis.set("key1", "1");
            System.out.println("Value for 'key1': " + jedis.get("key1"));
        }
        pool.destroy();
    }
}
