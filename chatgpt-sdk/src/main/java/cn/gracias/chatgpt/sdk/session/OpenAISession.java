package cn.gracias.chatgpt.sdk.session;

import cn.gracias.chatgpt.sdk.domain.billing.BillingUsage;
import cn.gracias.chatgpt.sdk.domain.billing.Subscription;
import cn.gracias.chatgpt.sdk.domain.chat.ChatCompletionRequest;
import cn.gracias.chatgpt.sdk.domain.chat.ChatCompletionResponse;
import cn.gracias.chatgpt.sdk.domain.edits.EditRequest;
import cn.gracias.chatgpt.sdk.domain.edits.EditResponse;
import cn.gracias.chatgpt.sdk.domain.embed.EmbeddingRequest;
import cn.gracias.chatgpt.sdk.domain.embed.EmbeddingResponse;
import cn.gracias.chatgpt.sdk.domain.files.DeleteFileResponse;
import cn.gracias.chatgpt.sdk.domain.files.UploadFileResponse;
import cn.gracias.chatgpt.sdk.domain.images.ImageEditRequest;
import cn.gracias.chatgpt.sdk.domain.images.ImageRequest;
import cn.gracias.chatgpt.sdk.domain.images.ImageResponse;
import cn.gracias.chatgpt.sdk.domain.other.OpenAIResponse;
import cn.gracias.chatgpt.sdk.domain.qa.QACompletionRequest;
import cn.gracias.chatgpt.sdk.domain.qa.QACompletionResponse;
import cn.gracias.chatgpt.sdk.domain.whisper.TranscriptionRequest;
import cn.gracias.chatgpt.sdk.domain.whisper.TranslationRequest;
import cn.gracias.chatgpt.sdk.domain.whisper.WhisperResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

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

    /**
     * 文本修复
     * @param editRequest 请求信息
     * @return 应答结果
     */
    EditResponse edit(EditRequest editRequest);

    /**
     * 生成图片
     * @param prompt 图片描述
     * @return 应答结果
     */
    ImageResponse genImages(String prompt);

    /**
     * 生成图片
     * @param imageRequest 图片描述
     * @return 应答结果
     */
    ImageResponse genImages(ImageRequest imageRequest);

    /**
     * 修改图片
     * @param image  图片对象
     * @param prompt 修改描述
     * @return 应答结果
     */
    ImageResponse editImages(File image, String prompt);

    /**
     * 修改图片
     * @param image            图片对象
     * @param imageEditRequest 图片参数
     * @return 应答结果
     */
    ImageResponse editImages(File image, ImageEditRequest imageEditRequest);

    /**
     * 修改图片
     * @param image            图片对象，小于4M的PNG图片
     * @param mask             图片对象，小于4M的PNG图片
     * @param imageEditRequest 图片参数
     * @return 应答结果
     */
    ImageResponse editImages(File image, File mask, ImageEditRequest imageEditRequest);

    /**
     * 向量计算；单个文本
     * 文本向量计算是一种在自然语言处理（NLP）领域中用于测量和比较文本相似性的技术。在这种方法中，每个单词或短语都被转换为一个向量，可以使用这些向量来比较不同文本之间的相似性，并在需要时进行推荐或分类
     * @param input 文本信息
     * @return 应答结果
     */
    EmbeddingResponse embeddings(String input);

    /**
     * 向量计算；多个文本
     * 文本向量计算是一种在自然语言处理（NLP）领域中用于测量和比较文本相似性的技术。在这种方法中，每个单词或短语都被转换为一个向量，可以使用这些向量来比较不同文本之间的相似性，并在需要时进行推荐或分类
     * @param inputs 多个文本
     * @return 应答结果
     */
    EmbeddingResponse embeddings(String... inputs);

    /**
     * 向量计算；多个文本
     * 文本向量计算是一种在自然语言处理（NLP）领域中用于测量和比较文本相似性的技术。在这种方法中，每个单词或短语都被转换为一个向量，可以使用这些向量来比较不同文本之间的相似性，并在需要时进行推荐或分类
     * @param inputs 多个文本
     * @return 应答结果
     */
    EmbeddingResponse embeddings(List<String> inputs);

    /**
     * 向量计算；入参
     * 文本向量计算是一种在自然语言处理（NLP）领域中用于测量和比较文本相似性的技术。在这种方法中，每个单词或短语都被转换为一个向量，可以使用这些向量来比较不同文本之间的相似性，并在需要时进行推荐或分类
     * @param embeddingRequest 请求结果
     * @return 应答结果
     */
    EmbeddingResponse embeddings(EmbeddingRequest embeddingRequest);

    /**
     * 获取文件
     * @return 应答结果
     */
    OpenAIResponse<File> files();

    /**
     * 上传文件
     * @param file 文件
     * @return 应答结果
     */
    UploadFileResponse uploadFile(File file);

    /**
     * 上传文件
     * @param purpose Use "fine-tune" for Fine-tuning. This allows us to validate the format of the uploaded file.
     * @param file    文件
     * @return 应答结果
     */
    UploadFileResponse uploadFile(String purpose, File file);

    /**
     * 删除文件
     * @param fileId 文件ID
     * @return 应答结果
     */
    DeleteFileResponse deleteFile(String fileId);

    /**
     * 语音转文字
     * @param file                  语音文件
     * @param transcriptionsRequest 请求信息
     * @return 应答结果
     */
    WhisperResponse speed2TextTranscriptions(File file, TranscriptionRequest transcriptionsRequest);

    /**
     * 语音翻译
     * @param file                语音文件
     * @param translationsRequest 请求信息
     * @return 应答结果
     */
    WhisperResponse speed2TextTranslations(File file, TranslationRequest translationsRequest);

    /**
     * 账单查询
     * @return 应答结果
     */
    Subscription subscription();

    /**
     * 消耗查询
     * @param starDate 开始时间
     * @param endDate  结束时间
     * @return  应答数据
     */
    BillingUsage billingUsage(@NotNull LocalDate starDate, @NotNull LocalDate endDate);
}
