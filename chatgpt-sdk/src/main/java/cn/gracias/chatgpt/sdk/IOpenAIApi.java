package cn.gracias.chatgpt.sdk;

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
import cn.gracias.chatgpt.sdk.domain.images.ImageRequest;
import cn.gracias.chatgpt.sdk.domain.images.ImageResponse;
import cn.gracias.chatgpt.sdk.domain.other.OpenAIResponse;
import cn.gracias.chatgpt.sdk.domain.qa.QACompletionRequest;
import cn.gracias.chatgpt.sdk.domain.qa.QACompletionResponse;
import cn.gracias.chatgpt.sdk.domain.whisper.WhisperResponse;
import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.*;

import java.io.File;
import java.time.LocalDate;
import java.util.Map;

public interface IOpenAIApi {

    String v1_completions = "v1/completions";
    String v1_chat_completions = "v1/chat/completions";
    String v1_edits = "v1/edits";
    String v1_images_generations = "v1/images/generations";
    String v1_images_edits = "v1/images/edits";
    String v1_embeddings = "v1/embeddings";
    String v1_files = "v1/files";
    String v1_files_field_id = "v1/files/{file_id}";
    String v1_files_field_id_content = "v1/files/{file_id}/content";
    String v1_audio_transcriptions = "v1/audio/transcriptions";
    String v1_audio_translations = "v1/audio/translations";
    String v1_dashboard_billing_subscription = "v1/dashboard/billing/subscription";
    String v1_dashboard_billing_usage = "v1/dashboard/billing/usage";

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

    /**
     * 文本修复
     *
     * @param editRequest 请求信息；编辑文本的参数
     * @return 应答结果
     */
    @POST(v1_edits)
    Single<EditResponse> edits(@Body EditRequest editRequest);

    /**
     * 生成图片
     * @param imageRequest 图片对象
     * @return 应答结果
     */
    @POST(v1_images_generations)
    Single<ImageResponse> genImages(@Body ImageRequest imageRequest);

    /**
     * 修改图片
     * @param image          图片对象
     * @param mask           图片对象
     * @param requestBodyMap 请求参数
     * @return 应答结果
     */
    @Multipart
    @POST(v1_images_edits)
    Single<ImageResponse> editImages(@Part MultipartBody.Part image, @Part MultipartBody.Part mask, @PartMap Map<String, RequestBody> requestBodyMap);

    /**
     * 向量计算
     * @param embeddingRequest 请求对象
     * @return 应答结果
     */
    @POST(v1_embeddings)
    Single<EmbeddingResponse> embeddings(@Body EmbeddingRequest embeddingRequest);

    /**
     * 文件列表；在你上传文件到服务端后，可以获取列表信息
     * @return 应答结果
     */
    @GET(v1_files)
    Single<OpenAIResponse<File>> files();

    @Multipart
    @POST(v1_files)
    Single<UploadFileResponse> uploadFile(@Part MultipartBody.Part file, @Part("purpose") RequestBody purpose);

    /**
     * 删除文件
     * @param fileId 文件ID
     * @return 应答结果
     */
    @DELETE(v1_files_field_id)
    Single<DeleteFileResponse> deleteFile(@Path("filed_id") String fieldId);

    /**
     * 检索文件
     * @param fileId 文件ID
     * @return 应答结果
     */
    @GET(v1_files_field_id)
    Single<File> retrieveFile(@Part("filed_id") String fileId);

    /**
     * 检索文件内容信息
     * @param fileId 文件ID
     * @return 应答结果
     */
    @Streaming
    @GET(v1_files_field_id_content)
    Single<ResponseBody> retrieveFileContent(@Path("file_id") String fileId);

    /**
     * 语音转文字
     * @param file           语音文件
     * @param requestBodyMap 请求信息
     * @return 应答结果
     */
    @Multipart
    @POST(v1_audio_transcriptions)
    Single<WhisperResponse> speed2TextTranscriptions(@Part MultipartBody.Part file, @PartMap() Map<String, RequestBody> requestBodyMap);

    /**
     * 语音翻译
     * @param file           语音文件
     * @param requestBodyMap 请求信息
     * @return 应答结果
     */
    @Multipart
    @POST(v1_audio_translations)
    Single<WhisperResponse> speed2TextTranslations(@Part MultipartBody.Part file, @PartMap() Map<String, RequestBody> requestBodyMap);

    /**
     * 账单查询
     * @return 应答结果
     */
    @GET(v1_dashboard_billing_subscription)
    Single<Subscription> subscription();

    /**
     * 消耗查询
     * @param starDate 开始时间
     * @param endDate  结束时间
     * @return  应答数据
     */
    @GET()
    Single<BillingUsage> billingUsage(@Query("start_date") LocalDate starDate, @Query("end_date") LocalDate endDate);
}
