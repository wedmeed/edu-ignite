package edu.ignite.computing.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.apache.ignite.cache.query.annotations.QuerySqlField;

import java.math.BigDecimal;

@Data
//@JsonPropertyOrder({ "name", "surname", "shoesize", "gender" })
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
    @JsonProperty(value = "Reviews")
    String Reviews;
}
