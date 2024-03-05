package cn.gracias.chatgpt.sdk.session.defaults;

import cn.gracias.chatgpt.sdk.IOpenAIApi;
import cn.gracias.chatgpt.sdk.domain.chat.ChatCompletionRequest;
import cn.gracias.chatgpt.sdk.domain.chat.ChatCompletionResponse;
import cn.gracias.chatgpt.sdk.domain.qa.QACompletionRequest;
import cn.gracias.chatgpt.sdk.domain.qa.QACompletionResponse;
import cn.gracias.chatgpt.sdk.session.OpenAISession;
import io.reactivex.Single;

public class DefaultOpenAISession implements OpenAISession {

    private IOpenAIApi openAIApi;

    public DefaultOpenAISession(IOpenAIApi openAIApi) {
        this.openAIApi = openAIApi;
    }

    @Override
    public QACompletionResponse completions(QACompletionRequest qaCompletionRequest) {
        return this.openAIApi.completions(qaCompletionRequest).blockingGet();
    }

    @Override
    public QACompletionResponse completions(String question) {
        QACompletionRequest request = QACompletionRequest
                .builder()
                .prompt(question)
                .build();
        Single<QACompletionResponse> completions = this.openAIApi.completions(request);
        return completions.blockingGet();
    }

    @Override
    public ChatCompletionResponse completions(ChatCompletionRequest chatCompletionRequest) {
        return this.openAIApi.completions(chatCompletionRequest).blockingGet();
    }
}
