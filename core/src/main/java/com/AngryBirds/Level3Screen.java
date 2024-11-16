package com.AngryBirds;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
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

public class Level3Screen implements Screen {
    private Stage stage;
    private Skin UIskin;
    private Texture background;
    private SpriteBatch spriteBatch;
    private Game game;

    private Sprite PAUSE;
    private Sprite RED;
    private Sprite BLACK;
    private Sprite YELLOW;
    private Sprite CATAPULT;

    private Sprite PIG;
    private Sprite WOOD_H;
    private Sprite WOOD_V;
    private Sprite WOOD_BOX;

    public Level3Screen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        spriteBatch = new SpriteBatch();
        background = new Texture(Gdx.files.internal("game_screenBG.png"));

        Texture pauseTexture = new Texture(Gdx.files.internal("pauseButton.png"));
        PAUSE = new Sprite(pauseTexture);
        PAUSE.setSize(150f, 50f);

        Texture redTexture = new Texture(Gdx.files.internal("redBird.png"));
        RED = new Sprite(redTexture);
        RED.setSize(200f, 20f);

        Texture blackTexture = new Texture(Gdx.files.internal("blackBird.png"));
        BLACK = new Sprite(blackTexture);
        BLACK.setSize(200f, 20f);

        Texture yellowTexture = new Texture(Gdx.files.internal("yellowBird.png"));
        YELLOW = new Sprite(yellowTexture);
        YELLOW.setSize(200f, 20f);

        Texture catapultTexture = new Texture(Gdx.files.internal("catapult.png"));
        CATAPULT = new Sprite(catapultTexture);
        CATAPULT.setSize(200f, 20f);

        Texture pigTexture = new Texture(Gdx.files.internal("pig.png"));
        PIG = new Sprite(pigTexture);
        PIG.setSize(200f, 20f);

        Texture wood_h_tex = new Texture(Gdx.files.internal("wood_h.png"));
        WOOD_H = new Sprite(wood_h_tex);
        WOOD_H.setSize(200f, 20f);

        Texture wood_v_tex = new Texture(Gdx.files.internal("wood_v.png"));
        WOOD_V = new Sprite(wood_v_tex);
        WOOD_V.setSize(200f, 20f);

        Texture wood_box_tex = new Texture(Gdx.files.internal("wood_box.png"));
        WOOD_BOX = new Sprite(wood_box_tex);
        WOOD_BOX.setSize(200f, 20f);

        UIskin = new Skin(Gdx.files.internal("skins/uiskin.json"));

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);

        Image pauseButtonImage = new Image(PAUSE);
        pauseButtonImage.setSize(30f, 30f);
        table.top().right();  // Set the table alignment to top-left
        table.add(pauseButtonImage).size(60f, 55f).padTop(20).padRight(40);
        table.row();

        Table table2 = new Table();
        table2.setFillParent(true);

        table2.center().left();

        Image blackButtonImage = new Image(blackTexture);
        blackButtonImage.setSize(30f, 30f);
        table2.add(blackButtonImage).size(50f, 50f);
        table2.columnDefaults(0).left();

        Image yellowButtonImage = new Image(yellowTexture);
        yellowButtonImage.setSize(30f, 30f);
        table2.add(yellowButtonImage).size(50f, 50f);
        table2.columnDefaults(0).left();

        Image redButtonImage = new Image(redTexture);
        redButtonImage.setSize(30f, 30f);
        table2.add(redButtonImage).size(50f, 50f);
        table2.columnDefaults(0).left();

        Image catapultButtonImage = new Image(catapultTexture);
        catapultButtonImage.setSize(75f, 50f);
        table2.add(catapultButtonImage).size(125f, 75f);

        table2.padBottom(30);
        //table2.padLeft(10);

        Table table3 = new Table();
        table3.setFillParent(true);

        table3.center();

        Image pig_top = new Image(pigTexture);
        table3.add(pig_top).size(50f, 50f);
        table3.row();

        Table row_2 = new Table();

        Image pig_left = new Image(pigTexture);
        row_2.add(pig_left).size(50f, 50f).padRight(20);
        row_2.columnDefaults(0);

        Image box = new Image(wood_box_tex);
        row_2.add(box).size(50f, 50f).padRight(20);
        row_2.columnDefaults(0);

        Image pig_right = new Image(pigTexture);
        row_2.add(pig_right).size(50f, 50f);
        row_2.columnDefaults(0);

        table3.add(row_2).center();
        table3.row();

        Image wood_h = new Image(wood_h_tex);
        table3.add(wood_h).size(250f, 20f).padBottom(0);

        table3.row();

        Table last_row = new Table();

        Image wood_v1 = new Image(wood_v_tex);
        last_row.add(wood_v1).size(20f, 50f).padRight(150);

        Image wood_v2 = new Image(wood_v_tex);
        last_row.add(wood_v2).size(20f, 50f);

        table3.add(last_row).center();
        table3.row();

        table3.padLeft(250);
        table3.padTop(70f);

        stage.addActor(table);
        stage.addActor(table2);
        stage.addActor(table3);

        ClickListener pauseButtonListener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new PauseScreen(game));
            }
        };

        pauseButtonImage.addListener(pauseButtonListener);
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

        stage.act(v);

        spriteBatch.end();

        stage.draw();

        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            game.setScreen(new EndScreen(game));
        }
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

    }
}
