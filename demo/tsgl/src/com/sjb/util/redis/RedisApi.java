package com.sjb.util.redis;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

public class RedisApi {
    
    private static JedisPool pool;
    
    private static Properties prop = null;
    
    private static JedisPoolConfig config = null;
    
    static {
    	//注意路径这边斜杠不要写错“/”
//        InputStream in = RedisApi.class.getClassLoader().getResourceAsStream("com/sjb/util/redis/redis.properties");
        InputStream in = RedisApi.class.getClassLoader().getResourceAsStream("redis.properties");
        prop = new Properties();
        try {
            prop.load(in);
            in.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        config = new JedisPoolConfig();
        config.setMaxTotal(Integer.valueOf(prop.getProperty("MAX_TOTAL")));
        config.setMaxIdle(Integer.valueOf(prop.getProperty("MAX_IDLE")));
        
        config.setMaxWaitMillis(Integer.valueOf(prop.getProperty("MAX_WAIT_MILLIS")));
        
        config.setTestOnBorrow(Boolean.valueOf(prop.getProperty("TEST_ON_BORROW")));
        config.setTestOnReturn(Boolean.valueOf(prop.getProperty("TEST_ON_RETURN")));
        config.setTestWhileIdle(Boolean.valueOf(prop.getProperty("TEST_WHILE_IDLE")));
        
        
        //初始化pool
        getPool();
        //清楚所有key
//        pool.getResource().flushAll();
       
    }
    
    public static void createJedisPool(String address) {
    	pool = new JedisPool(config, address.split(":")[0],
                Integer.valueOf(address.split(":")[1]), 100000,"12345678");
    }
    
    public static JedisPool getPool() {
        
        if (pool == null) {
            
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(Integer.valueOf(prop.getProperty("MAX_TOTAL")));
            config.setMaxIdle(Integer.valueOf(prop.getProperty("MAX_IDLE")));
            //链接超时时间
            config.setMaxWaitMillis(Integer.valueOf(prop.getProperty("MAX_WAIT_MILLIS")));
            
            config.setTestOnBorrow(Boolean.valueOf(prop.getProperty("TEST_ON_BORROW")));
            config.setTestOnReturn(Boolean.valueOf(prop.getProperty("TEST_ON_RETURN")));
            config.setTestWhileIdle(Boolean.valueOf(prop.getProperty("TEST_WHILE_IDLE")));
            pool = new JedisPool(config, prop.getProperty("REDIS_IP"),
                    Integer.valueOf(prop.getProperty("REDIS_PORT")), 100000,"12345678");
        }
        
        return pool;
    }
    
    public static void returnResource(JedisPool pool, Jedis redis) {
        if (redis != null) {
            pool.returnResource(redis);
        }
    }
    
    public static void publish(String channel, String msg) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.publish(channel, msg);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            returnResource(pool, jedis);
        }
    }
    
    public static void subsribe(String channel, JedisPubSub ps) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.subscribe(ps, channel);
        }
        catch (Exception e) {
        	e.printStackTrace();
        }
        finally {
            returnResource(pool, jedis);
        }
    }
    
    public static Long hdel(String key, String key1) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hdel(key, key1);
        }
        catch (Exception e) {
        	e.printStackTrace();
        }
        finally {
            returnResource(pool, jedis);
        }
        return null;
    }
    
    /** 
     *  
     * @param key 
     * @return 
     */
    public static String get(String key) {
        Jedis jedis = null;
        String value = null;
        try {
            jedis = pool.getResource();
            value = jedis.get(key);
        }
        catch (Exception e) {
        	e.printStackTrace();
        }
        finally {
            returnResource(pool, jedis);
        }
        return value;
    }
    
    /**
     * 是否存在
     * @param key
     * @return
     */
    public static boolean exists(String key) {
        Jedis jedis = null;
        boolean value = false;
        try {
            jedis = pool.getResource();
            value = jedis.exists(key);
        }
        catch (Exception e) {
        	e.printStackTrace();
        }
        finally {
            returnResource(pool, jedis);
        }
        return value;
    }
    
    public static String set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.set(key, value);
        }
        catch (Exception e) {
        	e.printStackTrace();
            return "0";
        }
        finally {
            returnResource(pool, jedis);
        }
    }
    
    /**
     * 跟新生存时间
     * @param key
     * @param expire
     */
    public static void expired(String key,int expire){
    	 Jedis jedis = null;
         try {
             jedis = pool.getResource();
             jedis.expire(key, expire);
         }
         catch (Exception e) {
        	 e.printStackTrace();
         }
         finally {
             returnResource(pool, jedis);
         }
    }
    
    /**
     * 设置成功，返回 1 。 设置失败，返回 0 。
     * 判断key 是否存在 存在设置失败 不存在设置成功
     * 常用于锁机制
     */
    public static boolean setNX(String key,String value){
   	 Jedis jedis = null;
        try {
            jedis = pool.getResource();
            if(jedis.setnx(key, value).intValue()==1)
            	return true;
            else
            	return false;
        }
        catch (Exception e) {
        	e.printStackTrace();
        }
        finally {
            returnResource(pool, jedis);
        }
		return false;
   }
    
    /**
     * 设置成功，返回 1 。 设置失败，返回 0 。 同时设置了过期时间
     * 判断key 是否存在 存在设置失败 不存在设置成功
     * 常用于锁机制 多了过期时间
     */
    public static boolean setNXEX(String key,String value, int expire){
   	 Jedis jedis = null;
        try {
            jedis = pool.getResource();
            if("OK".equals(jedis.set(key, value,"NX","EX",expire)))
            	return true;
            else
            	return false;
        }
        catch (Exception e) {
        	e.printStackTrace();
        }
        finally {
            returnResource(pool, jedis);
        }
		return false;
   }
    
    /**
     * 如果key存在覆盖旧值 跟新过期时间
     * @param key
     * @param value
     * @param expire
     * @return
     */
    public static String setEX(String key, String value, int expire) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.setex(key, expire, value);
            return "1";
        }
        catch (Exception e) {
        	e.printStackTrace();
            return "0";
        }
        finally {
            returnResource(pool, jedis);
        }
    }
    
    public static Long del(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.del(key);
        }
        catch (Exception e) {
        	e.printStackTrace();
            return null;
        }
        finally {
            returnResource(pool, jedis);
        }
    }
    
    /** 
     * @Description 操作list类型数据的 
     * @param @param key
     * @param @param strings  list.add(object)
     * @param @return 参数 
     * @return Long 返回类型  
     * @throws 
     */
    
    public static Long lpush(String key, String... strings) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lpush(key, strings);
        }
        catch (Exception e) {
        	e.printStackTrace();
            return 0L;
        }
        finally {
            returnResource(pool, jedis);
        }
    }
    
    /** 
     * @Description 操作list类型数据的 
     * @param @param key
     * @param @param strings  list.add(object)
     * @param @return 参数 
     * @return Long 返回类型  
     * @throws 
     */
    
    public static Long rpush(String key, String... strings) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.rpush(key, strings);
        }
        catch (Exception e) {
        	e.printStackTrace();
            return 0L;
        }
        finally {
            returnResource(pool, jedis);
        }
    }
    
    /**
     * 删除key中的第一元素并返回该元素
     * 操作类型 list
     * @param key
     * @return
     */
    public static String lpop (String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lpop (key);
        }
        catch (Exception e) {
        	e.printStackTrace();
            return null;
        }
        finally {
            returnResource(pool, jedis);
        }
    }
    
    /**
     * 删除key中的最后元素并返回该元素
     * @param key
     * @return
     */
    public static String rpop(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.rpop (key);
        }
        catch (Exception e) {
        	e.printStackTrace();
            return null;
        }
        finally {
            returnResource(pool, jedis);
        }
    }
    
    
    public static List<String> lrange(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lrange(key, 0, -1);
        }
        catch (Exception e) {
        	e.printStackTrace();
            return null;
        }
        finally {
            returnResource(pool, jedis);
        }
    }
    
    public static String hmset(String key, Map map) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hmset(key, map);
        }
        catch (Exception e) {
        	e.printStackTrace();
            return "0";
        }
        finally {
            returnResource(pool, jedis);
        }
    }
    
    /**
     * LINDEX key index
     * 返回列表 key 中，下标为 index 的元素。
     * 下标(index)参数 start 和 stop 都以 0 为底，也就是说，以 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推。
     * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
     * 如果 key 不是列表类型，返回一个错误。
     * @return 
     */
    public static String lindex(String key,int i) {
    	 Jedis jedis = null;
         try {
             jedis = pool.getResource();
             return jedis.lindex(key, i);
         }
         catch (Exception e) {
        	 e.printStackTrace();
         }
         finally {
             returnResource(pool, jedis);
         }
         return null;
    }
    
    /**
     * LSET key index value
     * 将列表 key 下标为 index 的元素的值设置为 value 。
     * 当 index 参数超出范围，或对一个空列表( key 不存在)进行 LSET 时，返回一个错误。
     * 关于列表下标的更多信息，请参考 LINDEX 命令。
     */
    public static String lset(String key,int i,String value) {
    	 Jedis jedis = null;
         try {
             jedis = pool.getResource();
             return jedis.lset(key, i, value);
         }
         catch (Exception e) {
        	 e.printStackTrace();
         }
         finally {
             returnResource(pool, jedis);
         }
         return null;
        
    }
    

    public static List<String> hvals(String key) {
    	 Jedis jedis = null;
    	 List<String> li = null;
         try {
             jedis = pool.getResource();
             li = jedis.hvals(key);
         }
         catch (Exception e) {
        	 e.printStackTrace();
         }
         finally {
             returnResource(pool, jedis);
         }
         return li;
        
    }
    
    public static String hget(String key,String value) {
   	 Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hget(key,value);
        }
        catch (Exception e) {
        	e.printStackTrace();
        }
        finally {
            returnResource(pool, jedis);
        }
        return null;
   }
    
	public static Long hset(String set, String key, String value) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.hset(set,key, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return null;
	}
	
	public static Long hdel(String key) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.hdel(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(pool, jedis);
		}
		return null;
	}
	
    public static List<String> hmget(String key, String... strings) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hmget(key, strings);
        }
        catch (Exception e) {
        	e.printStackTrace();
        }
        finally {
            returnResource(pool, jedis);
        }
        return null;
    }
}
