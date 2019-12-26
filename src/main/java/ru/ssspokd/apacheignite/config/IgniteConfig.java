package ru.ssspokd.apacheignite.config;

import org.apache.ignite.IgniteCheckedException;
import org.apache.ignite.IgniteLogger;
import org.apache.ignite.cache.store.hibernate.CacheHibernateBlobStoreFactory;
import org.apache.ignite.configuration.ClientConnectorConfiguration;
import org.apache.ignite.configuration.ConnectorConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.configuration.TransactionConfiguration;
import org.apache.ignite.logger.log4j.Log4JLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IgniteConfig {

    @Autowired
    private CacheConfig cacheConfiguration;
    @Autowired
    private DataStorageConfig dataStorageConfiguration;
    @Autowired
    private TcpDiscoveryConfig discoverySpi;
    private static final String HIBERNATE_CFG = "src\\main\\resources\\hibernate.cfg.xml";
    private String nameInstances;
    private String nameCache;

    public IgniteConfig(String nameInstances, String nameCache) {
        this.nameInstances = nameInstances;
        this.nameCache = nameCache;
        cacheConfiguration  =  new CacheConfig();
        dataStorageConfiguration =  new DataStorageConfig();
        discoverySpi = new TcpDiscoveryConfig();
    }

    public IgniteConfig() {
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
        cfg.setGridLogger(igniteLogger());
        return cfg;
    }

    private IgniteLogger igniteLogger(){
        IgniteLogger log = null;
        try {
            log = new Log4JLogger("src\\main\\resources\\log4j.xml");
        } catch (IgniteCheckedException e) {
            e.printStackTrace();
        }
        return log;
    }

    private ConnectorConfiguration connectorConfiguration(){
        ConnectorConfiguration connectorConfiguration = new ConnectorConfiguration();
        connectorConfiguration.setPort(6379);
        connectorConfiguration.setHost("127.0.0.1");
        return connectorConfiguration;
    }

    private ClientConnectorConfiguration clientConnectConfig() {
        ClientConnectorConfiguration clientConnectorConfiguration = new ClientConnectorConfiguration();
        clientConnectorConfiguration.setHost("127.0.0.1");
        clientConnectorConfiguration.setPort(11211);
        return clientConnectorConfiguration;
    }

    private TransactionConfiguration transactionalConfiguration() {
        TransactionConfiguration transactionConfiguration = new TransactionConfiguration();
        transactionConfiguration.setUseJtaSynchronization(true);
        CacheHibernateBlobStoreFactory cacheHibernateBlobStoreFactory = new CacheHibernateBlobStoreFactory<>();
        cacheHibernateBlobStoreFactory.setHibernateConfigurationPath(HIBERNATE_CFG);
        cacheHibernateBlobStoreFactory.create();
        transactionConfiguration.setTxManagerFactory(cacheHibernateBlobStoreFactory);
        return transactionConfiguration;
    }




}
