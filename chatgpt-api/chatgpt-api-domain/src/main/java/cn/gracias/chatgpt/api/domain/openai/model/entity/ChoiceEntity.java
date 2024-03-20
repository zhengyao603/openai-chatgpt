package cn.gracias.chatgpt.api.domain.openai.model.entity;

import lombok.Data;

@Data
public class ChoiceEntity {

    /** stream = true 请求参数里返回的属性是 delta */
    private MessageEntity delta;

    /** stream = false 请求参数里返回的属性是 message */
    private MessageEntity message;
}
