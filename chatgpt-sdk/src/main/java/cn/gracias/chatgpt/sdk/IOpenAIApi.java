package cn.gracias.chatgpt.sdk;

import cn.gracias.chatgpt.sdk.domain.chat.ChatCompletionRequest;
import cn.gracias.chatgpt.sdk.domain.chat.ChatCompletionResponse;
import cn.gracias.chatgpt.sdk.domain.qa.QACompletionRequest;
import cn.gracias.chatgpt.sdk.domain.qa.QACompletionResponse;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IOpenAIApi {

    String v1_completions = "v1/completions";
    String v1_chat_completions = "v1/chat/completions";

    /**
     * 文本问答，方法废弃
     * @param qaCompletionRequest 请求信息
     * @return                    返回结果
     */
    @POST(v1_completions)
    Single<QACompletionResponse> completions(@Body QACompletionRequest qaCompletionRequest);

    /**
     * 默认 GPT-3.5 问答模型
     * @param chatCompletionRequest 请求信息
     * @return                      返回结果
     */
    @POST(v1_chat_completions)
    Single<ChatCompletionResponse> completions(@Body ChatCompletionRequest chatCompletionRequest);
}
