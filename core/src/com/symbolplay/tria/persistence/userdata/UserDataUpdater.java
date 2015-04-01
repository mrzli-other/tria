package com.symbolplay.tria.persistence.userdata;

import com.badlogic.gdx.utils.ObjectMap;
import com.symbolplay.gamelibrary.util.ExceptionThrower;

final class UserDataUpdater {
    
    public static ObjectMap<String, Object> updateBySingleVersion(ObjectMap<String, Object> values, int currentVersion) {
        switch (currentVersion) {
            case 1:
                // add update code here
                // return updateVersion001(userDataValues);
                return values;
            
            default:
                ExceptionThrower.throwException("Invalid version of user data: %d", currentVersion);
                return null;
        }
    }
    
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
}
