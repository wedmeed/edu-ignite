package edu.ignite.caching.config;


import com.datastax.driver.core.policies.RoundRobinPolicy;
import edu.ignite.caching.model.Product;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.store.cassandra.CassandraCacheStoreFactory;
import org.apache.ignite.cache.store.cassandra.datasource.DataSource;
import org.apache.ignite.cache.store.cassandra.persistence.KeyValuePersistenceSettings;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.springdata20.repository.config.EnableIgniteRepositories;
import org.apache.ignite.transactions.TransactionConcurrency;
import org.apache.ignite.transactions.spring.SpringTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import java.io.File;
import java.util.concurrent.TimeUnit;


@Configuration
@EnableIgniteRepositories("edu.ignite.caching.dao")
public class CacheConfig {

    @Bean
    public IgniteConfiguration igniteConfiguration() {
        IgniteConfiguration cfg = new IgniteConfiguration();
        cfg.setIgniteInstanceName("jcpenney-store");
        cfg.setPeerClassLoadingEnabled(true);

        CacheConfiguration<String, Product> ccfg = new CacheConfiguration<>("ProductCache");
        ccfg.setIndexedTypes(String.class, Product.class);
        DataSource dataSource = new DataSource();
        dataSource.setContactPoints("127.0.0.1");
        RoundRobinPolicy robinPolicy = new RoundRobinPolicy();
        dataSource.setLoadBalancingPolicy(robinPolicy);
        dataSource.setReadConsistency("ONE");
        dataSource.setWriteConsistency("ONE");
        dataSource.setJmxReporting(true);
        KeyValuePersistenceSettings persistenceSettings = new KeyValuePersistenceSettings(new File(getClass().getResource("/persistence_settings.xml").getFile()));
        CassandraCacheStoreFactory<String, Product> cacheStoreFactory = new CassandraCacheStoreFactory<>();
        cacheStoreFactory.setDataSource(dataSource);
        cacheStoreFactory.setPersistenceSettings(persistenceSettings);
        ccfg.setCacheStoreFactory(cacheStoreFactory);

//        ccfg.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);

        ccfg.setWriteThrough(true);
//        ccfg.setWriteBehindEnabled(true);
//        ccfg.setWriteBehindFlushFrequency(30000);

        ccfg.setReadThrough(true);
        ccfg.setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, 70)));

        ccfg.setStatisticsEnabled(true);

        cfg.setCacheConfiguration(ccfg);
        return cfg;
    }

    /**
     * Creating Apache Ignite instance bean. A bean will be passed
     * to IgniteRepositoryFactoryBean to initialize all Ignite based Spring Data      * repositories and connect to a cluster.
     */
    @Bean
    public Ignite igniteInstance(IgniteConfiguration cfg) {
        Ignite ignite = Ignition.start(cfg);

        // if this warm-up will be skipped then first transaction without preventing by
        // non-transactional query will be failed with
        // "Failed to execute compound future reducer: GridDhtLockFuture"
        ignite.cache("ProductCache").put("null", new Product());

        //ignite.cache("ProductCache").loadCache(null);
        return ignite;
    }

    @Bean
    public SpringTransactionManager transactionManager(Ignite ignite) {
        SpringTransactionManager mngr = new SpringTransactionManager();
        mngr.setIgniteInstanceName(ignite.name());
        mngr.setTransactionConcurrency(TransactionConcurrency.PESSIMISTIC);
        return mngr;
    }
}
