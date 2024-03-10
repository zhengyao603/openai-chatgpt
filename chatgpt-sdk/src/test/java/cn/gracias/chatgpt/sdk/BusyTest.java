package cn.gracias.chatgpt.sdk;

import cn.gracias.chatgpt.sdk.common.Constants;
import cn.gracias.chatgpt.sdk.domain.chat.ChatCompletionRequest;
import cn.gracias.chatgpt.sdk.domain.chat.Message;
import cn.gracias.chatgpt.sdk.session.Configuration;
import cn.gracias.chatgpt.sdk.session.OpenAISession;
import cn.gracias.chatgpt.sdk.session.OpenAISessionFactory;
import cn.gracias.chatgpt.sdk.session.defaults.DefaultOpenAISessionFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

@Slf4j
public class BusyTest {

    private OpenAISession openAiSession;

    @Before
    public void test_OpenAiSessionFactory() {
        // 1. 配置文件 [联系小傅哥获取key]
        // 1.1 官网原始 apiHost https://api.openai.com/ - 官网的Key可直接使用
        // 1.2 三方公司 apiHost https://service-d6wuqy4n-1320869466.cd.apigw.tencentcs.com/ - 需要找我获得 Key 【支持3.5\4.0流式问答模型调用，有些模型已废弃不对接使用】
        Configuration configuration = new Configuration();
        configuration.setApiHost("https://pro-share-aws-api.zcyai.com/");
        configuration.setApiKey("sk-ocdfMcwguqbkyUmR59E5AfD22d0142E2947f916fBe7d4755");
        // 2. 会话工厂
        OpenAISessionFactory factory = new DefaultOpenAISessionFactory(configuration);
        // 3. 开启会话
        this.openAiSession = factory.openSession();
    }

    /**
     * 【常用对话模式，推荐使用此模型进行测试】
     * 此对话模型 3.5/4.0 接近于官网体验 & 流式应答
     */
    @Test
    public void test_chat_completions_stream() throws JsonProcessingException, InterruptedException {
        // 1. 创建参数
        ChatCompletionRequest chatCompletion = ChatCompletionRequest
                .builder()
                .stream(true)
                .messages(Collections.singletonList(Message.builder().role(Constants.Role.USER).content("1+1").build()))
                .model(ChatCompletionRequest.Model.GPT_3_5_TURBO.getCode())
                .maxTokens(1024)
                .build();

        // 2. 发起请求
        EventSource eventSource = openAiSession.chatCompletions(chatCompletion, new EventSourceListener() {
            @Override
            public void onEvent(EventSource eventSource, String id, String type, String data) {
                log.info("测试结果 id:{} type:{} data:{}", id, type, data);
            }

            @Override
            public void onFailure(EventSource eventSource, Throwable t, Response response) {
                log.error("失败 code:{} message:{}", response.code(), response.message());
            }
        });

        // 等待
        new CountDownLatch(1).await();
    }

//    @Test
//    public void test_chat_completions_future() throws JsonProcessingException, InterruptedException, ExecutionException {
//        // 1. 创建参数
//        ChatCompletionRequest chatCompletion = ChatCompletionRequest
//                .builder()
//                .stream(true)
//                .messages(Collections.singletonList(Message.builder().role(Constants.Role.USER).content("java 冒泡排序").build()))
//                .model(ChatCompletionRequest.Model.GPT_3_5_TURBO.getCode())
//                .maxTokens(1024)
//                .build();
//
//        log.info("测试开始：请等待调用结果");
//
//        // 2. 请求应答
//        CompletableFuture<String> future = openAiSession.chatCompletions(chatCompletion);
//
//        log.info("测试结果：{}", future.get());
//    }
//
//    @Test
//    public void test_images() throws InterruptedException {
//        ImageRequest request = ImageRequest.builder()
//                .prompt("画个小猫")
//                .model(ImageRequest.Model.DALL_E_3.getCode())
//                .size(ImageEnum.Size.size_1024.getCode())
//                .build();
//
//        ImageResponse imageResponse = openAiSession.genImages(request);
//
//        log.info("测试结果：{}", JSON.toJSONString(imageResponse.getData()));
//
//        // 等待
//        new CountDownLatch(1).await();
//    }
}

