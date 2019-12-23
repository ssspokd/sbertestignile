package ru.ssspokd.apacheignite;

import org.apache.ignite.*;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.springdata20.repository.config.EnableIgniteRepositories;
import org.apache.ignite.transactions.*;
import org.springframework.context.annotation.Configuration;
import ru.ssspokd.apacheignite.config.IgniteConfig;
import ru.ssspokd.apacheignite.model.EnumOperation;
import ru.ssspokd.apacheignite.model.Payment;
import ru.ssspokd.apacheignite.store.CacheStore;

import javax.cache.CacheException;
import java.util.Collection;
import java.util.Date;

@Configuration
@EnableIgniteRepositories
public class ApacheigniteApplication {


    private boolean process = true;
    private  Ignite ignite;
    private  Ignite igniteB;

    //@Bean
    public void Run() {
        IgniteConfig igniteConfig = new IgniteConfig("Instance1", "PaymentA");
        ignite = Ignition.start(igniteConfig.igniteConfiguration());
        IgniteCluster igniteClusterA = ignite.cluster();
        ///
        IgniteConfig igniteConfig2 = new IgniteConfig("Instance2", "PaymentB");
        igniteB = Ignition.start(igniteConfig2.igniteConfiguration());
        IgniteCluster igniteClusterB = igniteB.cluster();
        //activateCluster
        igniteClusterA.active(true);
        igniteClusterB.active(true);
        //initCacheA
        initCache(ignite.getOrCreateCache("PaymentA"), ignite);
        //initCacheB
        initCache(igniteB.getOrCreateCache("PaymentB"), igniteB);
        //
        Collection<ClusterNode> nodesA = igniteClusterA.forServers().nodes();
        Collection<ClusterNode> nodesB = igniteClusterB.forServers().nodes();
        igniteClusterA.setBaselineTopology(nodesA);
        igniteClusterB.setBaselineTopology(nodesB);


    }




      private void initCache(IgniteCache<Long, Payment> cache,Ignite ignite){
        IgniteTransactions transactions = ignite.transactions();
        CacheStore cacheStore = new CacheStore();
        Payment payment = cacheStore.loadLastOperation(cache.getName());
        try (Transaction tx = transactions.txStart(TransactionConcurrency.OPTIMISTIC,
                TransactionIsolation.SERIALIZABLE, 30000, 0)){
                if(payment == null || payment.getId().toString().isEmpty()){
                        payment =  new Payment(1L, cache.getName(),
                                5000L,new Date(),
                                EnumOperation.NO_OPERATION);
                }
                cache.put(payment.getId(), payment);
            tx.commit();
        }
        catch (CacheException e) {
            if (e.getCause() instanceof TransactionTimeoutException &&
                    e.getCause().getCause() instanceof TransactionDeadlockException)

                System.out.println(e.getCause().getCause().getMessage());
        }
        catch (TransactionException e){
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }
      }


    public static void main(String[] args) {
        ApacheigniteApplication apacheigniteApplication = new ApacheigniteApplication();
        apacheigniteApplication.Run();
    }

}
