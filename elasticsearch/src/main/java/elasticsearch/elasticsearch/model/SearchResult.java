package elasticsearch.elasticsearch.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class SearchResult {

    private String color;
    private Long count;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public SearchResult() {
    }

    public SearchResult(String color, Long count) {
        this.color = color;
        this.count = count;
    }
}
