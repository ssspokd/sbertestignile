package ru.ssspokd.apacheignite.config;

import org.apache.ignite.cache.*;
import org.apache.ignite.cache.store.CacheStoreSessionListener;
import org.apache.ignite.cache.store.hibernate.CacheHibernateStoreSessionListener;
import org.apache.ignite.cache.store.jdbc.CacheJdbcPojoStore;
import org.apache.ignite.cache.store.jdbc.CacheJdbcPojoStoreFactory;
import org.apache.ignite.configuration.CacheConfiguration;
import org.h2.jdbcx.JdbcConnectionPool;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;
import ru.ssspokd.apacheignite.model.Payment;
import ru.ssspokd.apacheignite.store.CacheStore;

import javax.cache.configuration.Factory;
import javax.cache.configuration.FactoryBuilder;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;

@Repository
@Transactional
public class CacheConfig implements Serializable {

    private static final String HIBERNATE_CFG = "src\\main\\resources\\hibernate.cfg.xml";
    private String nameCache;
    private int COUNT_BACKUPS = 10;
    private int WRITE_BEHID_FLUSH_SIZE=1024;
    private int WRITE_BEHID_FLUSH_FREQUENCY=5000;
    private int WRITE_BEHID_BATCH_SIZE=10;

    public CacheConfiguration createCache(String nameCache){
        this.nameCache = nameCache;
        return  cacheConfiguration();
    }
    private static final class CacheJdbcPojoStoreExampleFactory extends CacheJdbcPojoStoreFactory<Long, Payment> {
        /** {@inheritDoc} */
        @Override public CacheJdbcPojoStore<Long, Payment> create() {
            setDataSource(JdbcConnectionPool.create("jdbc:postgresql://localhost:5432/test", "postgres", "password"));

            return super.create();
        }
    }

    @Bean
    private CacheConfiguration cacheConfiguration(){
        CacheConfiguration<Long, Payment> cacheCfg = new CacheConfiguration(nameCache);
        cacheCfg.setName(nameCache);
        cacheCfg.setCacheMode(CacheMode.REPLICATED);
        cacheCfg.setAtomicityMode(CacheAtomicityMode.ATOMIC);
        cacheCfg.setBackups(COUNT_BACKUPS);
        cacheCfg.setIndexedTypes(Long.class, Payment.class);
        cacheCfg.setOnheapCacheEnabled(true);
        cacheCfg.setReadThrough(true);
        cacheCfg.setWriteThrough(true);
        cacheCfg.setStatisticsEnabled(true);
        cacheCfg.setWriteSynchronizationMode(CacheWriteSynchronizationMode.FULL_SYNC);
        cacheCfg.setWriteBehindEnabled(true);
        cacheCfg.setWriteBehindFlushSize(WRITE_BEHID_FLUSH_SIZE);
        cacheCfg.setWriteBehindFlushFrequency(WRITE_BEHID_FLUSH_FREQUENCY);
        cacheCfg.setWriteBehindBatchSize(WRITE_BEHID_BATCH_SIZE);
        cacheCfg.setCacheStoreSessionListenerFactories((Factory<CacheStoreSessionListener>) () -> {
            CacheHibernateStoreSessionListener lsnr = new CacheHibernateStoreSessionListener();
            lsnr.setHibernateConfigurationPath(HIBERNATE_CFG);
            return lsnr;
        });
        cacheCfg.setCacheStoreFactory(FactoryBuilder.factoryOf(CacheStore.class));
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
