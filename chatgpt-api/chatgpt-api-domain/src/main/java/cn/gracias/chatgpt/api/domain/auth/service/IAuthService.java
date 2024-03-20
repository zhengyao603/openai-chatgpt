package cn.gracias.chatgpt.api.domain.auth.service;

import cn.gracias.chatgpt.api.domain.auth.model.entity.AuthStateEntity;

public interface IAuthService {

    /**
     * 登录验证
     * @param code 验证码
     * @return Token
     */
    AuthStateEntity doLogin(String code);

    boolean checkToken(String token);
}
