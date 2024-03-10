package cn.gracias.chatgpt.sdk;

import cn.gracias.chatgpt.sdk.common.Constants;
import cn.gracias.chatgpt.sdk.domain.chat.ChatCompletionRequest;
import cn.gracias.chatgpt.sdk.domain.chat.ChatCompletionResponse;
import cn.gracias.chatgpt.sdk.domain.chat.Message;
import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.Single;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;
import org.junit.Test;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.Collections;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class HTTPClientTest {

    @Test
    public void test_client() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient
                .Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(chain -> {
                    Request original = chain.request();

                    // 从请求中获取 token 参数，并将其添加到请求路径中
                    HttpUrl url = original.url().newBuilder()
//                            .addQueryParameter("token", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ4ZmciLCJleHAiOjE2ODMyNzIyMjAsImlhdCI6MTY4MzI2ODYyMCwianRpIjoiOTkwMmM")
                            .build();

                    Request request = original.newBuilder()
                            .url(url)
                            .header(Header.AUTHORIZATION.getValue(), "Bearer" + "sk-ocdfMcwguqbkyUmR59E5AfD22d0142E2947f916fBe7d4755")
                            .header(Header.CONTENT_TYPE.getValue(), ContentType.JSON.getValue())
                            .method(original.method(), original.body())
                            .build();
                    return chain.proceed(request);
                }).build();

        IOpenAIApi openAIApi = new Retrofit.Builder()
                .baseUrl("https://pro-share-aws-api.zcyai.com/")
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .build().create(IOpenAIApi.class);

        Message message = Message.builder().role(Constants.Role.USER).content("写一个冒泡排序").build();
        ChatCompletionRequest chatCompletion = ChatCompletionRequest
                .builder()
                .messages(Collections.singletonList(message))
                .model(ChatCompletionRequest.Model.GPT_4.getCode())
                .stream(true)
                .build();

        Single<ChatCompletionResponse> chatCompletionResponseSingle = openAIApi.completions(chatCompletion);
        ChatCompletionResponse chatCompletionResponse = chatCompletionResponseSingle.blockingGet();

        chatCompletionResponse.getChoices().forEach(e -> {
            System.out.println(e.getMessage());
        });
    }

    @Test
    public void test_client_stream() throws JsonProcessingException, InterruptedException {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient
                .Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(chain -> {
                    Request original = chain.request();

                    // 从请求中获取 token 参数，并将其添加到请求路径中
                    HttpUrl url = original.url().newBuilder()
//                            .addQueryParameter("token", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ4ZmciLCJleHAiOjE2ODM5NDU3NDcsImlhdCI6MTY4Mzk0MjE0NywianRpIjoiM2QyMDExMTYtNmVjMS00Y2UzLWJhYzgtYzYxYmVmN2ZmNWE5IiwidXNlcm5hbWUiOiJ4ZmcifQ.3FDvUNuNoGemKLhcgagy8WH7xHwRU37t--BuH0N9skg")
                            .build();

                    Request request = original.newBuilder()
                            .url(url)
                            .header(Header.AUTHORIZATION.getValue(), "Bearer" + "sk-ocdfMcwguqbkyUmR59E5AfD22d0142E2947f916fBe7d4755")
                            .header(Header.CONTENT_TYPE.getValue(), ContentType.JSON.getValue())
                            .method(original.method(), original.body())
                            .build();
                    return chain.proceed(request);
                }).build();

        Message message = Message.builder().role(Constants.Role.USER).content("1+1=?").build();
        ChatCompletionRequest chatCompletion = ChatCompletionRequest
                .builder()
                .messages(Collections.singletonList(message))
                .model(ChatCompletionRequest.Model.GPT_4.getCode())
                .stream(true)
                .build();

        EventSource.Factory factory = EventSources.createFactory(okHttpClient);
        String requestBody = new ObjectMapper().writeValueAsString(chatCompletion);

        Request request = new Request.Builder()
                .url("https://pro-share-aws-api.zcyai.com/v1/chat/completions")
                .post(RequestBody.create(MediaType.parse(ContentType.JSON.getValue()), requestBody))
                .build();

        EventSource eventSource = factory.newEventSource(request, new EventSourceListener() {
            @Override
            public void onEvent(EventSource eventSource, String id, String type, String data) {
                log.info("测试结果: {}", data);
            }
        });

        // 等待
        new CountDownLatch(1).await();
    }
}
