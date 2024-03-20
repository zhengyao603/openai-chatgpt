package cn.gracias.chatgpt.api.config;

import cn.gracias.chatgpt.sdk.session.OpenAISession;
import cn.gracias.chatgpt.sdk.session.OpenAISessionFactory;
import cn.gracias.chatgpt.sdk.session.defaults.DefaultOpenAISessionFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ChatGPTSDKConfigProperties.class)
public class ChatGPTSDKConfig {

    @Bean
    public OpenAISession openAISession(ChatGPTSDKConfigProperties properties) {
        // 1. 配置文件
        cn.gracias.chatgpt.sdk.session.Configuration configuration = new cn.gracias.chatgpt.sdk.session.Configuration();
        configuration.setApiHost(properties.getApiHost());
        configuration.setApiKey(properties.getApiKey());
        configuration.setAuthToken(properties.getAuthToken());

        // 2. 会话工厂
        OpenAISessionFactory factory = new DefaultOpenAISessionFactory(configuration);

        // 3. 开启会话
        return factory.openSession();
    }
}
