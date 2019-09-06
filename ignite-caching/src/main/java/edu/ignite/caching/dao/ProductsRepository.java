package edu.ignite.caching.dao;

import edu.ignite.caching.model.Product;
import org.apache.ignite.springdata20.repository.IgniteRepository;
import org.apache.ignite.springdata20.repository.config.RepositoryConfig;

@RepositoryConfig(cacheName = "ProductCache")
public interface ProductsRepository extends IgniteRepository<Product, String> {
}
