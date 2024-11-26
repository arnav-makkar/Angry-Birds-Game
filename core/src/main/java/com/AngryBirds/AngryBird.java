package com.AngryBirds;

import com.badlogic.gdx.Game;

public class AngryBird extends Game {
    @Override
    public void create() {
        setScreen(new L3Screen(this));
    }
}
