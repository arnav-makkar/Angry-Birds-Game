package com.AngryBirds;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameState implements Serializable {
    public List<String> birdTextures = new ArrayList<>();
    public List<float[]> birdStates = new ArrayList<>();
    public List<float[]> obstacleStates = new ArrayList<>();
    public List<float[]> pigStates = new ArrayList<>();
    public float[] currentBirdState;
    public int birdCount;
    public float[] catapultState;
}
