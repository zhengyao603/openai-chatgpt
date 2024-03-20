package cn.gracias.chatgpt.api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "chatgpt.sdk.config", ignoreInvalidFields = true)
public class ChatGPTSDKConfigProperties {

    /** APi Host */
    private String apiHost;

    /** API Key */
    private String apiKey;

    /** 获取Token */
    private String authToken;
}
