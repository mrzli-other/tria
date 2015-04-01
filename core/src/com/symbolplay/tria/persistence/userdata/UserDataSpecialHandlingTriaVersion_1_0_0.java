package com.symbolplay.tria.persistence.userdata;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.symbolplay.gamelibrary.persistence.JsonReaderUtils;
import com.symbolplay.gamelibrary.util.ExceptionThrower;

// TODO: this code should be removed after a while (when all users updated from userdata in tria version 1.0.0)
public final class UserDataSpecialHandlingTriaVersion_1_0_0 {
    
    private static final String CHARSET = "UTF-8";
    
    @SuppressWarnings("unchecked")
    public static ObjectMap<String, Object> readSpecial(FileHandle file) {
        byte[] fileData = file.readBytes();
        String jsonText = decrypt(fileData, getKey());
        JsonValue jsonValue = new JsonReader().parse(jsonText);
        return (ObjectMap<String, Object>) JsonReaderUtils.jsonValueToObject(jsonValue);
    }
    
    private static String decrypt(byte[] encryptedData, byte[] key) {
        try {
            byte[] base64Data = decryptData(encryptedData, key);
            String base64String = new String(base64Data, CHARSET);
            String gameDataString = Base64Coder.decodeString(base64String);
            return gameDataString;
        } catch (Exception e) {
            ExceptionThrower.throwException(e.getMessage());
            return null;
        }
    }
    
    private static byte[] decryptData(byte[] encryptedData, byte[] key)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] clearData = cipher.doFinal(encryptedData);
        return clearData;
    }
    
    private static byte[] getKey() {
        byte[] key = new byte[] {
                (byte) "s".toCharArray()[0],
                (byte) "o".toUpperCase().toCharArray()[0],
                (byte) (11 / Math.min(3, 11)),
                44,
                111 + 11,
                (byte) "o".toCharArray()[0],
                (byte) "D".toCharArray()[0],
                (byte) "Y".toLowerCase().toCharArray()[0],
                32,
                55,
                53,
                (byte) (44 - 22 * Math.max(2, 3)),
                52,
                56,
                42,
                8
        };
        
        return key;
    }
}
