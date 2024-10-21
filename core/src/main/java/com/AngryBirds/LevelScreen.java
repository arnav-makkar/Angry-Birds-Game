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

public class LevelScreen implements Screen {
    private Stage stage;
    private Skin UIskin;
    private Texture background;
    private SpriteBatch spriteBatch;
    private Game game;

    private Sprite LEVEL1;
    private Sprite LEVEL2;
    private Sprite LEVEL3;
    private Sprite LEVEL4;
    private Sprite BACK;

    public LevelScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        spriteBatch = new SpriteBatch();
        background = new Texture(Gdx.files.internal("levelsBackground.png"));

        Texture levelTexture1 = new Texture(Gdx.files.internal("level1.png"));
        Texture levelTexture2 = new Texture(Gdx.files.internal("level2.png"));
        Texture levelTexture3 = new Texture(Gdx.files.internal("level3.png"));
        Texture levelTexture4 = new Texture(Gdx.files.internal("level4.png"));
        Texture backTexture = new Texture(Gdx.files.internal("back.png"));

        LEVEL1 = new Sprite(levelTexture1);
        LEVEL2 = new Sprite(levelTexture2);
        LEVEL3 = new Sprite(levelTexture3);
        LEVEL4 = new Sprite(levelTexture4);
        BACK = new Sprite(backTexture);

        UIskin = new Skin(Gdx.files.internal("skins/uiskin.json"));
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Using Table layout
        Table table = new Table();
        table.setFillParent(true);
        table.center();

        // Create images for each level
        Image l1ButtonImage = new Image(LEVEL1);
        Image l2ButtonImage = new Image(LEVEL2);
        Image l3ButtonImage = new Image(LEVEL3);
        Image l4ButtonImage = new Image(LEVEL4);
        Image backButtonImage = new Image(BACK);

        // Add Level 1 and Level 2 in the first row
        table.add(l1ButtonImage).size(100f, 100f).pad(10f);
        table.add(l2ButtonImage).size(100f, 100f).pad(10f);
        table.row(); // Move to next row

        // Add Level 3 and Level 4 in the second row
        table.add(l3ButtonImage).size(100f, 100f).pad(10f);
        table.add(l4ButtonImage).size(100f, 100f).pad(10f);
        table.row(); // Move to next row

        // Add the Back button in the third row, centered
        table.add(backButtonImage).colspan(2).center().size(150f, 60f).padTop(20f);

        stage.addActor(table);

        // Button listeners
        l1ButtonImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Logic for Level 1
            }
        });

        l2ButtonImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Logic for Level 2
            }
        });

        l3ButtonImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Logic for Level 3
            }
        });

        l4ButtonImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Logic for Level 4
            }
        });

        // Back button listener
        backButtonImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new HomeScreen(game));
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();

        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spriteBatch.end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
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
        stage.dispose();
        LEVEL1.getTexture().dispose();
        LEVEL2.getTexture().dispose();
        LEVEL3.getTexture().dispose();
        LEVEL4.getTexture().dispose();
        BACK.getTexture().dispose();
        background.dispose();
    }
}
