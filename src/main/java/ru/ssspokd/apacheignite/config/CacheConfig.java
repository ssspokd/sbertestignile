package ru.ssspokd.apacheignite.config;

import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.QueryEntity;
import org.apache.ignite.cache.QueryIndex;
import org.apache.ignite.cache.store.CacheStoreSessionListener;
import org.apache.ignite.cache.store.hibernate.CacheHibernateStoreSessionListener;
import org.apache.ignite.configuration.CacheConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;
import ru.ssspokd.apacheignite.model.Payment;

import javax.cache.configuration.Factory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;

@Repository
public class CacheConfig {

    private static final String HIBERNATE_CFG = "src\\main\\resources\\hibernate.cfg.xml";
    private String nameCache;

    public CacheConfiguration createCache(String nameCache){
        this.nameCache = nameCache;
        return  cacheConfiguration();
    }

    @Bean
    private CacheConfiguration cacheConfiguration(){
        CacheConfiguration cacheCfg = new CacheConfiguration(nameCache);
        cacheCfg.setName(nameCache);
        cacheCfg.setCacheMode(CacheMode.PARTITIONED);
        cacheCfg.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);
        cacheCfg.setBackups(10);
        cacheCfg.setIndexedTypes(Long.class, Payment.class);
        cacheCfg.setOnheapCacheEnabled(true);
        cacheCfg.setReadThrough(false);
        cacheCfg.setWriteThrough(false);
        cacheCfg.setStatisticsEnabled(true);
        cacheCfg.setCacheStoreSessionListenerFactories((Factory<CacheStoreSessionListener>) () -> {
            CacheHibernateStoreSessionListener lsnr = new CacheHibernateStoreSessionListener();
            lsnr.setHibernateConfigurationPath(HIBERNATE_CFG);
            return lsnr;
        });
        cacheCfg.setQueryEntities(Arrays.asList(setQueryEntity()));
        return  cacheCfg;

    }



    private QueryEntity setQueryEntity(){
        QueryEntity queryEntity = new QueryEntity();
        queryEntity.setKeyType(Long.class.getName());
        queryEntity.setValueType(Payment.class.getName());
        LinkedHashMap<String, String> fields = new LinkedHashMap();
        fields.put("id", Long.class.getName());
        fields.put("accountUser", Long.class.getName());
        fields.put("accountBalance", String.class.getName());
        fields.put("lastOperationDate", String.class.getName());
        fields.put("enumOperation", String.class.getName());
        queryEntity.setFields(fields);
        Collection<QueryIndex> indexes = new ArrayList<>(3);
        indexes.add(new QueryIndex("id"));
        indexes.add(new QueryIndex("namePayment"));
        indexes.add(new QueryIndex("lastOperationDate"));
        queryEntity.setIndexes(indexes);
        return queryEntity;
    }

}
