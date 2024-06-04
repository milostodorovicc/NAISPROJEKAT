package elasticsearch.elasticsearch.repository;

import elasticsearch.elasticsearch.model.Product;
import elasticsearch.elasticsearch.model.SearchResult;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends ElasticsearchRepository<Product, String> {

    @Query("{\"ids\": {\"values\": [\"?0\"]}}")
    Product pronadjipoid(String id);


    @Query("{\"bool\": {\"must\": [{\"term\": {\"product_type\": \"?0\"}}]}}")
    Product pronadjipotype(String type);


//    @Query("{\"bool\": {\"must\": [{\"match\": {\"product_gender_target\": \"?0\"}}, {\"range\": {\"price_usd\": {\"gte\": ?1, \"lte\": ?2}}}]}, \"aggs\": {\"by_color\": {\"terms\": {\"field\": \"product_color\", \"size\": 10}}}}")
//    List<Product> complexquerysearch(String genderTarget, String minPrice, String maxPrice);


    @Query("\"query\":{\"bool\":{\"must\":[{\"match\":{\"product_gender_target\":\"?0\"}},{\"match\":{\"product_season\":\"?1\"}}]},\"aggs\":{\"attribute_stats\":{\"terms\":{\"field\":\"product_color\"}}}}")
    List<Product> complexquerysearch(String genderTarget, String productseason);

//
//    @Query("\"query\":{\"bool\": {\"must\": [{\"term\": {\"product_type\": \"?0\"}}]}},\"aggs\":{\"attribute_stats\":{\"terms\":{\"field\":\"product_color\"}}}}")
//    List<Product> complexquerysearch1(String gendertarget);




    @Query("{\"bool\":{\"must\":[{\"match\":{\"product_type\":\"?0\"}},{\"match\":{\"product_gender_target\":\"?1\"}}]}}")
    List<Product> complexquerysearch1(String producttype,String gendertarget,String productcolor);

}