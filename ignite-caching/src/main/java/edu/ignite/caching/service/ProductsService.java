package edu.ignite.caching.service;

import edu.ignite.caching.dao.ProductsRepository;
import edu.ignite.caching.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@Slf4j
public class ProductsService {

    private ProductsRepository repository;
    private IgniteCache<String, Product> cache;

    public ProductsService(ProductsRepository repository, Ignite ignite) {
        this.repository = repository;
        this.cache = ignite.cache("ProductCache");
    }

    public Boolean updatePrice(String id, Double newPrice) {
        return cache.invoke(id, (entry, args) -> {
            if (entry.exists()) {
                Product value = entry.getValue();
                BigDecimal newNormalizedPrice = new BigDecimal((Double) args[0]).setScale(2, RoundingMode.HALF_UP);
                value.setList_price(newNormalizedPrice);
                entry.setValue(value);
                return true;
            }
            return false;
        }, newPrice);
    }

    public Product getProduct(String id) {
        return repository.findById(id).orElse(null);
    }
}
