package ru.ssspokd.apacheignite.config;

import org.apache.ignite.configuration.ClientConnectorConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.configuration.MemoryConfiguration;
import org.apache.ignite.configuration.TransactionConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;

@Repository
public class IgniteConfig {

    @Autowired
    CacheConfig cacheConfiguration;
    @Autowired
    DataStorageConfig dataStorageConfiguration;
    @Autowired
    TcpDiscoveryConfig discoverySpi;

    private String nameInstances,  nameCache;


    public IgniteConfig(String nameInstances, String nameCache) {
        this.nameInstances = nameInstances;
        this.nameCache = nameCache;
        cacheConfiguration  =  new CacheConfig();
        dataStorageConfiguration =  new DataStorageConfig();
        discoverySpi = new TcpDiscoveryConfig();
    }

    @Bean(name = "igniteConfiguration")
    public IgniteConfiguration igniteConfiguration(){
        IgniteConfiguration cfg = new IgniteConfiguration();
        cfg.setIgniteInstanceName(nameInstances);
        cfg.setPeerClassLoadingEnabled(true);
        cfg.setDataStorageConfiguration(dataStorageConfiguration.dataStorageConfiguration());
        cfg.setCacheConfiguration(cacheConfiguration.createCache(nameCache));
        cfg.setTransactionConfiguration(transactionalConfiguration());
        cfg.setDiscoverySpi(discoverySpi.discoverySpi());
        cfg.setClientConnectorConfiguration(clientConnectConfig());
        //cfg.setMemoryConfiguration(memoryConfiguration());
        return cfg;
    }

    private MemoryConfiguration memoryConfiguration(){
        MemoryConfiguration memoryConfiguration = new MemoryConfiguration();
        memoryConfiguration.setPageSize(4096);
        memoryConfiguration.setSystemCacheInitialSize(40 * 1024 * 1024);
        memoryConfiguration.setSystemCacheMaxSize(40 * 1024 * 1024);

        return memoryConfiguration;
    }

    private ClientConnectorConfiguration clientConnectConfig() {
        ClientConnectorConfiguration clientConnectorConfiguration = new ClientConnectorConfiguration();
        clientConnectorConfiguration.setHost("127.0.0.1");
        clientConnectorConfiguration.setPort(11211);
        return  clientConnectorConfiguration;
    }

    private TransactionConfiguration transactionalConfiguration() {
        TransactionConfiguration transactionConfiguration  = new TransactionConfiguration();
        return  transactionConfiguration;
    }




}
