# ignite-caching
Capstone project for IMDG course

## Installation

1. run docker container from https://hub.docker.com/_/cassandra,
configure volumes and ports
1. connect to docker terminal and enter to ``` cqlsh ```
1. create keyspace: ``` CREATE KEYSPACE jcpenney With 
replication = {'class': 'SimpleStrategy', 'replication_factor' : 1}; ```
1. create table as following: ``` CREATE TABLE jcpenney.products (
uniq_id VARCHAR PRIMARY KEY,
sku VARCHAR,
name_title TEXT,
description TEXT,
list_price DECIMAL,
sale_price DECIMAL,
category VARCHAR,
category_tree VARCHAR,
average_product_rating VARCHAR,
product_url TEXT,
product_image_urls TEXT,
brand VARCHAR,
total_number_reviews INT,
Reviews TEXT); ```
1. download data from csv: ``` COPY jcpenney.products (uniq_id,sku,name_title,description,list_price,sale_price,category,category_tree,average_product_rating,product_url,product_image_urls,brand,total_number_reviews,Reviews)
FROM '/var/lib/cassandra/jcpenney_com-ecommerce_sample.csv' 
WITH DELIMITER=',' AND HEADER=TRUE AND ESCAPE='"'; ```
1. Run application by ```gradlew bootRun```
1. work by the Postman with Ignite-caching.postman_collection.json
1. see metrics through JMX under ```org.apache```

