package com.loushi.utils.login;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.security.Security;

/**
 * 解密工具
 */

@Slf4j
public class AesCbcUtil {

    static {
        // BouncyCastle是一个开源的加解密解决方案，主页在http://www.bouncycastle.org/
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * AES解密
     * @param data           //密文，被加密的数据
     * @param key            //秘钥
     * @param iv             //偏移量
     * @param encodingFormat //解密后的结果需要进行的编码
     */
    public static JSONObject decrypt(String data, String key, String iv, String encodingFormat) throws Exception {
        // 被加密的数据
        byte[] dataByte = Base64.decodeBase64(data);
        // 加密秘钥
        byte[] keyByte = Base64.decodeBase64(key);
        // 偏移量
        byte[] ivByte = Base64.decodeBase64(iv);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
        SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
        AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
        parameters.init(new IvParameterSpec(ivByte));
        cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化

        byte[] resultByte = cipher.doFinal(dataByte);
        if (null != resultByte && resultByte.length > 0) {
            String str = new String(resultByte, encodingFormat);
            log.info("解密成功... {}", str);
            return JSONObject.parseObject(str);
        }
        throw new Exception("解密失败...");
    }


    public static void main(String[] args) throws Exception {
        String data = "Ig7J45phEi0lQkNxoDDnUXqL2iRkyLXyFo97ib0J+dBeOtmikv4AY0AJVRINNpi406gJ5AxYFrQGYM8WV3yBZSZUTpwSiTfAThVIZpHFbhlW+XD++CGkWw0ssNTDjFDLsnSZzIAJB+q6NaMLLif9P19qGBtA9m7P9McoOdHn5vM7uSpxNTtc/3b16IEuJokfJVWupZQmkrKh9TmlBbH6eV2Fts6CIDANElXelgTE13nScxG0gWuVR6sBTz8+UsTp5mH4937iOFx+mOACctj8bWooUJ3cnlY8LRrUusUuZqUTy0H7fDGEIMBq7uAyGndGplhfT94SC/gYPWehJiLKAs1TU1TGD+bKzsvefg+z6piGDv/pVgsGsMJliJu+Mf1YpvYToFGYjnY466YGEf+LjFoAXqVEK3VppV4ijFWSCJhTFqvC7g7qwRxSsv8ew9qDK9Xt7TzAOEDQTQDB+r3wgMVQcS7SHMJ1ZGgD5r/ShTU=";
        String key = "QC5oxeAiKGF16SbzIhowkw==";
        String iv = "zV4BSoV2TfGJHlCxBsIF7Q==";
        System.out.println(decrypt(data, key, iv, "UTF-8"));
    }

}
