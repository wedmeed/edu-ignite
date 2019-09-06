package edu.ignite.computing.config;

import com.datastax.driver.core.policies.RoundRobinPolicy;
import edu.ignite.computing.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.store.cassandra.CassandraCacheStoreFactory;
import org.apache.ignite.cache.store.cassandra.datasource.DataSource;
import org.apache.ignite.cache.store.cassandra.persistence.KeyValuePersistenceSettings;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.transactions.TransactionConcurrency;
import org.apache.ignite.transactions.spring.SpringTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import java.io.File;
import java.util.concurrent.TimeUnit;


@Configuration
@Slf4j
public class CacheConfig {

    @Bean
    public CacheConfiguration<String, Product> cacheConfiguration() {
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

        ccfg.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);

        ccfg.setWriteThrough(true);

        ccfg.setReadThrough(true);
        ccfg.setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, 70)));

        ccfg.setStatisticsEnabled(true);

        return ccfg;
    }

    /**
     * Creating Apache Ignite instance bean. A bean will be passed
     * to IgniteRepositoryFactoryBean to initialize all Ignite based Spring Data      * repositories and connect to a cluster.
     */
    @Bean
    public Ignite igniteInstance(CacheConfiguration<String, Product> ccfg) {
        Ignite ignite = Ignition.start("./config.xml");
        ignite.getOrCreateCache(ccfg);

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
