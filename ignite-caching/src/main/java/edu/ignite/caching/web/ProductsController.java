package edu.ignite.caching.web;

import edu.ignite.caching.model.Product;
import edu.ignite.caching.service.ProductsService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/product")
public class ProductsController {

    private ProductsService srv;

    public ProductsController(ProductsService srv) {
        this.srv = srv;
    }


    @GetMapping("/{id}")
    public Mono<Product> getProduct(@PathVariable String id) {
        return Mono.fromCallable(() -> srv.getProduct(id));
    }

    @PutMapping("/{id}/price")
    public Mono<Boolean> changeProductPrice(@PathVariable String id, @RequestParam("price") Double newPrice) {
        return Mono.fromCallable(() -> srv.updatePrice(id, newPrice));
    }


}
