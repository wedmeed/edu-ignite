package edu.ignite.caching.service;

import edu.ignite.caching.dao.ProductsRepository;
import edu.ignite.caching.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@Slf4j
public class ProductsService {

    private ProductsRepository repository;

    public ProductsService(ProductsRepository repository) {
        this.repository = repository;
    }


    @Transactional
    public Boolean updatePrice(String id, Double newPrice) {
        log.debug("before read");
        var productWrap = repository.findById(id);
        if (productWrap.isPresent()) {
            var product = productWrap.get();
            product.setList_price(new BigDecimal(newPrice).setScale(2, RoundingMode.HALF_UP));
            log.debug("before write");
            repository.save(id, product);
            log.debug("after write");
            return true;
        }
        return false;
    }

    public Product getProduct(String id) {
        return repository.findById(id).orElse(null);
    }
}
