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
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GameScreen implements Screen {
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

    public GameScreen(Game game) {
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

        UIskin = new Skin(Gdx.files.internal("skins/uiskin.json"));

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);

        Image pauseButtonImage = new Image(PAUSE);
        pauseButtonImage.setSize(30f, 30f);
        table.top().right();  // Set the table alignment to top-left
        table.add(pauseButtonImage).size(50f, 50f).padTop(20).padRight(60);
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

        stage.addActor(table);
        stage.addActor(table2);

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

    }
}
