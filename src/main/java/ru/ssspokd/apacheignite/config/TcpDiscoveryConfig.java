package ru.ssspokd.apacheignite.config;

import org.apache.ignite.resources.IgniteInstanceResource;
import org.apache.ignite.resources.SpringResource;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.jdbc.TcpDiscoveryJdbcIpFinder;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.Arrays;

@Repository
@Service
public class TcpDiscoveryConfig {

    @Autowired
    @Qualifier("dataSource")
    @IgniteInstanceResource
    DataSource dataSource;
    @Autowired
    @IgniteInstanceResource
    @SpringResource( resourceName = "getStrSpr")
    String getStr;

    //@Bean
    public TcpDiscoverySpi discoverySpi(){
        TcpDiscoverySpi tcpDiscoverySpi = new TcpDiscoverySpi();
        tcpDiscoverySpi.setName("ipFinder");
        tcpDiscoverySpi.setAckTimeout(1000);
        tcpDiscoverySpi.setIpFinder(tcpDiscoveryJdbcIpFinder());
        tcpDiscoverySpi.setReconnectCount(10);
        tcpDiscoverySpi.setReconnectDelay(1000);
        tcpDiscoverySpi.setMaxAckTimeout(60000);
        tcpDiscoverySpi.setSocketTimeout(1000);
        return tcpDiscoverySpi;
    }


    private TcpDiscoveryVmIpFinder tcpDiscoveryVmIpFinder(){
        TcpDiscoveryVmIpFinder tcpDiscoveryVmIpFinder =  new TcpDiscoveryVmIpFinder();
        tcpDiscoveryVmIpFinder.setAddresses(Arrays.asList("127.0.0.1:11211", "127.0.0.1:47500..47509"));
        return  tcpDiscoveryVmIpFinder;
    }


    private TcpDiscoveryMulticastIpFinder tcpDiscoveryMulticastIpFinder() {
        TcpDiscoveryMulticastIpFinder tcMp = new TcpDiscoveryMulticastIpFinder();
        tcMp.setLocalAddress("127.0.0.1");
        tcMp.setMulticastPort(11211);
        return tcMp;
    }

    private TcpDiscoveryJdbcIpFinder tcpDiscoveryJdbcIpFinder(){
        TcpDiscoveryJdbcIpFinder tcpDiscoveryJdbcIpFinder =  new TcpDiscoveryJdbcIpFinder();
        if(dataSource==null){
            DataSourcesConfig dataSourcesConfig = new DataSourcesConfig();
            dataSource = dataSourcesConfig.dataSource();
        }
        tcpDiscoveryJdbcIpFinder.setDataSource(dataSource);
        return tcpDiscoveryJdbcIpFinder;
    }
}
