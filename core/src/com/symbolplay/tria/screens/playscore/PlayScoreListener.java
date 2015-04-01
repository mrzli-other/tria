package com.symbolplay.tria.screens.playscore;

import com.badlogic.gdx.utils.Array;

public interface PlayScoreListener {
    void scoreReceived(Array<PlayScoreData> playScores);
}
