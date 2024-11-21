package com.AngryBirds;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;

public class Obstacle {
    Body body;
    Texture texture;

    Obstacle(Body body, Texture texture) {
        this.body = body;
        this.texture = texture;
    }
}
