package com.loserico.encrypt;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

public class AESCrypt {

    private static final String IV_STRING = "3612213421341234";
    private static final String charset = "UTF-8";
    private static final String key = "idssinsightkey01";
    
    public static void main(String[] args) {
        String key = AESCrypt.aesEncryptString("067f5f48bbac4d90b11568cf19320d9e").substring(0,16);
        String tmpPasswd = AESCrypt.aesDecryptString("OTD1StSOJek+9ayxLPm8Pg==",key);
        String encrypt = AESCrypt.aesEncryptString(tmpPasswd);
        String decrypt = AESCrypt.aesDecryptString(encrypt);
        
        String pwd = MD5Utils.encode16((decrypt + "6sN0JtvQ").getBytes());
        
    }

    public static String aesEncryptString(String content)  {
        try {
            byte[] contentBytes = content.getBytes(charset);
            byte[] keyBytes = key.getBytes(charset);
            byte[] encryptedBytes = aesEncryptBytes(contentBytes, keyBytes);
            Encoder encoder = Base64.getEncoder();
            return encoder.encodeToString(encryptedBytes);
        }catch (Exception e){}
        return null;
    }

    public static String aesDecryptString(String content)  {
        try {
            Decoder decoder = Base64.getDecoder();
            byte[] encryptedBytes = decoder.decode(content);
            byte[] keyBytes = key.getBytes(charset);
            byte[] decryptedBytes = aesDecryptBytes(encryptedBytes, keyBytes);
            return new String(decryptedBytes, charset);
        }catch (Exception e){}
        return null;
    }

    public static String aesDecryptString(String content,String key1)  {
        try {
            Decoder decoder = Base64.getDecoder();
            byte[] encryptedBytes = decoder.decode(content);
            byte[] keyBytes = key1.getBytes(charset);
            byte[] decryptedBytes = aesDecryptBytes(encryptedBytes, keyBytes);
            return new String(decryptedBytes, charset);
        }catch (Exception e){}
        return null;
    }

    public static byte[] aesEncryptBytes(byte[] contentBytes, byte[] keyBytes)  {
        try {
            return cipherOperation(contentBytes, keyBytes, Cipher.ENCRYPT_MODE);
        }catch (Exception e){}
        return null;
    }

    public static byte[] aesDecryptBytes(byte[] contentBytes, byte[] keyBytes) {
        return cipherOperation(contentBytes, keyBytes, Cipher.DECRYPT_MODE);
    }

    private static byte[] cipherOperation(byte[] contentBytes, byte[] keyBytes, int mode)  {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");
            byte[] initParam = IV_STRING.getBytes(charset);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(mode, secretKey, ivParameterSpec);
            return cipher.doFinal(contentBytes);
        }catch (Exception e){}
        return null;
    }

}
