package com.AngryBirds;

import com.badlogic.gdx.Game;

public class AngryBird extends Game {
    @Override
    public void create() {
        setScreen(new L1copy(this));
    }
}
