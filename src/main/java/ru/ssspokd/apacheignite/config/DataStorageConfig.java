package ru.ssspokd.apacheignite.config;

import org.apache.ignite.configuration.DataPageEvictionMode;
import org.apache.ignite.configuration.DataRegionConfiguration;
import org.apache.ignite.configuration.DataStorageConfiguration;
import org.apache.ignite.configuration.WALMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataStorageConfig {

    private Long initSize = 20000000L;
    private Long maxSize = 300000000L;
    private final int  WAL_SEGMENT_SIZE= 1000000;
    private final long MAX_WAL_ARCHIVE_SIZE=100000000L;
    private String STORAGE_PATH = "e:/tmp/ignite/storage";
    private String WAL_PATH ="e:/tmp/ignite/wall";
    private String WAL_PATH_ARCHIVE_SIZE = "e:/tmp/ignite/wall_archive";
    private String NAME_DATA_REGION_CONGIF="dataRegionConfig";

    @Bean
    public DataStorageConfiguration dataStorageConfiguration(){
        DataStorageConfiguration dataStorageConfiguration =  new DataStorageConfiguration();
        dataStorageConfiguration.setDataRegionConfigurations(dataRegionConfigurations());
        dataStorageConfiguration.setWalSegmentSize(WAL_SEGMENT_SIZE);
        dataStorageConfiguration.setMaxWalArchiveSize(MAX_WAL_ARCHIVE_SIZE);
        dataStorageConfiguration.setStoragePath(STORAGE_PATH);
        dataStorageConfiguration.setWalPath(WAL_PATH);
        dataStorageConfiguration.setWalCompactionEnabled(true);
        dataStorageConfiguration.setWalMode(WALMode.FSYNC);
        dataStorageConfiguration.setWalArchivePath(WAL_PATH_ARCHIVE_SIZE);
        return dataStorageConfiguration;
    }

    private DataRegionConfiguration dataRegionConfigurations() {
        DataRegionConfiguration dataRegionConfiguration = new DataRegionConfiguration();
        dataRegionConfiguration.setName(NAME_DATA_REGION_CONGIF);
        dataRegionConfiguration.setInitialSize(initSize);
        dataRegionConfiguration.setMaxSize(maxSize);
        dataRegionConfiguration.setPageEvictionMode(DataPageEvictionMode.RANDOM_LRU);
        dataRegionConfiguration.setPersistenceEnabled(true);
        dataRegionConfiguration.setMetricsEnabled(true);
        dataRegionConfiguration.setSwapPath(null);
        return dataRegionConfiguration;
    }
}
