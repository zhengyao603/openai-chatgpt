package cn.gracias.chatgpt.sdk.domain.chat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@Builder
@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class ChatCompletionRequest implements Serializable {

    // 默认类型
    private String model = Model.GPT_3_5_TURBO.getCode();

    // 问题描述
    private List<Message> messages;

    // 控制温度【随机性】：0-2之间，较高的值输出更加随机，较低的值输出更加集中
    private double temperature = 0.2;

    // 多样性控制：使用温度采样的替代方法成为核心采样，其中模型考虑具有top_p概率质量的令牌的结果。因此，0.1 意味着只考虑包含前 10% 概率质量的代币
    @JsonProperty("top_p")
    private Double topP = 1d;

    // 为每个提示生成的完成次数
    private Integer n = 1;

    // 是否为流式输出
    private boolean stream = false;

    // 停止输出标识
    private List<String> stop;

    // 输出字符串限制：0-4096
    @JsonProperty("max_tokens")
    private Integer maxTokens = 2048;

    // 频率惩罚：降低模型重复同一行的可能性
    @JsonProperty("frequency_penalty")
    private double frequencyPenalty = 0;

    // 存在惩罚：增强模型谈论新话题的可能性
    @JsonProperty("presence_penalty")
    private double presencePenalty = 0;

    // 生成多个调用结果，只显示最佳的。这样会消耗更多的 api token
    @JsonProperty("logit_bias")
    private Map logitBias;

    // 调用表示，避免重复调用
    private String user;

    @Getter
    @AllArgsConstructor
    public enum Model {
        // gpt-3.5-turbo
        GPT_3_5_TURBO("gpt-3.5-turbo"),
        // gpt4.0
        GPT_4("gpt-4"),
        // gpt4.0 超长上下文
        GPT_4_32("gpt-4-32k"),
        ;

        private String code;
    }
}
