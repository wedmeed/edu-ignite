package edu.ignite.computing.client.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import edu.ignite.computing.client.model.Product;
import lombok.NoArgsConstructor;
import org.apache.ignite.cache.store.CacheStoreAdapter;
import org.apache.ignite.lang.IgniteBiInClosure;

import javax.cache.Cache;
import javax.cache.integration.CacheLoaderException;
import java.io.File;
import java.io.IOException;


@NoArgsConstructor
public class CacheCsvProductStore extends CacheStoreAdapter<String, Product> {
    @Override
    public Product load(String key) {
        //this one-time-use persistence adapter does not support such operation
        return null;
    }

    @Override
    public void write(Cache.Entry<? extends String, ? extends Product> entry) {
        //this read-only persistence adapter does not support such operation
    }

    @Override
    public void delete(Object key) {
        //this read-only persistence adapter does not support such operation
    }

    @Override
    public void loadCache(IgniteBiInClosure<String, Product> clo, Object... args) {
        if (args == null || args.length == 0 || args[0] == null || !(args[0] instanceof String)) {
            throw new CacheLoaderException("Expected file name as first arg");
        }

        System.out.println("---------------- loadCache ----------------");
        File resource = new File((String) args[0]);
        if (resource.exists()) {
            loadUsingJackson(clo, resource);
            System.out.println("---------------- CacheLoaded ----------------");
        } else {
            System.out.println("---------------- FileIsMissed ----------------");
        }

    }

    private void loadUsingJackson(IgniteBiInClosure<String, Product> clo, File resource) {
        try {
            CsvMapper mapper = new CsvMapper();
            mapper.addHandler(new DeserializationProblemHandler() {
                volatile int count = 0;

                @Override
                public Object handleWeirdStringValue(DeserializationContext ctxt, Class<?> targetType, String valueToConvert, String failureMsg) {
                    if (++count % 100 == 0) {
                        System.out.println("Roughly " + count + "+ entries are skipped");
                    }
                    return null;
                }
            });
            mapper.readerFor(Product.class)
                    .with(JsonParser.Feature.AUTO_CLOSE_SOURCE)
                    .with(mapper
                            .typedSchemaFor(Product.class)
                            .withHeader()
                            .withColumnReordering(true)
                            .withUseHeader(true)
                            .withEscapeChar('"'))
                    .<Product>readValues(resource)
                    .forEachRemaining(product -> clo.apply(product.getUniq_id(), product));
        } catch (IOException e) {
            throw new CacheLoaderException(e);
        }
    }


}
