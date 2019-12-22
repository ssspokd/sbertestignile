package ru.ssspokd.apacheignite;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCluster;
import org.apache.ignite.Ignition;
import org.apache.ignite.springdata20.repository.config.EnableIgniteRepositories;
import org.springframework.context.annotation.Configuration;
import ru.ssspokd.apacheignite.config.IgniteConfig;

@Configuration
@EnableIgniteRepositories
public class ApacheigniteApplication {


    //@Bean
    public void Run(){
        IgniteConfig igniteConfig =  new IgniteConfig("Instanse1", "PaymentA");
        Ignite ignite = Ignition.start(igniteConfig.igniteConfiguration());
        IgniteCluster igniteClusterA = ignite.cluster();
        igniteClusterA.active(true);
    }


    public static void main(String[] args) {
        ApacheigniteApplication apacheigniteApplication = new ApacheigniteApplication();
        apacheigniteApplication.Run();
    }

}
