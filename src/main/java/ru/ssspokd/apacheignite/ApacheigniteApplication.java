package ru.ssspokd.apacheignite;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteCluster;
import org.apache.ignite.Ignition;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.springdata20.repository.config.EnableIgniteRepositories;
import org.springframework.context.annotation.Configuration;
import ru.ssspokd.apacheignite.config.IgniteConfig;
import ru.ssspokd.apacheignite.model.Payment;

import java.util.Collection;

@Configuration
@EnableIgniteRepositories
public class ApacheigniteApplication {


    //@Bean
    public void Run(){
        IgniteConfig igniteConfig =  new IgniteConfig("Instance1", "PaymentA");
        Ignite ignite = Ignition.start(igniteConfig.igniteConfiguration());
        IgniteCluster igniteClusterA = ignite.cluster();
        ///
        IgniteConfig igniteConfig2 =  new IgniteConfig("Instance2", "PaymentB");
        Ignite ignite1 = Ignition.start(igniteConfig2.igniteConfiguration());
        IgniteCluster igniteClusterB = ignite1.cluster();
        //
        igniteClusterA.active(true);
        igniteClusterB.active(true);
        IgniteCache<Long, Payment> cache  = ignite.getOrCreateCache("PaymentA");
        IgniteCache<Long, Payment> cache1  = ignite1.getOrCreateCache("PaymentB");
        //cache.put(1L, new Payment(1L, "PaymentA", 5000L, new Date(), EnumOperation.NO_OPERATION));
        //cache1.put(1L, new Payment(1L, "PaymentB", 5008L, new Date(), EnumOperation.NO_OPERATION));
       // igniteClusterA.enableWal("false");
       // igniteClusterB.enableWal("false");
        Collection<ClusterNode> nodesA = igniteClusterA.forServers().nodes();
        Collection<ClusterNode> nodesB = igniteClusterB.forServers().nodes();
        igniteClusterA.setBaselineTopology(nodesA);
        igniteClusterB.setBaselineTopology(nodesB);
      }

      private void initCache(IgniteCache<Long, Payment> cache){

      }


    public static void main(String[] args) {
        ApacheigniteApplication apacheigniteApplication = new ApacheigniteApplication();
        apacheigniteApplication.Run();
    }

}
