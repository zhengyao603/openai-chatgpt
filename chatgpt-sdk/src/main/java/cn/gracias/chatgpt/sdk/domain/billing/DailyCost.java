package cn.gracias.chatgpt.sdk.domain.billing;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class DailyCost {

    @JsonProperty("timestamp")
    private long timestamp;

    @JsonProperty("line_items")
    private List<LineItem> lineItems;

}
