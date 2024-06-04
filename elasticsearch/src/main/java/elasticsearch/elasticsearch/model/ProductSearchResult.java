package elasticsearch.elasticsearch.model;

import java.util.List;

public class ProductSearchResult {

    List<Product> products;
    List<SearchResult> agregacije;

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<SearchResult> getAgregacije() {
        return agregacije;
    }

    public void setAgregacije(List<SearchResult> agregacije) {
        this.agregacije = agregacije;
    }

    public ProductSearchResult() {
    }

    public ProductSearchResult(List<Product> products, List<SearchResult> agregacije) {
        this.products = products;
        this.agregacije = agregacije;
    }
}
