package ru.ssspokd.apacheignite;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCluster;
import org.apache.ignite.Ignition;
import org.apache.ignite.springdata20.repository.config.EnableIgniteRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import ru.ssspokd.apacheignite.config.IgniteConfig;
import ru.ssspokd.apacheignite.config.SpringDataConfig;

@Configuration
@EnableIgniteRepositories
public class ApacheigniteApplication {

    @Autowired(required = true)
    private IgniteConfig igniteConfiguration;

    public ApacheigniteApplication() {
        igniteConfiguration  = new IgniteConfig();
    }

    //@Bean
    public void Run(){
        IgniteConfig igniteConfig =  new IgniteConfig();
        Ignite ignite = Ignition.start(igniteConfig.igniteConfiguration());
        IgniteCluster igniteClusterA = ignite.cluster();
        igniteClusterA.active(true);
    }


    public static void main(String[] args) {
        ApacheigniteApplication apacheigniteApplication = new ApacheigniteApplication();
        apacheigniteApplication.Run();
    }

}
