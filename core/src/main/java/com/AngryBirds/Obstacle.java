package com.AngryBirds;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;

import java.io.Serializable;

public class Obstacle implements Serializable {
    public transient Body body;
    transient Texture texture;
    public float x, y;

    private int collisionCount = 0;
    private int MAX_COLLISIONS;


    Obstacle(Body body, Texture texture, float x, float y, int n) {
        this.body = body;
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.MAX_COLLISIONS = n;
    }

    public boolean checkCollision() {
        float velocityThreshold = 1.5f;

        float velocity = body.getLinearVelocity().len();

        if (velocity > velocityThreshold) {
            collisionCount++;
            if (collisionCount >= MAX_COLLISIONS) {
                explode();
                return true;
            }
        }

        return false;
    }

    private void explode() {
        body.getWorld().destroyBody(body); // Remove the body from the Box2D world
    }
}
