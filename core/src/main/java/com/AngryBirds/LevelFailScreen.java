package com.AngryBirds;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class LevelFailScreen implements Screen {
    private final Game game;
    private Texture background;
    private Sprite fail;
    private Sprite click;
    private SpriteBatch batch;
    private Stage stage;

    public LevelFailScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        background = new Texture("game_screenBG.png");
        fail = new Sprite(new Texture("game_over.png"));
        click = new Sprite(new Texture("click.png"));

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Table table1 = new Table();
        table1.setFillParent(true);

        table1.center();

        Image fail_img = new Image(fail);
        fail_img.setSize(200f,200f);
        table1.add(fail_img).size(400f, 70f).padBottom(50f);


        Table table2 = new Table();
        table2.setFillParent(true);

        table2.center().bottom();

        Image click_img = new Image(click);
        click_img.setSize(200f,200f);
        table2.add(click_img).size(180f, 60f);

        stage.addActor(table1);
        stage.addActor(table2);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

//        float failX = (Gdx.graphics.getWidth() - fail.getWidth()) / 2f;
//        float failY = (Gdx.graphics.getHeight() - fail.getHeight()) / 2f;
//        batch.draw(fail, failX, failY);
//        batch.draw(click,failX+160,failY-100,400,40);
        batch.end();

        if (Gdx.input.justTouched()) {
            game.setScreen(new LevelScreen(game));
        }

        stage.act();
        stage.draw();
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
    }
}
