package cn.gracias.chatgpt.api.infrastructure.util.sdk;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class SignUtil {
    private static String token = "b8b6";

    /**
     * 校验签名
     * @param signature 签名
     * @param timestamp 时间戳
     * @param nonce 随机数
     * @return 布尔值
     */
    public static boolean checkSignature(String signature, String timestamp, String nonce) {
        String checkText = null;
        if (null != signature) {
            // 对 token, timestamp, nonce 按字典排序
            String[] paramArr = new String[]{token, timestamp, nonce};
            Arrays.sort(paramArr);
            // 将排序后的结果拼成一个字符串
            String content = paramArr[0].concat(paramArr[1]).concat(paramArr[2]);

            try {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
                // 对接后的字符串进行 SHA1 加密
                byte[] digest = messageDigest.digest(content.toString().getBytes());
                checkText = byteToStr(digest);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        // 将加密后的字符串与 signature 进行对比
        return checkText != null ? checkText.equals(signature.toUpperCase()) : false;
    }

    /**
     * 将字节数组转化我16进制字符串
     * @param byteArrays 字符数组
     * @return 字符串
     */
    private static String byteToStr(byte[] byteArrays) {
        String str = "";
        for (int i = 0; i < byteArrays.length; i++) {
            str += byteToHexStr(byteArrays[i]);
        }
        return str;
    }

    /**
     *  将字节转化为十六进制字符串
     * @param myByte 字节
     * @return 字符串
     */
    private static String byteToHexStr(byte myByte) {
        char[] digits = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        char[] tempArr = new char[2];
        tempArr[0] = digits[(myByte >>> 4) & 0x0F];
        tempArr[1] = digits[myByte & 0x0F];
        String str = new String(tempArr);
        return str;
    }
}
