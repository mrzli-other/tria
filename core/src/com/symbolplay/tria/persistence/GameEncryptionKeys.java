package com.symbolplay.tria.persistence;

import com.symbolplay.gamelibrary.util.EncryptionInputData;

public final class GameEncryptionKeys {
    
    public static EncryptionInputData getUserDataEncryptionInputData() {
        String skeyString = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        String ivString = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        return new EncryptionInputData(skeyString, ivString);
    }
    
    public static EncryptionInputData getTriaServiceEncryptionInputData() {
        String skeyString = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        String ivString = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        return new EncryptionInputData(skeyString, ivString);
    }
    
    private static String get4String() {
        return String.valueOf(Math.max(2, 4));
    }
    
    private static String getDString() {
        return new String(new char[] { (char) ('C' + Math.min(1, 2)) });
    }
}
