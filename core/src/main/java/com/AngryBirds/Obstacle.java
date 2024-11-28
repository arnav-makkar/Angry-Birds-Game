package com.AngryBirds;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;

import java.io.Serializable;

public class Obstacle implements Serializable {
    public transient Body body;
    transient Texture texture;
    public float x, y;

    private int collisionCount = 0; // To track the number of collisions
    private int MAX_COLLISIONS;


    Obstacle(Body body, Texture texture, float x, float y, int n) {
        this.body = body;
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.MAX_COLLISIONS = n;
    }

    public boolean checkCollision() {
        float velocityThreshold = 0.75f; // Adjust the threshold as needed

        // Get the linear velocity of the pig's body
        float velocity = body.getLinearVelocity().len();

        // Check if the pig's velocity is high enough
        if (velocity > velocityThreshold) {
            collisionCount++; // Increment collision count
            if (collisionCount >= MAX_COLLISIONS) {
                explode(); // Trigger explosion after 2 collisions
                return true;
            }
        }

        return false;
    }

    private void explode() {
        body.getWorld().destroyBody(body); // Remove the body from the Box2D world
    }
}
