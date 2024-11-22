package com.AngryBirds;

import com.badlogic.gdx.Game;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class AngryBird extends Game {
    @Override
    public void create() {
        setScreen(new L1Screen(this));
    }
}
