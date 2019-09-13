package edu.ignite.computing.client.config;

import edu.ignite.computing.client.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.configuration.FactoryBuilder;


@Configuration
@Slf4j
public class CacheConfig {


    @Bean
    public CacheConfiguration<String, Product> productCacheConfiguration() {

        CacheConfiguration<String, Product> ccfg = new CacheConfiguration<>("ProductCache");
        ccfg.setIndexedTypes(String.class, Product.class);
        ccfg.setCacheStoreFactory(FactoryBuilder.factoryOf(CacheCsvProductStore.class));

//        ccfg.setWriteThrough(true);
//        ccfg.setReadThrough(true);
//        ccfg.setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, 70)));

        ccfg.setStatisticsEnabled(true);

        return ccfg;
    }

    @Bean
    public Ignite igniteInstance() {
        IgniteConfiguration cfg = new IgniteConfiguration();
        cfg.setClientMode(true);
        cfg.setPeerClassLoadingEnabled(true);

        return Ignition.start(cfg);
    }


}
