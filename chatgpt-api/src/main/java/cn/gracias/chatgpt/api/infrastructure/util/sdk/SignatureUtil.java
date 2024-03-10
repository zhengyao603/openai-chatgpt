package cn.gracias.chatgpt.api.infrastructure.util.sdk;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class SignatureUtil {

    /**
     * 校验签名
     * @param signature 签名
     * @param timestamp 时间戳
     * @param nonce 随机数
     * @return 布尔值
     */
    public static boolean check(String token, String signature, String timestamp, String nonce) {
        String[] arr = new String[]{token, timestamp, nonce};
        // 将 token，timestamp，nonce 三个参数进行字典排序
        Arrays.sort(arr);
        StringBuilder content = new StringBuilder();
        for (String s : arr) {
            content.append(s);
        }

        MessageDigest md;
        String tmpStr = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
            // 将三个参数字符串拼接成一个字符串后进行sha1加密
            byte[] digest = md.digest(content.toString().getBytes());
            tmpStr = byteToStr(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        // 将sha1加密后的字符串可与signature对比，标识该请求来源于微信
        return tmpStr != null && tmpStr.equals(signature.toUpperCase());
    }

    /**
     * 将字节数组转化我16进制字符串
     * @param byteArray 字符数组
     * @return 字符串
     */
    private static String byteToStr(byte[] byteArray) {
        StringBuilder strDigest = new StringBuilder();
        for (byte b : byteArray) {
            strDigest.append(byteToHexStr(b));
        }
        return strDigest.toString();
    }

    /**
     * 将字节转化为十六进制字符串
     * @param mByte 字节
     * @return 字符串
     */
    private static String byteToHexStr(byte mByte) {
        char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] tempArr = new char[2];
        tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
        tempArr[1] = Digit[mByte & 0X0F];
        return new String(tempArr);
    }
}
