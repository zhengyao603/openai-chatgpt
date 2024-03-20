package cn.gracias.chatgpt.api.domain.weixin.service;

public interface IWeiXinValidateService {

    boolean checkSign(String signature, String timestamp, String nonce);
}
