package elasticsearch.elasticsearch.controller;

import elasticsearch.elasticsearch.model.Product;
import elasticsearch.elasticsearch.model.ProductSearchResult;
import elasticsearch.elasticsearch.model.SearchResult;
import elasticsearch.elasticsearch.service.ProductService;
import elasticsearch.elasticsearch.service.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public Product addProduct(@RequestBody Product product) {




        Product product1 = productService.save(product);
        return product1;
    }


    @GetMapping("/pronadjipoid/{id1}")
    public Product readProduct(@PathVariable String id1) {
        Product product = productService.read(id1);
        return product;

    }


    @PostMapping(value="/searchtype")
    public Product searchProduct(@RequestParam("type") String type) {
        Product product = productService.searchproducttype(type);
        return product;

    }



    @PostMapping(value="/update")
    public Product updateProduct(@RequestBody Product product) {
       Product product1 = productService.update(product);
       return product1;
    }



    @PostMapping(value="/delete/{id1}")
    public void deleteProduct(@PathVariable String id1) {
        String deleted =  productService.delete(id1);

    }



    @PostMapping("/complexquery1")
    public ProductSearchResult complexquery1(@RequestParam("producttype") String producttype,@RequestParam("minprice") String minprice,@RequestParam("maxprice") String maxprice) throws IOException {
        ProductSearchResult productSearchResult = productService.complexquery1(producttype,minprice,maxprice);
        return productSearchResult;



    }



    @PostMapping("/complexquery2")
    public ProductSearchResult complexquery2(@RequestParam("productmaterial") String productmaterial,@RequestParam("brandname") String brandname) throws IOException {
        ProductSearchResult productSearchResult = productService.complexquery2(productmaterial,brandname);
        return productSearchResult;



    }




    @PostMapping("/findbylikecount")
    public List<Product> findbylikecount(@RequestParam("minlikecount") String minlikecount,@RequestParam("maxlikecount") String maxlikecount) throws IOException {
      List<Product> products =  productService.findbylikecount(minlikecount,maxlikecount);


      return products;

    }


    @PostMapping("/findbycountryandavailable")
    public List<Product> findbycountryandavailable(@RequestParam("country") String country) throws IOException {
        List<Product> products =  productService.findbycountryandavailable(country);


        return products;

    }



    @PostMapping("/generisizivestaj")
    public byte[] findbycountryandavailable(@RequestParam("minlikecount") String minlikecount,@RequestParam("maxlikecount") String maxlikecount,@RequestParam("country") String country,@RequestParam("producttype") String producttype,@RequestParam("minprice") String minprice,@RequestParam("maxprice") String maxprice) throws IOException {
        byte[] izvestaj =  productService.generisiizvestaj(minlikecount,maxlikecount,country,producttype,minprice,maxprice);


        return izvestaj;

    }


}
