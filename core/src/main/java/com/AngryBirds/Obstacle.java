package com.AngryBirds;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;

public class Obstacle {
    Body body;
    Texture texture;
    float x, y;

    Obstacle(Body body, Texture texture, float x, float y) {
        this.body = body;
        this.texture = texture;
        this.x = x;
        this.y = y;
    }
}
