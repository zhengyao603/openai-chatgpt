package cn.gracias.chatgpt.sdk.session;

import cn.gracias.chatgpt.sdk.IOpenAIApi;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSources;
import org.jetbrains.annotations.NotNull;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Configuration {

    private IOpenAIApi openAIApi;

    private OkHttpClient okHttpClient;

    @NotNull
    private String apiKey;

    private String apiHost;

    private String authToken;

    public EventSource.Factory createRequestFactory() {
        return EventSources.createFactory(okHttpClient);
    }
}
