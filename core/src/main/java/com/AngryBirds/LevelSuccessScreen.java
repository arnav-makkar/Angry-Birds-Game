package com.AngryBirds;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class LevelSuccessScreen implements Screen {
    private Stage stage;
    private Skin UIskin;
    private Texture background;
    private SpriteBatch spriteBatch;
    private Game game;
    private float time;
    private BitmapFont font;

    public LevelSuccessScreen(Game game, float time) {
        this.time = time;
        this.game = game;
    }

    public float calcScore() {
        return (20 - this.time) * 100;
    }

    @Override
    public void show() {
        spriteBatch = new SpriteBatch();

        background = new Texture(Gdx.files.internal("game_screenBG.png"));

        UIskin = new Skin(Gdx.files.internal("skins/uiskin.json"));

        font = new BitmapFont();
        font.getData().setScale(2);
        font.setColor(Color.WHITE);

        Texture headerTexture = new Texture(Gdx.files.internal("levelComplete.png"));
        Image headerImage = new Image(headerTexture);
        TextButton nextLevelButton = new TextButton("Select Levels", UIskin);

        stage = new Stage(new ScreenViewport());

        Table table = new Table();
        table.setFillParent(true);
        table.center();

        table.add(headerImage).padBottom(50);
        table.row();
        table.add(nextLevelButton).padBottom(40);

        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);

        nextLevelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LevelScreen(game));
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        String scoreText = "Current Score is : " + (int) calcScore();
        float scoreX = Gdx.graphics.getWidth() / 2f - font.getRegion().getRegionWidth() / 2f;
        float scoreY = Gdx.graphics.getHeight() / 2f;
        font.draw(spriteBatch, scoreText, scoreX, scoreY+10);

        spriteBatch.end();

        stage.act(delta);
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
        stage.dispose();
        UIskin.dispose();
        spriteBatch.dispose();
        background.dispose();
        font.dispose();
    }
}
