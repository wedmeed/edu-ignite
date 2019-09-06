package edu.ignite.caching.model;

import lombok.Data;
import org.apache.ignite.cache.query.annotations.QuerySqlField;

import java.math.BigDecimal;

@Data
public class Product {
    @QuerySqlField(index = true)
    String uniq_id;
    String sku;
    @QuerySqlField
    String name_title;
    String description;
    @QuerySqlField
    BigDecimal list_price;
    BigDecimal sale_price;
    String category;
    String category_tree;
    String average_product_rating;
    String product_url;
    String product_image_urls;
    String brand;
    Integer total_number_reviews;
    String Reviews;
}
