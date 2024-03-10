package cn.gracias.chatgpt.sdk.domain.other;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OpenAIResponse<T> implements Serializable {

    private String object;

    private List<T> data;

    private Error error;

    @Data
    public class Error {
        private String message;
        private String type;
        private String param;
        private String code;
    }
}
