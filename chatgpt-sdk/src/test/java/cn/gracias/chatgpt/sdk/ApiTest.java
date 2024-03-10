package cn.gracias.chatgpt.sdk;

import cn.gracias.chatgpt.sdk.common.Constants;
import cn.gracias.chatgpt.sdk.domain.billing.BillingUsage;
import cn.gracias.chatgpt.sdk.domain.billing.Subscription;
import cn.gracias.chatgpt.sdk.domain.chat.ChatCompletionRequest;
import cn.gracias.chatgpt.sdk.domain.chat.ChatCompletionResponse;
import cn.gracias.chatgpt.sdk.domain.chat.Message;
import cn.gracias.chatgpt.sdk.domain.edits.EditRequest;
import cn.gracias.chatgpt.sdk.domain.edits.EditResponse;
import cn.gracias.chatgpt.sdk.domain.embed.EmbeddingResponse;
import cn.gracias.chatgpt.sdk.domain.files.DeleteFileResponse;
import cn.gracias.chatgpt.sdk.domain.files.UploadFileResponse;
import cn.gracias.chatgpt.sdk.domain.images.ImageEnum;
import cn.gracias.chatgpt.sdk.domain.images.ImageRequest;
import cn.gracias.chatgpt.sdk.domain.images.ImageResponse;
import cn.gracias.chatgpt.sdk.domain.other.OpenAIResponse;
import cn.gracias.chatgpt.sdk.domain.qa.QACompletionRequest;
import cn.gracias.chatgpt.sdk.domain.qa.QACompletionResponse;
import cn.gracias.chatgpt.sdk.session.Configuration;
import cn.gracias.chatgpt.sdk.session.OpenAISession;
import cn.gracias.chatgpt.sdk.session.OpenAISessionFactory;
import cn.gracias.chatgpt.sdk.session.defaults.DefaultOpenAISessionFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;


@Slf4j
public class ApiTest {

    private OpenAISession openAiSession;

    @Before
    public void test_OpenAiSessionFactory() {
        // 1. 配置文件
        Configuration configuration = new Configuration();
        configuration.setApiHost("https://pro-share-aws-api.zcyai.com/");
        // 可以根据课程首页评论置顶说明获取 apihost、apikey；https://t.zsxq.com/0d3o5FKvc
        configuration.setApiKey("sk-ocdfMcwguqbkyUmR59E5AfD22d0142E2947f916fBe7d4755");

        // 如果需要做用户验证的话
//        configuration.setAuthToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ4ZmciLCJleHAiOjE2ODMyODE2NzEsImlhdCI6MTY4MzI3ODA3MSwianRpIjoiMWUzZTkwYjYtY2UyNy00NzNlLTk5ZTYtYWQzMWU1MGVkNWE4IiwidXNlcm5hbWUiOiJ4ZmcifQ.YgQRJ2U5-9uydtd6Wbkg2YatsoX-y8mS_OJ3FdNRaX0");

        // 2. 会话工厂
        OpenAISessionFactory factory = new DefaultOpenAISessionFactory(configuration);
        // 3. 开启会话
        this.openAiSession = factory.openSession();
    }

    /**
     * 简单问答模式，方法废弃。
     * 推荐使用 test_chat_completions
     */
    @Deprecated
    public void test_qa_completions() throws JsonProcessingException {
        QACompletionResponse response01 = openAiSession.QACompletions("写个java冒泡排序");
        log.info("测试结果：{}", new ObjectMapper().writeValueAsString(response01.getChoices()));
    }

    /**
     * 简单问答模式 * 流式应答。
     * 方法弃用
     */
    @Deprecated
    public void test_qa_completions_stream() throws JsonProcessingException, InterruptedException {
        // 1. 创建参数
        QACompletionRequest request = QACompletionRequest
                .builder()
                .prompt("写个java冒泡排序")
                .stream(true)
                .build();

        for (int i = 0; i < 1; i++) {
            // 2. 发起请求
            EventSource eventSource = openAiSession.QACompletions(request, new EventSourceListener() {
                @Override
                public void onEvent(EventSource eventSource, String id, String type, String data) {
                    log.info("测试结果：{}", data);
                }
            });
        }

        // 等待
        new CountDownLatch(1).await();
    }

