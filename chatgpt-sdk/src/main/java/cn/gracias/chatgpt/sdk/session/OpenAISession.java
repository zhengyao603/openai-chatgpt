package cn.gracias.chatgpt.sdk.session;

import cn.gracias.chatgpt.sdk.domain.chat.ChatCompletionRequest;
import cn.gracias.chatgpt.sdk.domain.chat.ChatCompletionResponse;
import cn.gracias.chatgpt.sdk.domain.qa.QACompletionRequest;
import cn.gracias.chatgpt.sdk.domain.qa.QACompletionResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;

public interface OpenAISession {

    /**
     * 文本问答；简单请求， 24年1月，方法废弃
     * @param question 请求信息
     * @return         返回结果
     */
    QACompletionResponse QACompletions(String question);

    /**
     * 文本问答 24年1月，方法废弃
     * @param qaCompletionRequest 请求信息
     * @return                    返回结果
     */
    QACompletionResponse QACompletions(QACompletionRequest qaCompletionRequest);

    /**
     * 文本问答 & 流式反馈，方法废弃
     * @param qaCompletionRequest 请求信息
     * @param eventSourceListener 实现监听；通过监听的 onEvent 方法接收数据
     */
    EventSource QACompletions(QACompletionRequest qaCompletionRequest, EventSourceListener eventSourceListener) throws JsonProcessingException;


    /**
     * 默认 GPT-3.5 问答模型
     * @param chatCompletionRequest 请求信息
     * @return                      返回结果
     */
    ChatCompletionResponse chatCompletions(ChatCompletionRequest chatCompletionRequest);

    /**
     * 问答模型 GPT-3.5/4.0 流式反馈
     * @param chatCompletionRequest 请求信息
     * @param eventSourceListener 实现监听: 通过监听的 onEvent 方法接收数据
     * @return 返回结果
     */
    EventSource chatCompletions(ChatCompletionRequest chatCompletionRequest, EventSourceListener eventSourceListener) throws JsonProcessingException;
}
