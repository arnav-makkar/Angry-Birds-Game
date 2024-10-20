package com.AngryBirds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SettingsScreen implements Screen {
    private Stage stage;
    private Skin UIskin;
    private SpriteBatch spriteBatch;

    private Sprite LOGO;
    private Sprite BACK;

    @Override
    public void show() {
        spriteBatch=new SpriteBatch();

        Texture logoTexture = new Texture(Gdx.files.internal("settingsLogo.png"));
        LOGO = new Sprite(logoTexture);
        LOGO.setSize(200f, 20f);

        Texture backTexture = new Texture(Gdx.files.internal("back.png"));
        LOGO = new Sprite(backTexture);
        LOGO.setSize(200f, 20f);


        Table table=new Table();
        table.setFillParent(true);

        Image logoButtonImage=new Image(LOGO);
        logoButtonImage.setSize(150f, 10f);

        table.add(logoButtonImage).size(700f,10f).center().padBottom(20);
        table.row();

        Label VOLUME = new Label("Game Volume", UIskin);
        Slider vslide = new Slider(0, 100, 1, false, UIskin);
        vslide.setValue(50);

        table.add(VOLUME).center().padBottom(10);
        table.row();
        table.add(vslide).width(300).center().padBottom(30);
        table.row();

        Label BRIGHTNESS = new Label("Volume", UIskin);
        Slider bslide = new Slider(0, 100, 1, false, UIskin);
        bslide.setValue(50);

        table.add(BRIGHTNESS).center().padBottom(10);
        table.row();
        table.add(bslide).width(300).center().padBottom(30);
        table.row();

        Image backButtonImage=new Image(BACK);
        backButtonImage.setSize(150f, 10f); // Set size for the play button image

        table.add(backButtonImage).size(700f, 100f).center().padBottom(20);
        table.row();

        stage.addActor(table);

        ChangeListener volumeListner=new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
            }
        };

        ChangeListener brightnessListner=new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
            }
        };

        vslide.addListener((EventListener) volumeListner);
        bslide.addListener((EventListener) brightnessListner);

    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(0, 0, 0, 1);

        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();

        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        spriteBatch.end();

        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        UIskin.dispose();
        stage.dispose();
        spriteBatch.dispose();
        BACK.getTexture().dispose();
    }
}
