package com.AngryBirds;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class AngryBird extends Game {
    @Override
    public void create() {
        setScreen(new SettingsScreen(this));
    }
}
