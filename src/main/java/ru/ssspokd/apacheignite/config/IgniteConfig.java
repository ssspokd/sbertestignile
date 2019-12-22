package ru.ssspokd.apacheignite.config;

import org.apache.ignite.configuration.ClientConnectorConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
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

    public IgniteConfig() {
        cacheConfiguration  =  new CacheConfig();
        dataStorageConfiguration =  new DataStorageConfig();
        discoverySpi = new TcpDiscoveryConfig();
    }

    @Bean
    public IgniteConfiguration igniteConfiguration(){
        IgniteConfiguration cfg = new IgniteConfiguration();
        cfg.setIgniteInstanceName("PaymentInstance");
        cfg.setPeerClassLoadingEnabled(true);
        cfg.setDataStorageConfiguration(dataStorageConfiguration.dataStorageConfiguration());
        cfg.setCacheConfiguration(cacheConfiguration.createCache("PaymentA"),
                cacheConfiguration.createCache("PaymentB"));
        cfg.setTransactionConfiguration(transactionalConfiguration());
        cfg.setDiscoverySpi(discoverySpi.discoverySpi());
        cfg.setClientConnectorConfiguration(clientConnectConfig());
        return cfg;
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
