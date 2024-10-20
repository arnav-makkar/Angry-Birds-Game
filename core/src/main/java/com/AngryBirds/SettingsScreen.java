package com.AngryBirds;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class SettingsScreen implements Screen {
    private Stage stage;
    private Skin UIskin;
    private SpriteBatch spriteBatch;
    private Game game;



    private Sprite LOGO;
    private Sprite BACK;

    public SettingsScreen(Game game) {
        this.game=game;
    }

    @Override
    public void show() {
        spriteBatch = new SpriteBatch();
        stage = new Stage(new ScreenViewport());

        Gdx.input.setInputProcessor(stage);
        UIskin = new Skin(Gdx.files.internal("skins/uiskin.json"));

        Texture logoTexture = new Texture(Gdx.files.internal("settingsLogo.png"));
        LOGO = new Sprite(logoTexture);
        LOGO.setSize(200f, 20f);

        Texture backTexture = new Texture(Gdx.files.internal("back.png"));
        BACK = new Sprite(backTexture);
        BACK.setSize(200f, 20f);

        // Setup the layout using a table
        Table table = new Table();
        table.setFillParent(true);

        Image logoButtonImage = new Image(LOGO);
        logoButtonImage.setSize(150f, 10f);
        table.add(logoButtonImage).size(700f, 300f).center().padBottom(20);
        table.row();

        Label VOLUME = new Label("Game Volume", UIskin);
        Slider vslide = new Slider(0, 100, 1, false, UIskin);
        vslide.setValue(50);

        table.add(VOLUME).center().padBottom(10);
        table.row();
        table.add(vslide).width(500).center().padBottom(30);
        table.row();

        Label BRIGHTNESS = new Label("Screen Brightness", UIskin);
        Slider bslide = new Slider(0, 100, 1, false, UIskin);
        bslide.setValue(50);

        table.add(BRIGHTNESS).center().padBottom(10);
        table.row();
        table.add(bslide).width(500).center().padBottom(30);
        table.row();

        Image backButtonImage = new Image(BACK);
        backButtonImage.setSize(150f, 10f);

        table.add(backButtonImage).size(300f, 100f).center().padBottom(20);
        table.row();
        stage.addActor(table);

        // Change listeners for sliders
        ChangeListener volumeListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                // Handle volume changes
                System.out.println("Volume changed to: " + vslide.getValue());
            }
        };

        ChangeListener brightnessListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                // Handle brightness changes
                System.out.println("Brightness changed to: " + bslide.getValue());
            }
        };

        ClickListener backButtonListener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new HomeScreen(game));
            }
        };

        backButtonImage.addListener(backButtonListener);
        vslide.addListener(volumeListener);
        bslide.addListener(brightnessListener);
    }

    @Override
    public void render(float delta) {
        // Clear the screen with a black color
        Gdx.gl.glClearColor(0.8f, 0.8f,0.8f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Set up camera
        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();

        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        spriteBatch.end();

        // Act and draw the stage
        stage.act(Math.min(delta, 1 / 30f));
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
        LOGO.getTexture().dispose();
    }
}
