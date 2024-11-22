package com.AngryBirds;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;

public class Pig extends Obstacle {

    // Constructor to match the parent class
    public Pig(Body body, Texture texture, float x, float y) {
        super(body, texture, x, y, 1); // Call the parent class constructor
    }
}
