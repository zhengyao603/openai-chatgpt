package cn.gracias.chatgpt.sdk.domain.embed;

import cn.gracias.chatgpt.sdk.domain.other.Usage;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class EmbeddingResponse implements Serializable {

    private String object;

    private List<Item> data;

    private String model;

    private Usage usage;
}
