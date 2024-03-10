package cn.gracias.chatgpt.api.domain.receive.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MessageTextEntity {

    @XStreamAlias("MsgId")
    private String msgId;

    @XStreamAlias("ToUserName")
    private String toUserName;

    @XStreamAlias("FromUserName")
    private String fromUserName;

    @XStreamAlias("CreateTime")
    private String createTime;

    @XStreamAlias("MsgType")
    private String msgType;

    @XStreamAlias("Content")
    private String content;

    @XStreamAlias("Event")
    private String event;

    @XStreamAlias("EventKey")
    private String eventKey;
}
