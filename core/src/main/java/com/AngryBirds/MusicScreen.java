package com.AngryBirds;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MusicScreen implements Screen {
    private Stage stage;
    private Skin UIskin;
    private Texture background;
    private SpriteBatch spriteBatch;
    private Game game;

    private Sprite snd1;
    private Sprite snd2;
    private Sprite snd3;
    private Sprite snd4;
    private Sprite BACK;

    private Sprite SBGM;

    public MusicScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        spriteBatch = new SpriteBatch();
        stage = new Stage(new ScreenViewport());
        background = new Texture(Gdx.files.internal("homeBackground.png"));


        Gdx.input.setInputProcessor(stage);
        UIskin = new Skin(Gdx.files.internal("skins/uiskin.json"));

        Texture backTexture = new Texture(Gdx.files.internal("exit_app.png"));
        BACK = new Sprite(backTexture);

        Texture sbgm_texture = new Texture(Gdx.files.internal("select_bgm.png"));
        SBGM = new Sprite(sbgm_texture);

        Texture s1tex = new Texture(Gdx.files.internal("sound1.png"));
        snd1 = new Sprite(s1tex);

        Texture s2tex = new Texture(Gdx.files.internal("sound2.png"));
        snd2 = new Sprite(s2tex);

        Texture s3tex = new Texture(Gdx.files.internal("sound3.png"));
        snd3 = new Sprite(s3tex);

        Texture s4tex = new Texture(Gdx.files.internal("sound4.png"));
        snd4 = new Sprite(s4tex);

        Table table = new Table();
        table.setFillParent(true);

        table.center().padTop(100);

        Image sound1img = new Image(snd1);
        sound1img.setSize(400f, 100f);
        table.add(sound1img).size(200f, 50).center().padRight(60f);

        Image sound2img = new Image(snd2);
        sound2img.setSize(400f, 100f);
        table.add(sound2img).size(200f, 50f);

        table.row();

        Image sound3img = new Image(snd3);
        sound3img.setSize(400f, 100f);
        table.add(sound3img).size(200, 50).center().padRight(60f).padTop(60f);

        Image sound4img = new Image(snd4);
        sound4img.setSize(400f, 100f);
        table.add(sound4img).size(200, 50).padTop(60f);

        Image backButtonImage = new Image(BACK);
        backButtonImage.setSize(125f, 80f);
        stage.addActor(backButtonImage);
        backButtonImage.setPosition(20f, 20f);

        Image sbgm_image = new Image(SBGM);
        sbgm_image.setSize(600, 75);
        stage.addActor(sbgm_image);
        sbgm_image.setPosition(170, 310);

        stage.addActor(table);

        ClickListener backButtonListener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new HomeScreen(game));
            }
        };

        ClickListener s1Listener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameSettings.SONG_PATH="s1.mp3";
            }
        };

        ClickListener s2Listener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameSettings.SONG_PATH="s2.mp3";
            }
        };

        ClickListener s3Listener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameSettings.SONG_PATH="s3.mp3";
            }
        };

        ClickListener s4Listener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameSettings.SONG_PATH="s4.mp3";
            }
        };

        backButtonImage.addListener(backButtonListener);
        sound1img.addListener(s1Listener);
        sound2img.addListener(s2Listener);
        sound3img.addListener(s3Listener);
        sound4img.addListener(s4Listener);
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(GameSettings.brightness * 0.8f, GameSettings.brightness * 0.8f, GameSettings.brightness * 0.8f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();

        spriteBatch.setColor(GameSettings.brightness, GameSettings.brightness, GameSettings.brightness, 1.0f);
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
        snd1.getTexture().dispose();
    }
}
