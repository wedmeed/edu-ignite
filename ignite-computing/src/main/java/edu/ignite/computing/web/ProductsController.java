package edu.ignite.computing.web;

import edu.ignite.computing.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.internal.util.lang.GridTuple;
import org.apache.ignite.internal.util.lang.IgnitePair;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/product/cache")
@Slf4j
public class ProductsController {

    private Ignite ignite;

    public ProductsController(Ignite ignite) {
        this.ignite = ignite;
    }

    @GetMapping("/load")
    public String loadCache() {
        IgniteCache<String, Product> cache = ignite.getOrCreateCache("ProductCache");
        cache.put("null", new Product());
        ignite.cache("ProductCache").loadCache(null);
        return "OK";
    }

    @GetMapping("/count")
    public Map<String, Integer> count() {
        Collection<Integer> results =  ignite.compute().apply(
                (IgnitePair<Double> pair)->{
                    log.info("Requested from {} to {}",pair.get1(), pair.get2());
                    return 0;
                },
                Arrays.asList(new IgnitePair<>(0.0, 50.0),
                        new IgnitePair<>(50.0, 100.0),
                        new IgnitePair<>(100.0, Double.POSITIVE_INFINITY))

        );
        log.info("Counted {} entries", results.stream().mapToInt(Integer::intValue).sum());
        return null;
    }


}
