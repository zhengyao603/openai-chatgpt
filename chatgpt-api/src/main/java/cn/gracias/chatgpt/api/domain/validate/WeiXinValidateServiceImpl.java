package cn.gracias.chatgpt.api.domain.validate;

import cn.gracias.chatgpt.api.application.IWeiXinValidateService;
import cn.gracias.chatgpt.api.infrastructure.util.sdk.SignatureUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WeiXinValidateServiceImpl implements IWeiXinValidateService {

    @Value("${wx.config.token}")
    private String token;

    @Override
    public boolean checkSign(String signature, String timestamp, String nonce) {
        return SignatureUtil.check(token, signature, timestamp, nonce);
    }
}
