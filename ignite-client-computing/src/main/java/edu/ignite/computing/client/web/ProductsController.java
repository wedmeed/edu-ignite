package edu.ignite.computing.client.web;

import edu.ignite.computing.client.model.PriceCategory;
import edu.ignite.computing.client.model.Product;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.lang.IgniteBiTuple;
import org.apache.ignite.lang.IgniteCallable;
import org.apache.ignite.lang.IgniteClosure;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/product/cache")
public class ProductsController {

    private Ignite ignite;
    CacheConfiguration<String, Product> ccfg;

    public ProductsController(Ignite ignite, CacheConfiguration<String, Product> ccfg) {
        this.ignite = ignite;
        this.ccfg = ccfg;
    }

    @GetMapping("/load")
    public String loadCache() {
        IgniteCache<String, Product> cache = ignite.getOrCreateCache(ccfg);
        cache.loadCache(null, "/Users/asokolov/Desktop/edu/ignite/ignite-caching/jcpenney_com-ecommerce_sample.csv");
        return "OK";
    }

    @GetMapping("/count")
    public Map<String, Long> count() {
        Instant start = Instant.now();
        Collection<IgniteBiTuple<String, Long>> results = ignite.compute().apply(
                new IgniteClosure<PriceCategory, IgniteBiTuple<String, Long>>() {
                    @IgniteInstanceResource
                    private Ignite ign;

                    @Override
                    public IgniteBiTuple<String, Long> apply(PriceCategory category) {
                        String diap = "from " + category.getBottom() + " to " + category.getTop();
                        System.out.println("Requested " + diap);
                        SqlFieldsQuery sql = new SqlFieldsQuery(
                                "select count(*) from Product where list_price >= " + category.getBottom() +
                                        " AND list_price < " + category.getTop());

                        IgniteBiTuple<String, Long> res = new IgniteBiTuple<>(diap, 0L);
                        IgniteCache cache = ign.cache("ProductCache");
                        try (QueryCursor<List<Long>> cursor = cache.query(sql)) {
                            res.setValue(cursor.getAll().get(0).get(0));
                        }
                        return res;
                    }
                },
                Arrays.asList(PriceCategory.CHEAP,
                        PriceCategory.NORMAL,
                        PriceCategory.EXPENSIVE)

        );
        Map<String, Long> res = new HashMap<>();
        results.forEach(res::putAll);
        res.put("Total time", Duration.between(start, Instant.now()).toMillis());
        return res;
    }

    @GetMapping("/countLocally")
    public Map<String, Long> countLocally() {
        Instant start = Instant.now();
        Collection<Map<String, Long>> results = ignite.compute().broadcast(
                new IgniteCallable<Map<String, Long>>() {
                    @IgniteInstanceResource
                    private Ignite ign;

                    @Override
                    public Map<String, Long> call() throws Exception {
                        Instant localStart = Instant.now();
                        Map<String, Long> result = new HashMap<>();
                        for (PriceCategory category : Arrays.asList(PriceCategory.CHEAP, PriceCategory.NORMAL, PriceCategory.EXPENSIVE)) {
                            String diap = "from " + category.getBottom() + " to " + category.getTop();
                            SqlFieldsQuery sql = new SqlFieldsQuery(
                                    "select count(*) from Product where list_price >= " + category.getBottom() +
                                            " AND list_price < " + category.getTop()).setLocal(true);
                            IgniteCache cache = ign.cache("ProductCache");
                            try (QueryCursor<List<Long>> cursor = cache.query(sql)) {
                                result.put(diap, cursor.getAll().get(0).get(0));
                            }
                        }
                        System.out.println("Done in " + Duration.between(localStart, Instant.now()).toMillis());
                        System.out.println(result);
                        return result;
                    }
                });
        Map<String, Long> res = new HashMap<>();
        results.forEach(map ->
                map.forEach((key, value) ->
                        res.put(key, res.getOrDefault(key, 0L) + value)));
        res.put("Total time", Duration.between(start, Instant.now()).toMillis());
        return res;
    }


}
