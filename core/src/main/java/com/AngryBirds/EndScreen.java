package com.AngryBirds;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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

public class EndScreen implements Screen {
    private Stage stage;
    private Skin UIskin;
    private Texture background;
    private SpriteBatch spriteBatch;
    private Game game;
    private int score;

    public EndScreen(int score) {
        this.score = score;
        this.game = AngryBird;
    }

    @Override
    public void show() {
        spriteBatch = new SpriteBatch();

        background = new Texture(Gdx.files.internal("game_screenBG.png"));

        UIskin = new Skin(Gdx.files.internal("skins/uiskin.json"));

        Texture headerTexture = new Texture(Gdx.files.internal("levelComplete.png"));
        Image headerImage = new Image(headerTexture);

        Texture scoreTexture = new Texture(Gdx.files.internal("score.png"));
        Image scoreImage = new Image(scoreTexture);

        TextButton nextLevelButton = new TextButton("Select Levels", UIskin);

        scoreImage.setWidth(200);
        scoreImage.setHeight(100);

        stage = new Stage(new ScreenViewport());

        Table table = new Table();
        table.setFillParent(true);
        table.center();

        table.add(headerImage).padBottom(20);
        table.row();
        table.add(scoreImage).padBottom(20);
        table.row();
        table.add(nextLevelButton).padBottom(20);

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
    }
}
