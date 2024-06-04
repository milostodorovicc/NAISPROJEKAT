package elasticsearch.elasticsearch.service;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.json.JsonData;
import elasticsearch.elasticsearch.model.Product;
import elasticsearch.elasticsearch.model.ProductSearchResult;
import elasticsearch.elasticsearch.model.SearchResult;
import elasticsearch.elasticsearch.repository.ProductRepository;
import org.apache.http.HttpHost;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;

import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregations;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl {

    ElasticsearchOperations elasticsearchOperations;


    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository,ElasticsearchOperations elasticsearchOperations) {

        this.productRepository = productRepository;
        this.elasticsearchOperations = elasticsearchOperations;


    }


    public void save(Product product) {
        productRepository.save(product);
    }


    public Product read(String id1) {


        Product product12 = this.productRepository.pronadjipoid(id1);

        return product12;


    }


    public Product searchproducttype(String type) {


        Product product12 = this.productRepository.pronadjipotype(type);

        return product12;


    }


    public Product update(Product product) {


        Product product2 = productRepository.save(product);
        return product2;
    }


    public String delete(String id1) {


        productRepository.deleteById(id1);
        return "Uspesno ste obrisali odabrani proizvod";
    }





    public List<Product> findbycountryandavailable(String country) throws IOException {

        System.out.println(country);

        NativeQuery query = NativeQuery.builder().withQuery(Query.of(q -> q
                        .bool(b -> b
                                .filter(f -> f
                                        .match(m -> m
                                                .field("seller_country")
                                                .query(country)
                                        )
                                )
                                .filter(f -> f
                                        .match(m -> m
                                                .field("available")
                                                .query("True")
                                        )
                                )
                        )
                )
        )
                .build();

        query.setMaxResults(10000);

        SearchHits<Product> searchHits = elasticsearchOperations.search(query, Product.class);

        List<Product> products = searchHits.getSearchHits()
                .stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());

        int count = products.size();
        System.out.println(count);


        return products;



    }


    public List<Product> findbylikecount(String minlikecount,String maxlikecount) throws IOException {

        System.out.println(minlikecount+maxlikecount);

        NativeQuery query = NativeQuery.builder().withQuery(Query.of(q -> q
                        .bool(b -> b

                                .filter(f -> f
                                        .range(r -> r
                                                .field("product_like_count")
                                                .gte(JsonData.of(minlikecount))
                                                .lte(JsonData.of(maxlikecount))
                                        )
                                )
                        )
                )
        )
                .build();



        query.setMaxResults(10000);

        SearchHits<Product> searchHits = elasticsearchOperations.search(query, Product.class);

        List<Product> products = searchHits.getSearchHits()
                .stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());

        int count = products.size();
        System.out.println(count);


       return products;
    }

        public ProductSearchResult complexquery1(String producttype,String minprice,String maxprice) throws IOException {

        System.out.println(producttype+minprice+maxprice);

            NativeQuery query = NativeQuery.builder().withQuery(Query.of(q -> q
                    .bool(b -> b
                            .filter(f -> f
                                    .match(m -> m
                                            .field("product_type")
                                            .query(producttype)
                                    )
                            )
                            .filter(f -> f
                                    .range(r -> r
                                            .field("price_usd")
                                            .gte(JsonData.of(minprice))
                                            .lte(JsonData.of(maxprice))
                                    )
                            )
                    )
                    )
            )
                    .withAggregation("suggestions", Aggregation.of(a -> a
                            .terms(ta -> ta.field("product_color").size(10).minDocCount(2))))
                    .build();

            SearchHits<Product> searchHits = elasticsearchOperations.search(query, Product.class);
            List<Product> products = searchHits.getSearchHits()
                    .stream()
                    .map(SearchHit::getContent)
                    .collect(Collectors.toList());


            ElasticsearchAggregations aggregations = (ElasticsearchAggregations) searchHits.getAggregations();
            assert aggregations != null;
            List<StringTermsBucket> buckets = aggregations.aggregationsAsMap().get("suggestions").aggregation().getAggregate().sterms().buckets().array();

            List<SearchResult> result = new ArrayList<>();

            ProductSearchResult productSearchResult = new ProductSearchResult();
            productSearchResult.setProducts(products);

            buckets.forEach(stringTermsBucket -> result.add(
                   SearchResult.builder()
                            .color(stringTermsBucket.key().stringValue())
                            .count(stringTermsBucket.docCount())
                            .build()
            ));

            productSearchResult.setAgregacije(result);
           for(SearchResult sr: result)
           {
               System.out.println(sr.getColor()+sr.getCount());
           }

           return productSearchResult;
    }





    public void complexquery2(String productmaterial,String brandname) throws IOException {

        System.out.println(productmaterial+brandname);

        NativeQuery query = NativeQuery.builder().withQuery(Query.of(q -> q
                        .bool(b -> b
                                .filter(f -> f
                                        .match(m -> m
                                                .field("product_material")
                                                .query(productmaterial)
                                        )
                                )
                                .filter(f -> f
                                        .match(m -> m
                                                .field("brand_name")
                                                .query(brandname)
                                        )
                                )
                        )
                )
        )
                .withAggregation("productseason", Aggregation.of(a -> a
                        .terms(ta -> ta.field("product_season").size(10).minDocCount(2))))
                .build();

        SearchHits<Product> searchHits = elasticsearchOperations.search(query, Product.class);
        ElasticsearchAggregations aggregations = (ElasticsearchAggregations) searchHits.getAggregations();
        assert aggregations != null;
        List<StringTermsBucket> buckets = aggregations.aggregationsAsMap().get("productseason").aggregation().getAggregate().sterms().buckets().array();

        List<SearchResult> result = new ArrayList<>();

        buckets.forEach(stringTermsBucket -> result.add(
                SearchResult.builder()
                        .color(stringTermsBucket.key().stringValue())
                        .count(stringTermsBucket.docCount())
                        .build()
        ));

        for(SearchResult sr: result)
        {
            System.out.println(sr.getColor()+sr.getCount());
        }

    }









    public byte[] generisiizvestaj(String minlikecount, String maxlikecount,String country,String producttype,String minprice,String maxprice) throws IOException
    {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Document document = new Document();

        String filename =  LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm-ss")) + ".pdf";

        PdfWriter.getInstance(document, byteArrayOutputStream);
        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 15, Font.BOLD);

        Paragraph title = new Paragraph("PROIZVODI KOJI IMAJU IZMEDJU "+minlikecount+" I "+maxlikecount+" LAJKOVA", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph("\n"));

        PdfPTable reportTable = new PdfPTable(7);
        reportTable.setWidthPercentage(100);

        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8, Font.BOLD);
        PdfPCell headerCell1 = new PdfPCell(new Paragraph("Price usd", headerFont));
        PdfPCell headerCell2 = new PdfPCell(new Paragraph("Product season", headerFont));
        PdfPCell headerCell3 = new PdfPCell(new Paragraph("Product condition", headerFont));
        PdfPCell headerCell4 = new PdfPCell(new Paragraph("Product color", headerFont));
        PdfPCell headerCell5 = new PdfPCell(new Paragraph("Product material", headerFont));
        PdfPCell headerCell6 = new PdfPCell(new Paragraph("Product type", headerFont));
        PdfPCell headerCell7 = new PdfPCell(new Paragraph("Product like count", headerFont));

        headerCell1.setBackgroundColor(new Color(110, 231, 234, 255));
        headerCell2.setBackgroundColor(new Color(110, 231, 234, 255));
        headerCell3.setBackgroundColor(new Color(110, 231, 234, 255));
        headerCell4.setBackgroundColor(new Color(110, 231, 234, 255));
        headerCell5.setBackgroundColor(new Color(110, 231, 234, 255));
        headerCell6.setBackgroundColor(new Color(110, 231, 234, 255));
        headerCell7.setBackgroundColor(new Color(110, 231, 234, 255));

        reportTable.addCell(headerCell1);
        reportTable.addCell(headerCell2);
        reportTable.addCell(headerCell3);
        reportTable.addCell(headerCell4);
        reportTable.addCell(headerCell5);
        reportTable.addCell(headerCell6);
        reportTable.addCell(headerCell7);

        List<Product> products = findbylikecount(minlikecount,maxlikecount);

        for (Product product : products) {
            reportTable.addCell(product.getPrice_usd());
            reportTable.addCell(product.getProduct_season());
            reportTable.addCell(product.getProduct_condition());
            reportTable.addCell(product.getProduct_color());
            reportTable.addCell(product.getProduct_material());
            reportTable.addCell(product.getProduct_type());
            reportTable.addCell(product.getProduct_like_count());
        }

        document.add(reportTable);
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("\n"));

        Paragraph title1 = new Paragraph("PROIZVODI IZ DRZAVE "+country+" KOJI SU DOSTUPNI", titleFont);
        title1.setAlignment(Element.ALIGN_CENTER);
        document.add(title1);
        document.add(new Paragraph("\n"));

        PdfPTable reportTable1 = new PdfPTable(7);
        reportTable1.setWidthPercentage(100);

        Font headerFont1 = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8, Font.BOLD);
        PdfPCell headerCell12 = new PdfPCell(new Paragraph("Price usd", headerFont1));
        PdfPCell headerCell22 = new PdfPCell(new Paragraph("Product season", headerFont1));
        PdfPCell headerCell32 = new PdfPCell(new Paragraph("Product condition", headerFont1));
        PdfPCell headerCell42 = new PdfPCell(new Paragraph("Product color", headerFont1));
        PdfPCell headerCell52 = new PdfPCell(new Paragraph("Product material", headerFont1));
        PdfPCell headerCell62 = new PdfPCell(new Paragraph("Product type", headerFont1));
        PdfPCell headerCell72 = new PdfPCell(new Paragraph("Product country", headerFont1));

        headerCell12.setBackgroundColor(new Color(110, 231, 234, 255));
        headerCell22.setBackgroundColor(new Color(110, 231, 234, 255));
        headerCell32.setBackgroundColor(new Color(110, 231, 234, 255));
        headerCell42.setBackgroundColor(new Color(110, 231, 234, 255));
        headerCell52.setBackgroundColor(new Color(110, 231, 234, 255));
        headerCell62.setBackgroundColor(new Color(110, 231, 234, 255));
        headerCell72.setBackgroundColor(new Color(110, 231, 234, 255));

        reportTable.addCell(headerCell12);
        reportTable.addCell(headerCell22);
        reportTable.addCell(headerCell32);
        reportTable.addCell(headerCell42);
        reportTable.addCell(headerCell52);
        reportTable.addCell(headerCell62);
        reportTable.addCell(headerCell72);

        List<Product> products1 = findbycountryandavailable(country);

        for (Product product : products1) {
            reportTable1.addCell(product.getPrice_usd());
            reportTable1.addCell(product.getProduct_season());
            reportTable1.addCell(product.getProduct_condition());
            reportTable1.addCell(product.getProduct_color());
            reportTable1.addCell(product.getProduct_material());
            reportTable1.addCell(product.getProduct_type());
            reportTable1.addCell(product.getSeller_country());
        }

        document.add(reportTable1);
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("\n"));





        Paragraph title2 = new Paragraph("PROIZVODI KOJI SU TIPA "+producttype+" I KOJI IMAJU CENU IZMEDJU "+minprice+" i "+maxprice, titleFont);
        title2.setAlignment(Element.ALIGN_CENTER);
        document.add(title2);
        document.add(new Paragraph("\n"));

        PdfPTable reportTable2 = new PdfPTable(6);
        reportTable2.setWidthPercentage(100);

        Font headerFont12 = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8, Font.BOLD);
        PdfPCell headerCell123 = new PdfPCell(new Paragraph("Price usd", headerFont12));
        PdfPCell headerCell223 = new PdfPCell(new Paragraph("Product season", headerFont12));
        PdfPCell headerCell323 = new PdfPCell(new Paragraph("Product condition", headerFont12));
        PdfPCell headerCell423 = new PdfPCell(new Paragraph("Product color", headerFont12));
        PdfPCell headerCell523 = new PdfPCell(new Paragraph("Product material", headerFont12));
        PdfPCell headerCell623 = new PdfPCell(new Paragraph("Product type", headerFont12));


        headerCell123.setBackgroundColor(new Color(110, 231, 234, 255));
        headerCell223.setBackgroundColor(new Color(110, 231, 234, 255));
        headerCell323.setBackgroundColor(new Color(110, 231, 234, 255));
        headerCell423.setBackgroundColor(new Color(110, 231, 234, 255));
        headerCell523.setBackgroundColor(new Color(110, 231, 234, 255));
        headerCell623.setBackgroundColor(new Color(110, 231, 234, 255));


        reportTable2.addCell(headerCell123);
        reportTable2.addCell(headerCell223);
        reportTable2.addCell(headerCell323);
        reportTable2.addCell(headerCell423);
        reportTable2.addCell(headerCell523);
        reportTable2.addCell(headerCell623);


        ProductSearchResult products2 = complexquery1(producttype,minprice,maxprice);

        List<Product> produkti = products2.getProducts();

            for (Product product1 : produkti)
            {
                reportTable2.addCell(product1.getPrice_usd());
                reportTable2.addCell(product1.getProduct_season());
                reportTable2.addCell(product1.getProduct_condition());
                reportTable2.addCell(product1.getProduct_color());
                reportTable2.addCell(product1.getProduct_material());
                reportTable2.addCell(product1.getProduct_type());

        }


        document.add(reportTable2);
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("\n"));


        Paragraph title3 = new Paragraph("AGREGACIJE PO BOJI PROIZVODA",titleFont);
        title3.setAlignment(Element.ALIGN_CENTER);
        document.add(title3);
        document.add(new Paragraph("\n"));

        PdfPTable reportTable3 = new PdfPTable(2);
        reportTable3.setWidthPercentage(100);

        Font headerFont1234 = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8, Font.BOLD);
        PdfPCell headerCell1233 = new PdfPCell(new Paragraph("Boja proizvoda", headerFont1234));
        PdfPCell headerCell2233 = new PdfPCell(new Paragraph("Broj proizvoda", headerFont1234));



        headerCell1233.setBackgroundColor(new Color(110, 231, 234, 255));
        headerCell2233.setBackgroundColor(new Color(110, 231, 234, 255));



        reportTable3.addCell(headerCell1233);
        reportTable3.addCell(headerCell2233);





        List<SearchResult> agregacije = products2.getAgregacije();

        for (SearchResult agregacija : agregacije)
        {
            reportTable3.addCell(agregacija.getColor());
            String a  = agregacija.getCount().toString();
            reportTable3.addCell(a);


        }


        document.add(reportTable3);
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("\n"));





        document.close();

        try (FileOutputStream fileOutputStream = new FileOutputStream(filename)) {
            byteArrayOutputStream.writeTo(fileOutputStream);
        }

        return byteArrayOutputStream.toByteArray();
    }







}
