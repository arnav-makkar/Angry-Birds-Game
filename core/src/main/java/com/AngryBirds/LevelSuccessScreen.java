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
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.max;

public class LevelSuccessScreen implements Screen {
    private Stage stage;
    private Skin UIskin;
    private Texture background;
    private SpriteBatch spriteBatch;
    private Game game;
    private BitmapFont font;
    private float score;
    private int game_num;
    private int highscore;


    public LevelSuccessScreen(Game game, float score, int game_num) {
        this.score = score;
        this.game = game;
        this.game_num = game_num;
    }

    @Override
    public void show() {
        spriteBatch = new SpriteBatch();

        background = new Texture(Gdx.files.internal("game_screenBG.png"));

        UIskin = new Skin(Gdx.files.internal("skins/uiskin.json"));

        font = new BitmapFont();
        font.getData().setScale(2);
        font.setColor(Color.WHITE);

        Texture lvl_complete = new Texture(Gdx.files.internal("levelComplete.png"));
        Image lvl_complete_img = new Image(lvl_complete);
        Texture select_lvl = new Texture(Gdx.files.internal("selectLevel.png"));
        Image select_lvl_img = new Image(select_lvl);

        stage = new Stage(new ScreenViewport());

        Table table1 = new Table();
        table1.setFillParent(true);
        table1.center();

        table1.add(lvl_complete_img).size(480,80).padBottom(100);

        Table table2 = new Table();
        table2.setFillParent(true);
        table2.bottom();

        table2.add(select_lvl_img).size(300, 50).padBottom(80);

        stage.addActor(table1);
        stage.addActor(table2);
        Gdx.input.setInputProcessor(stage);

        select_lvl_img.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LevelScreen(game));
            }
        });
    }

    public int calcHS(int game_num){
        List<String[]> data = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("highscore.csv"))) {
            String line;

            while ((line = br.readLine()) != null) {
                data.add(line.split(","));
            }

            if (data.size() > 1) {
                highscore = Integer.parseInt(data.get(1)[game_num]);
            }
        }
        catch (IOException e) {
            System.err.println("Error updating the file: " + e.getMessage());
        }

        return highscore;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        String scoreText = "Current Score: " + (int) score + "\n High Score    : " + calcHS(game_num);
        float scoreX = Gdx.graphics.getWidth() / 2f - font.getRegion().getRegionWidth() / 2f;
        float scoreY = Gdx.graphics.getHeight() / 2f;
        font.draw(spriteBatch, scoreText, scoreX+15, scoreY-10);

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
