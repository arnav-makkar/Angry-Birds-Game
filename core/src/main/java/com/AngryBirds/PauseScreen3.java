package com.AngryBirds;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.io.IOException;
import java.io.ObjectInputStream;

public class PauseScreen3 implements Screen {
    private Stage stage;
    private Skin UIskin;
    private SpriteBatch spriteBatch;
    private Texture background;
    private Game game;

    private Sprite RESG;
    private Sprite BTMM;

    public PauseScreen3(Game game) {
        this.game=game;
    }

    @Override
    public void show() {
        spriteBatch = new SpriteBatch();
        stage = new Stage(new ScreenViewport());

        background = new Texture(Gdx.files.internal("game_screenBG.png"));

        Gdx.input.setInputProcessor(stage);
        UIskin = new Skin(Gdx.files.internal("skins/uiskin.json"));

        Texture resgTexture = new Texture(Gdx.files.internal("resumegameButton.png"));
        RESG = new Sprite(resgTexture);
        RESG.setSize(200f, 20f);

        Texture btmmTexture = new Texture(Gdx.files.internal("btmmButton.png"));
        BTMM = new Sprite(btmmTexture);
        BTMM.setSize(200f, 20f);


        Table table = new Table();
        table.setFillParent(true);

        Image resgButtonImage = new Image(RESG);
        resgButtonImage.setSize(150f, 10f);
        table.add(resgButtonImage).size(400f, 70f).center().padBottom(20);
        table.row();

        Image btmmButtonImage = new Image(BTMM);
        btmmButtonImage.setSize(150f, 10f);

        table.add(btmmButtonImage).size(575f, 70f).center().padBottom(20);
        table.row();
        stage.addActor(table);

        ClickListener resumeButtonListener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                L3Screen gameScreen=new L3Screen(game);
                gameScreen.loadState("savegame.dat");
                game.setScreen(gameScreen);
            }
        };

        ClickListener btmmButtonListener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new HomeScreen(game));
            }
        };

        btmmButtonImage.addListener(btmmButtonListener);
        resgButtonImage.addListener(resumeButtonListener);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.8f, 0.8f,0.8f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();

        spriteBatch.setColor(GameSettings.brightness, GameSettings.brightness, GameSettings.brightness, 1.0f);
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spriteBatch.end();

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
        RESG.getTexture().dispose();
        BTMM.getTexture().dispose();
    }
}
