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
}
