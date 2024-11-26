package com.AngryBirds;
import com.badlogic.gdx.graphics.Texture;

import java.io.Serializable;

public class BodyState implements Serializable {

    private Texture redBirdTexture;
    private Texture yellowBirdTexture;
    private Texture blackBirdTexture;


    public float posX, posY;
    public float velX, velY;
    public float angle;
    String birdType;

    public BodyState(float posX, float posY, float velX, float velY, float angle,Texture birdTexture) {
        this.posX = posX;
        this.posY = posY;
        this.velX = velX;
        this.velY = velY;
        this.angle = angle;

        if (birdTexture == redBirdTexture) this.birdType = "red";
        else if (birdTexture == yellowBirdTexture) this.birdType = "yellow";
        else if (birdTexture == blackBirdTexture) this.birdType = "black";
    }

}

