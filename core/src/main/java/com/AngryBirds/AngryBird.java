package com.AngryBirds;

import com.badlogic.gdx.Game;

public class AngryBird extends Game {
    @Override
    public void create() {
        setScreen(new LevelSuccessScreen(this, 0, 0));
    }
}
