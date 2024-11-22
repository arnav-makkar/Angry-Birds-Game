package com.AngryBirds;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;

public class Pig extends Obstacle {

    // Constructor to match the parent class
    public Pig(Body body, Texture texture, float x, float y) {
        super(body, texture, x, y, 1); // Call the parent class constructor
    }

    // explode pig if it hits the bird or the ground

    public boolean checkCollision() {
        float velocityThreshold = 2f; // Adjust the threshold as needed

        // Get the linear velocity of the pig's body
        float velocity = body.getLinearVelocity().len();

        // Check if the pig's velocity is high enough to "explode"
        if (velocity > velocityThreshold) {
            explode();
            return true;
        }

        return false;
    }

    private void explode() {
        body.getWorld().destroyBody(body); // Remove pig from the Box2D world
    }
}
