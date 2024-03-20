package cn.gracias.chatgpt.api.domain.weixin.service;

import cn.gracias.chatgpt.api.domain.weixin.model.entity.UserBehaviorMessageEntity;

public interface IWeiXinBehaviorService {

    String acceptUserBehavior(UserBehaviorMessageEntity userBehaviorMessageEntity);
}
