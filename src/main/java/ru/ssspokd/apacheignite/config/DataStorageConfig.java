package ru.ssspokd.apacheignite.config;
import org.apache.ignite.configuration.DataPageEvictionMode;
import org.apache.ignite.configuration.DataRegionConfiguration;
import org.apache.ignite.configuration.DataStorageConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;

@Repository
public class DataStorageConfig {

    private Long initSize = 1000000000L;
    private Long maxSize = 20000000000L;


    @Bean
    public DataStorageConfiguration dataStorageConfiguration(){
        DataStorageConfiguration dataStorageConfiguration =  new DataStorageConfiguration();
        dataStorageConfiguration.setDataRegionConfigurations(dataRegionConfigurations());
        dataStorageConfiguration.setWalSegmentSize(1000000);
        dataStorageConfiguration.setMaxWalArchiveSize(1000000000L);
        return dataStorageConfiguration;
    }

    private DataRegionConfiguration dataRegionConfigurations() {
        DataRegionConfiguration dataRegionConfiguration = new DataRegionConfiguration();
        dataRegionConfiguration.setName("dataRegionCOnfig");
        dataRegionConfiguration.setInitialSize(initSize);
        dataRegionConfiguration.setMaxSize(maxSize);
        dataRegionConfiguration.setPageEvictionMode(DataPageEvictionMode.RANDOM_2_LRU);
        dataRegionConfiguration.setPersistenceEnabled(true);
        dataRegionConfiguration.setMetricsEnabled(true);
        return dataRegionConfiguration;
    }
}
