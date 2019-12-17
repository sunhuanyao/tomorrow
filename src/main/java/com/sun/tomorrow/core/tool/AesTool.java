package com.sun.tomorrow.core.tool;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * @Author roger sun
 * @Date 2019/12/17 18:13
 */
public class AesTool {

    private String mode = "AES/CBC/NoPadding";

    private String privateKey ="1234567890ABCDEF";

    private String iv = "fedcba0987654321";

    private Cipher instance;
    private SecretKeySpec pvkey;
    private IvParameterSpec ivMode;

    public AesTool() throws Exception{
        init();
    }

    public AesTool(String mode, String privateKey, String iv) throws Exception {
        this.mode = mode;
        this.privateKey = privateKey;
        this.iv = iv;
        init();
    }

    public void init() throws Exception{
        this.instance = Cipher.getInstance(this.mode); //设定的算法模式
        this.pvkey = new SecretKeySpec(this.privateKey.getBytes(), "AES"); // 设定的私钥
        this.ivMode = new IvParameterSpec(this.iv.getBytes()); //设定的iv
    }

    public Cipher getInstance(int waysMode) throws Exception{
        this.instance.init(waysMode, this.pvkey, this.ivMode);
        return instance;
    }

    public byte[] encryFinal(String text) throws Exception{

        byte[] enText = text.getBytes();    //要加密的数据
        byte[] aligned;
        int mod = enText.length % 16; // 规范加密的要加密的位数（必须是16位的倍数），，不足就进行填充。
        if (mod == 0) {
            aligned = new byte[enText.length];
        } else {
            aligned = new byte[enText.length + 16 - mod];
        }
        System.arraycopy(enText, 0, aligned, 0, enText.length);

        return getInstance(Cipher.ENCRYPT_MODE).doFinal(aligned);
    }

    public String encryFinalWithBase64(String text) throws Exception{
        return Base64.getEncoder().encodeToString(encryFinal(text));
    }


    public String decryFinalWithBase64(String text) throws Exception{
        //初始化
        byte[] cipherBytes = Base64.getDecoder().decode(text);
        return decryFinal(cipherBytes);
    }

    public String decryFinal(byte[] text) throws Exception{
        //初始化
        byte[] aligned = getInstance(Cipher.DECRYPT_MODE).doFinal(text);
        int posNil;
        for (posNil = 0; posNil < aligned.length; posNil++) {
            if (aligned[posNil] == 0x00)
                break;
        }
        byte[] enText = new byte[posNil];
        System.arraycopy(aligned, 0, enText, 0, posNil);
        return new String(enText);
    }



    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }
}
