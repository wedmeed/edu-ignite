package edu.ignite.computing.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.transactions.TransactionConcurrency;
import org.apache.ignite.transactions.spring.SpringTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@Slf4j
public class CacheConfig {


    /**
     * Creating Apache Ignite instance bean. A bean will be passed
     * to IgniteRepositoryFactoryBean to initialize all Ignite based Spring Data      * repositories and connect to a cluster.
     */
    @Bean
    public Ignite igniteInstance() {
        Ignite ignite = Ignition.start("file:///Users/asokolov/Desktop/edu/ignite/ignite-computing/src/main/resources/config.xml");
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
