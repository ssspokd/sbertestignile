package ru.ssspokd.apacheignite.config;

import org.apache.ignite.cache.store.hibernate.CacheHibernateBlobStoreFactory;
import org.apache.ignite.configuration.*;
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
    private static final String HIBERNATE_CFG = "src\\main\\resources\\hibernate.cfg.xml";

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
        cfg.setConnectorConfiguration(connectorConfiguration());
        return cfg;
    }

    private ConnectorConfiguration connectorConfiguration(){
        ConnectorConfiguration connectorConfiguration=new ConnectorConfiguration();
        connectorConfiguration.setPort(6379);
        connectorConfiguration.setHost("127.0.0.1");
        return connectorConfiguration;
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
        transactionConfiguration.setUseJtaSynchronization(true);
        CacheHibernateBlobStoreFactory cacheHibernateBlobStoreFactory = new CacheHibernateBlobStoreFactory<>();
        cacheHibernateBlobStoreFactory.setHibernateConfigurationPath(HIBERNATE_CFG);
        cacheHibernateBlobStoreFactory.create();
        transactionConfiguration.setTxManagerFactory(cacheHibernateBlobStoreFactory);
        return  transactionConfiguration;
    }




}
