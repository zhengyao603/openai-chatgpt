package cn.gracias.chatgpt.sdk;

import cn.gracias.chatgpt.sdk.common.Constants;
import cn.gracias.chatgpt.sdk.domain.chat.ChatCompletionRequest;
import cn.gracias.chatgpt.sdk.domain.chat.ChatCompletionResponse;
import cn.gracias.chatgpt.sdk.domain.chat.Message;
import cn.gracias.chatgpt.sdk.session.Configuration;
import cn.gracias.chatgpt.sdk.session.OpenAISession;
import cn.gracias.chatgpt.sdk.session.OpenAISessionFactory;
import cn.gracias.chatgpt.sdk.session.defaults.DefaultOpenAISessionFactory;

import java.util.ArrayList;
import java.util.Scanner;

public class ClientTest {

    public static void main(String[] args) throws InterruptedException {
        // 1. 配置文件
        Configuration configuration = new Configuration();
        configuration.setApiHost("https://api.xfg.im/b8b6/");
        configuration.setApiKey("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");
        // 测试时候，需要先获得授权token：http://api.xfg.im:8080/authorize?username=xfg&password=123 - 此地址暂时有效，后续根据课程首页说明获取token；https://t.zsxq.com/0d3o5FKvc
        configuration.setAuthToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ4ZmciLCJleHAiOjE2ODQ2MzEwNjAsImlhdCI6MTY4NDYyNzQ2MCwianRpIjoiMGU2M2Q3NDctNDk1YS00NDU3LTk1ZTAtOWVjMzkwNTlkNmQzIiwidXNlcm5hbWUiOiJ4ZmcifQ.xX4kaw-Pz2Jm4LBSvADzijud4nlNLFQUOaN6UgxrK9E");

        // 2. 会话工厂
        OpenAISessionFactory factory = new DefaultOpenAISessionFactory(configuration);
        OpenAISession openAiSession = factory.openSession();

        System.out.println("我是 OpenAI ChatGPT，请输入你的问题：");

        ChatCompletionRequest chatCompletion = ChatCompletionRequest
                .builder()
                .messages(new ArrayList<>())
                .model(ChatCompletionRequest.Model.GPT_3_5_TURBO.getCode())
                .user("testUser01")
                .build();

        // 3. 等待输入
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String text = scanner.nextLine();
            chatCompletion.getMessages().add(Message.builder().role(Constants.Role.USER).content(text).build());
            ChatCompletionResponse chatCompletionResponse = openAiSession.chatCompletions(chatCompletion);
            chatCompletion.getMessages().add(Message.builder().role(Constants.Role.USER).content(chatCompletionResponse.getChoices().get(0).getMessage().getContent()).build());
            // 输出结果
            System.out.println(chatCompletionResponse.getChoices().get(0).getMessage().getContent());
            System.out.println("请输入你的问题：");
        }

    }

}

