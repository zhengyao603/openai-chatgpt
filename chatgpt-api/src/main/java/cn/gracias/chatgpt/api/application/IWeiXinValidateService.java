package cn.gracias.chatgpt.api.application;

public interface IWeiXinValidateService {

    boolean checkSign(String signature, String timestamp, String nonce);
}
