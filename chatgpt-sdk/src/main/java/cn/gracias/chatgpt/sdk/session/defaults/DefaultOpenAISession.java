package cn.gracias.chatgpt.sdk.session.defaults;

import cn.gracias.chatgpt.sdk.IOpenAIApi;
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
import cn.gracias.chatgpt.sdk.session.Configuration;
import cn.gracias.chatgpt.sdk.session.OpenAISession;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.Single;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.time.LocalDate;
import java.util.*;

public class DefaultOpenAISession implements OpenAISession {

    // 配置信息
    private final Configuration configuration;

    // OpenAI 接口
    private final IOpenAIApi openAIApi;

    // 工厂事件
    private final EventSource.Factory factory;

    public DefaultOpenAISession(Configuration configuration) {
        this.configuration = configuration;
        this.openAIApi = configuration.getOpenAIApi();
        this.factory = configuration.createRequestFactory();
    }

    @Override
    public QACompletionResponse QACompletions(String question) {
        QACompletionRequest request = QACompletionRequest
                .builder()
                .prompt(question)
                .build();
        Single<QACompletionResponse> completions = this.openAIApi.completions(request);
        return completions.blockingGet();
    }

    @Override
    public QACompletionResponse QACompletions(QACompletionRequest qaCompletionRequest) {
        return this.openAIApi.completions(qaCompletionRequest).blockingGet();
    }

    @Override
    public EventSource QACompletions(QACompletionRequest qaCompletionRequest, EventSourceListener eventSourceListener) throws JsonProcessingException {
        // 核心参数校验；不对用户的传参做更改，只返回错误信息
        if (!qaCompletionRequest.isStream()){
            throw new RuntimeException("illegal parameter stream is false!");
        }

        // 构建请求信息
        Request request = new Request.Builder()
                .url(configuration.getApiHost().concat(IOpenAIApi.v1_completions))
                .post(RequestBody.create(MediaType.parse(ContentType.JSON.getValue()), new ObjectMapper().writeValueAsBytes(qaCompletionRequest)))
                .build();

        // 返回事件结果
        return factory.newEventSource(request, eventSourceListener);
    }

    @Override
    public ChatCompletionResponse chatCompletions(ChatCompletionRequest chatCompletionRequest) {
        return this.openAIApi.completions(chatCompletionRequest).blockingGet();
    }

    @Override
    public EventSource chatCompletions(ChatCompletionRequest chatCompletionRequest, EventSourceListener eventSourceListener) throws JsonProcessingException {
        // 核心参数校验；不对用户的传参做更改，只返回错误信息。
        if (!chatCompletionRequest.isStream()) {
            throw new RuntimeException("illegal parameter stream is false!");
        }

        // 构建请求信息
        Request request = new Request.Builder()
                // url: https://api.openai.com/v1/chat/completions - 通过 IOpenAiApi 配置的 POST 接口，用这样的方式从统一的地方获取配置信息
                .url(configuration.getApiHost().concat(IOpenAIApi.v1_chat_completions))
                // 封装请求参数信息，如果使用了 Fastjson 也可以替换 ObjectMapper 转换对象
                .post(RequestBody.create(MediaType.parse(ContentType.JSON.getValue()), new ObjectMapper().writeValueAsString(chatCompletionRequest)))
                .build();

        // 返回结果信息；EventSource 对象可以取消应答
        return factory.newEventSource(request, eventSourceListener);
    }

    @Override
    public EditResponse edit(EditRequest editRequest) {
        return this.openAIApi.edits(editRequest).blockingGet();
    }

    @Override
    public ImageResponse genImages(String prompt) {
        ImageRequest imageRequest = ImageRequest.builder().prompt(prompt).build();
        return this.genImages(imageRequest);
    }

    @Override
    public ImageResponse genImages(ImageRequest imageRequest) {
        return this.openAIApi.genImages(imageRequest).blockingGet();
    }

    @Override
    public ImageResponse editImages(File image, String prompt) {
        ImageEditRequest imageEditRequest = ImageEditRequest.builder().prompt(prompt).build();
        return this.editImages(image, null, imageEditRequest);
    }

    @Override
    public ImageResponse editImages(File image, ImageEditRequest imageEditRequest) {
        return this.editImages(image, null, imageEditRequest);
    }

