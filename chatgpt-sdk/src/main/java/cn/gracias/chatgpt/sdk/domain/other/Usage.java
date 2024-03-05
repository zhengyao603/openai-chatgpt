package cn.gracias.chatgpt.sdk.domain.other;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class Usage implements Serializable {

    // 提示令牌
    @JsonProperty("prompt_tokens")
    private long promptTokens;

    // 完成令牌
    @JsonProperty("completion_tokens")
    private long completionTokens;

    // 总量令牌
    @JsonProperty("total_tokens")
    private long totalTokens;
}
