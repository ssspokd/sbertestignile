package ru.ssspokd.apacheignite.config;

import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;
import ru.ssspokd.apacheignite.model.Payment;

@Repository
public class CacheConfig {

    @Bean
    private CacheConfiguration cacheConfiguration(String nameCache){
        CacheConfiguration cacheCfg = new CacheConfiguration(nameCache);
        cacheCfg.setName(nameCache);
        cacheCfg.setCacheMode(CacheMode.PARTITIONED);
        cacheCfg.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);
        cacheCfg.setBackups(10);
        cacheCfg.setIndexedTypes(Long.class, Payment.class);
        return  cacheCfg;
    }

    public CacheConfiguration createCache(String nameCache){
        return  cacheConfiguration(nameCache);
    }
}
