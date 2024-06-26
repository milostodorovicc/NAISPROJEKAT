package elasticsearch.elasticsearch.service;

import elasticsearch.elasticsearch.model.Product;
import elasticsearch.elasticsearch.model.ProductSearchResult;
import elasticsearch.elasticsearch.model.SearchResult;
import org.elasticsearch.action.search.SearchRequest;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.io.IOException;
import java.util.List;

public interface ProductService {

    Product save(Product product);
    Product read(String id1);
    Product searchproducttype(String type);
    Product update(Product product);
    String delete(String id1);
    ProductSearchResult complexquery1(String producttype, String minprice, String maxprice) throws IOException;
    ProductSearchResult complexquery2(String productmaterial,String brandname) throws IOException;
    List<Product> findbylikecount(String minlikecount, String maxlikecount) throws IOException;
    List<Product> findbycountryandavailable(String country) throws IOException;
    byte[] generisiizvestaj(String minlikecount, String maxlikecount,String country,String producttype,String minprice,String maxprice) throws IOException;
}
