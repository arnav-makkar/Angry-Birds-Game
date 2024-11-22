package com.AngryBirds;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LevelFailScreen implements Screen {
    private final Game game;
    private Texture background;
    private Texture fail;
    private Texture click;
    private SpriteBatch batch;

    public LevelFailScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        background = new Texture("game_screenBG.png");
        fail = new Texture("levelFail.png");
        click=new Texture("click.png");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        float failX = (Gdx.graphics.getWidth() - fail.getWidth()) / 2f;
        float failY = (Gdx.graphics.getHeight() - fail.getHeight()) / 2f;
        batch.draw(fail, failX, failY);
        batch.draw(click,failX+160,failY-100,400,40);
        batch.end();

        if (Gdx.input.justTouched()) {
            game.setScreen(new LevelScreen(game));
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        fail.dispose();
        click.dispose();
    }
}
