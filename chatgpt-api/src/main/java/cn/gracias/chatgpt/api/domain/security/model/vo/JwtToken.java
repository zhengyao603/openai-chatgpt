package cn.gracias.chatgpt.api.domain.security.model.vo;

import org.apache.shiro.authc.AuthenticationToken;

public class JwtToken implements AuthenticationToken {

    private String jwt;

    public JwtToken(String jwt) {
        this.jwt = jwt;
    }

    /**
     * 等同于账户
     */
    @Override
    public Object getPrincipal() {
        return this.jwt;
    }

    /**
     * 等同于密码
     */
    @Override
    public Object getCredentials() {
        return this.jwt;
    }
}
