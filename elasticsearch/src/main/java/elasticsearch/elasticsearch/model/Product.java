package elasticsearch.elasticsearch.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "products1")
public class Product {

    @Id
    private String id;

    private String price_usd;

    private String product_season;

    private String product_keywords;

    private String product_condition;

    private String product_color;

    private String product_material;

    private String product_type;

    private String brand_name;

    private String product_gender_target;

    private String product_like_count;

    private String seller_country;

    private String available;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSeller_country() {
        return seller_country;
    }

    public void setSeller_country(String seller_country) {
        this.seller_country = seller_country;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getProduct_like_count() {
        return product_like_count;
    }

    public void setProduct_like_count(String product_like_count) {
        this.product_like_count = product_like_count;
    }

    public String getPrice_usd() {
        return price_usd;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getProduct_gender_target() {
        return product_gender_target;
    }

    public void setProduct_gender_target(String product_gender_target) {
        this.product_gender_target = product_gender_target;
    }

    public void setPrice_usd(String price_usd) {
        this.price_usd = price_usd;
    }

    public String getProduct_season() {
        return product_season;
    }

    public void setProduct_season(String product_season) {
        this.product_season = product_season;
    }

    public String getProduct_keywords() {
        return product_keywords;
    }

    public void setProduct_keywords(String product_keywords) {
        this.product_keywords = product_keywords;
    }

    public String getProduct_condition() {
        return product_condition;
    }

    public void setProduct_condition(String product_condition) {
        this.product_condition = product_condition;
    }

    public String getProduct_color() {
        return product_color;
    }

    public void setProduct_color(String product_color) {
        this.product_color = product_color;
    }

    public String getProduct_material() {
        return product_material;
    }

    public void setProduct_material(String product_material) {
        this.product_material = product_material;
    }

    public String getProduct_type() {
        return product_type;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }

    public Product() {
    }

    public Product(String id, String price_usd, String product_season, String product_keywords, String product_condition, String product_color, String product_material, String product_type, String brand_name, String product_gender_target, String product_like_count, String seller_country,String available) {
        this.id = id;
        this.price_usd = price_usd;
        this.product_season = product_season;
        this.product_keywords = product_keywords;
        this.product_condition = product_condition;
        this.product_color = product_color;
        this.product_material = product_material;
        this.product_type = product_type;
        this.brand_name = brand_name;
        this.product_gender_target = product_gender_target;
        this.product_like_count = product_like_count;
        this.seller_country = seller_country;
        this.available = available;
    }
}
