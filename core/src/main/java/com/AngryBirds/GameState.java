package com.AngryBirds;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GameState implements Serializable {

    public LinkedList<Obstacle> obstacles;
    public LinkedList<Pig> pigs;
    public LinkedList<BodyState> birdStates;
    public float totalTime;
    public int birdCount;
}


