package com.AngryBirds;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class HomeScreen implements Screen {
    private Stage stage;
    private Skin UIskin;
    private Texture background;
    private SpriteBatch spriteBatch;
    private Game game;

    private Sprite PLAY;
    private Sprite LOGO;
    private Sprite SETTINGS;
    private Sprite EXIT;

    public HomeScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        spriteBatch = new SpriteBatch();
        background = new Texture(Gdx.files.internal("homeBackground.png"));

        Texture playTexture = new Texture(Gdx.files.internal("playButton.png"));
        PLAY = new Sprite(playTexture);
        PLAY.setSize(150f, 50f);

        Texture logoTexture = new Texture(Gdx.files.internal("angryBirdsLogo.png"));
        LOGO = new Sprite(logoTexture);
        LOGO.setSize(200f, 20f);

        Texture settingsTexture = new Texture(Gdx.files.internal("settingsButton.png"));
        SETTINGS = new Sprite(settingsTexture);
        SETTINGS.setSize(200f, 20f);

        Texture exitTexture = new Texture(Gdx.files.internal("exit_app.png"));
        EXIT = new Sprite(exitTexture);
        EXIT.setSize(200f, 20f);

        UIskin = new Skin(Gdx.files.internal("skins/uiskin.json"));

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);

        Image logoButtonImage = new Image(LOGO);
        logoButtonImage.setSize(150f, 10f); // Set size for the play button image
        table.add(logoButtonImage).size(700f, 160f).center().padBottom(20).padTop(80f);
        table.row();

        Image playButtonImage = new Image(PLAY);
        playButtonImage.setSize(150f, 50f);
        table.add(playButtonImage).size(200f, 70f).center().padBottom(20);
        table.row();

        Table buttonRow = new Table();

        Image exitButtonImage = new Image(EXIT);
        exitButtonImage.setSize(150f, 750f);
        buttonRow.add(exitButtonImage).size(125f, 80f).padRight(80f); // Add padding between buttons

        Image settingsButtonImage = new Image(SETTINGS);
        settingsButtonImage.setSize(150f, 750f);
        buttonRow.add(settingsButtonImage).size(75f, 75f).padRight(25f);

        table.add(buttonRow).center();
        table.row();

        stage.addActor(table);

        ClickListener playButtonListener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LevelScreen(game));
            }
        };

        ClickListener settingsButtonListener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SettingsScreen(game));
            }
        };

        ClickListener exitButtonListener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.exit(0);
            }
        };

        playButtonImage.addListener(playButtonListener);
        settingsButtonImage.addListener(settingsButtonListener);
        exitButtonImage.addListener(exitButtonListener);
    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(0, 0, 0, 1);

        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();

        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spriteBatch.end();

        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        UIskin.dispose();
        stage.dispose();
        spriteBatch.dispose();
        LOGO.getTexture().dispose();
        PLAY.getTexture().dispose();
        background.dispose();
    }
}