    @Override
    public ImageResponse editImages(File image, File mask, ImageEditRequest imageEditRequest) {
        // 1. imageMultipartBody
        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), image);
        MultipartBody.Part imageMultipartBody = MultipartBody.Part.createFormData("image", image.getName(), imageBody);
        // 2. maskMultipartBody
        MultipartBody.Part maskMultipartBody = null;
        if (Objects.nonNull(mask)) {
            RequestBody maskBody = RequestBody.create(MediaType.parse("multipart/form-data"), mask);
            maskMultipartBody = MultipartBody.Part.createFormData("mask", mask.getName(), maskBody);
        }
        // requestBodyMap
        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        requestBodyMap.put("prompt", RequestBody.create(MediaType.parse("multipart/form-data"), imageEditRequest.getPrompt()));
        requestBodyMap.put("n", RequestBody.create(MediaType.parse("multipart/form-data"), imageEditRequest.getN().toString()));
        requestBodyMap.put("size", RequestBody.create(MediaType.parse("multipart/form-data"), imageEditRequest.getSize()));
        requestBodyMap.put("response_format", RequestBody.create(MediaType.parse("multipart/form-data"), imageEditRequest.getResponseFormat()));
        if (!(Objects.isNull(imageEditRequest.getUser()) || "".equals(imageEditRequest.getUser()))) {
            requestBodyMap.put("user", RequestBody.create(MediaType.parse("multipart/form-data"), imageEditRequest.getUser()));
        }
        return this.openAIApi.editImages(imageMultipartBody, maskMultipartBody, requestBodyMap).blockingGet();
    }

    @Override
    public EmbeddingResponse embeddings(String input) {
        EmbeddingRequest embeddingRequest = EmbeddingRequest.builder().input(new ArrayList<String>() {{
            add(input);
        }}).build();
        return this.embeddings(embeddingRequest);
    }

    @Override
    public EmbeddingResponse embeddings(String... inputs) {
        EmbeddingRequest embeddingRequest = EmbeddingRequest.builder().input(Arrays.asList(inputs)).build();
        return this.embeddings(embeddingRequest);
    }

    @Override
    public EmbeddingResponse embeddings(List<String> inputs) {
        EmbeddingRequest embeddingRequest = EmbeddingRequest.builder().input(inputs).build();
        return this.embeddings(embeddingRequest);
    }

    @Override
    public EmbeddingResponse embeddings(EmbeddingRequest embeddingRequest) {
        return this.openAIApi.embeddings(embeddingRequest).blockingGet();
    }

    @Override
    public OpenAIResponse<File> files() {
        return this.openAIApi.files().blockingGet();
    }

    @Override
    public UploadFileResponse uploadFile(File file) {
        return this.uploadFile("fine-tune", file);
    }

    @Override
    public UploadFileResponse uploadFile(String purpose, File file) {
        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("file", file.getName(), fileBody);
        RequestBody purposeBody = RequestBody.create(MediaType.parse("multipart/form-data"), purpose);
        return this.openAIApi.uploadFile(multipartBody, purposeBody).blockingGet();
    }

    @Override
    public DeleteFileResponse deleteFile(String fileId) {
        return this.openAIApi.deleteFile(fileId).blockingGet();
    }

    @Override
    public WhisperResponse speed2TextTranscriptions(File file, TranscriptionRequest transcriptionRequest) {
        // 1. 语音文件
        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("file", file.getName(), fileBody);
        // 2. 参数封装
        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        if (StrUtil.isNotBlank(transcriptionRequest.getLanguage())) {
            requestBodyMap.put(TranscriptionRequest.Fields.language, RequestBody.create(MediaType.parse("multipart/form-data"), transcriptionRequest.getLanguage()));
        }
        if (StrUtil.isNotBlank(transcriptionRequest.getModel())) {
            requestBodyMap.put(TranscriptionRequest.Fields.model, RequestBody.create(MediaType.parse("multipart/form-data"), transcriptionRequest.getModel()));
        }
        if (StrUtil.isNotBlank(transcriptionRequest.getPrompt())) {
            requestBodyMap.put(TranscriptionRequest.Fields.prompt, RequestBody.create(MediaType.parse("multipart/form-data"), transcriptionRequest.getPrompt()));
        }
        if (StrUtil.isNotBlank(transcriptionRequest.getResponseFormat())) {
            requestBodyMap.put(TranscriptionRequest.Fields.responseFormat, RequestBody.create(MediaType.parse("multipart/form-data"), transcriptionRequest.getResponseFormat()));
        }
        requestBodyMap.put(TranscriptionRequest.Fields.temperature, RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(transcriptionRequest.getTemperature())));
        return this.openAIApi.speed2TextTranscriptions(multipartBody, requestBodyMap).blockingGet();
    }

    @Override
    public WhisperResponse speed2TextTranslations(File file, TranslationRequest translationsRequest) {
        // 1. 语音文件
        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("file", file.getName(), fileBody);
        // 2. 参数封装
        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        if (StrUtil.isNotBlank(translationsRequest.getModel())) {
            requestBodyMap.put(TranscriptionRequest.Fields.model, RequestBody.create(MediaType.parse("multipart/form-data"), translationsRequest.getModel()));
        }
        if (StrUtil.isNotBlank(translationsRequest.getPrompt())) {
            requestBodyMap.put(TranscriptionRequest.Fields.prompt, RequestBody.create(MediaType.parse("multipart/form-data"), translationsRequest.getPrompt()));
        }
        if (StrUtil.isNotBlank(translationsRequest.getResponseFormat())) {
            requestBodyMap.put(TranscriptionRequest.Fields.responseFormat, RequestBody.create(MediaType.parse("multipart/form-data"), translationsRequest.getResponseFormat()));
        }
        requestBodyMap.put(TranslationRequest.Fields.temperature, RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(translationsRequest.getTemperature())));
        requestBodyMap.put(TranscriptionRequest.Fields.temperature, RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(translationsRequest.getTemperature())));
        return this.openAIApi.speed2TextTranscriptions(multipartBody, requestBodyMap).blockingGet();
    }

    @Override
    public Subscription subscription() {
        return this.openAIApi.subscription().blockingGet();
    }

    @Override
    public BillingUsage billingUsage(@NotNull LocalDate starDate, @NotNull LocalDate endDate) {
        return this.openAIApi.billingUsage(starDate, endDate).blockingGet();
    }

}
