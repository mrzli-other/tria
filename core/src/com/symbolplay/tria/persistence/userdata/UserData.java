package com.symbolplay.tria.persistence.userdata;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.ObjectMap;
import com.symbolplay.gamelibrary.persistence.UserDataBase;
import com.symbolplay.gamelibrary.util.Logger;
import com.symbolplay.tria.persistence.GameEncryptionKeys;

// v001
// - userData : ObjectMap
//   - version : long
//   - careerData : ObjectMap
//       - coins : long
//       - antiGravityLevel : long
//       - rocketLevel : long
//       - initialLivesNextGame : long
//   - achievementsData : ObjectMap
//       - highScores : Array (20)
//         - highScore : ObjectMap
//           - name : String
//           - score : long
//           - time : long
public final class UserData extends UserDataBase {
    
    public static final int VERSION = 1;
    public static final int NUM_HIGH_SCORES = 20;
    public static final String DEFAULT_HIGH_SCORE_NAME = "-";
    
    public UserData(FileHandle fileHandle) {
        super(fileHandle, VERSION, GameEncryptionKeys.getUserDataEncryptionInputData());
    }
    
    @Override
    protected ObjectMap<String, Object> updateBySingleVersion(ObjectMap<String, Object> values, int currentVersion) {
        return UserDataUpdater.updateBySingleVersion(values, currentVersion);
    }
    
    @Override
    protected ObjectMap<String, Object> getDefault() {
        return UserDataDefault.getDefaultUserDataValues();
    }
    
    @Override
    protected ObjectMap<String, Object> readSpecial(FileHandle file) {
        try {
            return UserDataSpecialHandlingTriaVersion_1_0_0.readSpecial(file);
        } catch (Exception e) {
            Logger.error("Failed special read of user data from version 1.0.0.");
            return null;
        }
    }
}
