package cn.gracias.chatgpt.api.domain.receive.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class BehaviorMatter {

    private String openId;

    private String fromUserName;

    private String msgType;

    private String content;

    private String event;

    private Date createTime;
}
