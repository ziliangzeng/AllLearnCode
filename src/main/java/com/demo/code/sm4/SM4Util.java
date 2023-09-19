package com.demo.code.sm4;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Base64;
import java.util.Objects;

public class SM4Util {

    private static final String ALGORITHM_NAME = "SM4";
    private static final String ALGORITHM_ECB_PKCS5PADDING = "SM4/ECB/PKCS5Padding";

    /**
     * SM4算法目前只支持128位（即密钥16字节）
     */
    private static final int DEFAULT_KEY_SIZE = 128;

    static {
        // 防止内存中出现多次BouncyCastleProvider的实例
        if (Objects.isNull(Security.getProvider(BouncyCastleProvider.PROVIDER_NAME))) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    /**
     * 生成秘钥
     * @return 秘钥
     * @throws Exception
     */
    public static String generateKey() throws Exception {
        KeyGenerator kg = KeyGenerator.getInstance(ALGORITHM_NAME, BouncyCastleProvider.PROVIDER_NAME);
        kg.init(DEFAULT_KEY_SIZE, new SecureRandom());
        return Hex.toHexString(kg.generateKey().getEncoded());
    }

    /**
     * 加密，SM4-ECB-PKCS5Padding
     *
     * @param data 要加密的明文
     * @param key 秘钥
     * @return base64加密后的密文
     * @throws Exception 加密异常
     */
    public static String encrypt(String data,String key) throws Exception {
        byte[] bytes = sm4(data.getBytes(), Hex.decode(key), Cipher.ENCRYPT_MODE);
        //返回base64加密的数据
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * 解密，SM4-ECB-PKCS5Padding
     *
     * @param data 要解密的密文
     * @param key 秘钥
     * @return 解密后的明文
     * @throws Exception 解密异常
     */
    public static String decrypt(String data,String key) throws Exception {
        //base64解密
        byte[] decodeData = Base64.getDecoder().decode(data);
        //解密
        byte[] bytes = sm4(decodeData, Hex.decode(key), Cipher.DECRYPT_MODE);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    /**
     * SM4对称加解密
     *
     * @param input 明文（加密模式）或密文（解密模式）
     * @param key   密钥
     * @param mode  Cipher.ENCRYPT_MODE - 加密；Cipher.DECRYPT_MODE - 解密
     * @return 密文（加密模式）或明文（解密模式）
     * @throws Exception 加解密异常
     */
    private static byte[] sm4(byte[] input, byte[] key, int mode) throws Exception {
        SecretKeySpec sm4Key = new SecretKeySpec(key, ALGORITHM_NAME);
        Cipher cipher = Cipher.getInstance(ALGORITHM_ECB_PKCS5PADDING, BouncyCastleProvider.PROVIDER_NAME);
        cipher.init(mode, sm4Key);
        return cipher.doFinal(input);
    }

    public static void main(String[] args) throws Exception {
        String key = "e9767a189c52adedfdf8cefb31bd0010";
        String data="{\n" +
                "    \"alarmId\":\"111\",\n" +
                "    \"alarmType\":\"jk\",\n" +
                "    \"alarmName\":\"监控告警\",\n" +
                "    \"alarmTime\":\"2023-08-09 11:00:05\",\n" +
                "    \"alarmData\":\"测试告警内容\",\n" +
                "    \"recipientPhone\":\"15602576088,15602576099\",\n" +
                "    \"notifierPhone\":\"15602576077\",\n" +
                "    \"cityCode\":\"51002200\",\n" +
                "    \"countyCode\":\"51146100\",\n" +
                "    \"deptCode\":\"51080100\",\n" +
                "    \"alarmPosition\":\"51146100\"\n" +
                "}";
        //加密
        String encrypt = encrypt(data, key);
        System.out.println(encrypt);
        //解密
        String decrypt = decrypt(encrypt, key);
        System.out.println(decrypt);

        long l = System.currentTimeMillis();

        System.out.println("时间戳："+l);
    }

}