    /**
     * 此对话模型 3.5 接近于官网体验
     */
    @Test
    public void test_chat_completions() {
        // 1. 创建参数
        ChatCompletionRequest chatCompletion = ChatCompletionRequest
                .builder()
                .messages(Collections.singletonList(Message.builder().role(Constants.Role.USER).content("写一个java冒泡排序").build()))
                .model(ChatCompletionRequest.Model.GPT_3_5_TURBO.getCode())
                .build();
        // 2. 发起请求
        ChatCompletionResponse chatCompletionResponse = openAiSession.chatCompletions(chatCompletion);
        // 3. 解析结果
        chatCompletionResponse.getChoices().forEach(e -> {
            log.info("测试结果：{}", e.getMessage());
        });
    }

    /**
     * 此对话模型 3.5 接近于官网体验 & 流式应答
     */
    @Test
    public void test_chat_completions_stream() throws JsonProcessingException, InterruptedException {
        // 1. 创建参数
        ChatCompletionRequest chatCompletion = ChatCompletionRequest
                .builder()
                .stream(true)
                .messages(Collections.singletonList(Message.builder().role(Constants.Role.USER).content("写一个java冒泡排序").build()))
                .model(ChatCompletionRequest.Model.GPT_3_5_TURBO.getCode())
                .build();
        // 2. 发起请求
        EventSource eventSource = openAiSession.chatCompletions(chatCompletion, new EventSourceListener() {
            @Override
            public void onEvent(EventSource eventSource, String id, String type, String data) {
                log.info("测试结果：{}", data);
            }
        });
        // 等待
        new CountDownLatch(1).await();
    }

    /**
     * 文本修复
     */
    @Test
    public void test_edit() {
        // 文本请求
        EditRequest textRequest = EditRequest.builder()
                .input("码农会锁")
                .instruction("帮我修改错字")
                .model(EditRequest.Model.TEXT_DAVINCI_EDIT_001.getCode()).build();
        EditResponse textResponse = openAiSession.edit(textRequest);
        log.info("测试结果：{}", textResponse);

        // 代码请求
        EditRequest codeRequest = EditRequest.builder()
                // j <= 10 应该修改为 i <= 10
                .input("for (int i = 1; j <= 10; i++) {\n" +
                        "    System.out.println(i);\n" +
                        "}")
                .instruction("这段代码执行时报错，请帮我修改").model(EditRequest.Model.CODE_DAVINCI_EDIT_001.getCode()).build();
        EditResponse codeResponse = openAiSession.edit(codeRequest);
        log.info("测试结果：{}", codeResponse);
    }

    /**
     * 生成图片
     */
    @Test
    public void test_genImages() {
        // 方式1，简单调用
        ImageResponse imageResponse01 = openAiSession.genImages("画一个996加班的程序员");
        log.info("测试结果：{}", imageResponse01);

        // 方式2，调参调用
        ImageResponse imageResponse02 = openAiSession.genImages(ImageRequest.builder()
                .prompt("画一个996加班的程序员")
                .size(ImageEnum.Size.size_256.getCode())
                .responseFormat(ImageEnum.ResponseFormat.B64_JSON.getCode()).build());
        log.info("测试结果：{}", imageResponse02);
    }

    /**
     * 修改图片，有3个方法，入参不同。
     */
    @Test
    public void test_editImages() throws IOException {
        ImageResponse imageResponse = openAiSession.editImages(new File(""), "去除图片中的文字");
        log.info("测试结果：{}", imageResponse);
    }

    @Test
    public void test_embeddings() {
        EmbeddingResponse embeddingResponse = openAiSession.embeddings("哈喽", "嗨", "hi!");
        log.info("测试结果：{}", embeddingResponse);
    }

    @Test
    public void test_files() {
        OpenAIResponse<File> openAiResponse = openAiSession.files();
        log.info("测试结果：{}", openAiResponse);
    }

    @Test
    public void test_uploadFile() {
        UploadFileResponse uploadFileResponse = openAiSession.uploadFile(new File("/Users/fuzhengwei/1024/KnowledgePlanet/chatgpt/chatgpt-sdk-java/docs/files/introduce.md"));
        log.info("测试结果：{}", uploadFileResponse);
    }

    @Test
    public void test_deleteFile() {
        DeleteFileResponse deleteFileResponse = openAiSession.deleteFile("file id 上传后才能获得");
        log.info("测试结果：{}", deleteFileResponse);
    }

    @Test
    public void test_subscription() {
        Subscription subscription = openAiSession.subscription();
        log.info("测试结果：{}", subscription);
    }

    @Test
    public void test_billingUsage() {
        BillingUsage billingUsage = openAiSession.billingUsage(LocalDate.of(2023, 3, 20), LocalDate.now());
        log.info("测试结果：{}", billingUsage.getTotalUsage());
    }
}
