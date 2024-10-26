package com.AngryBirds;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class SettingsScreen implements Screen {
    private Stage stage;
    private Skin UIskin;
    private Texture background;
    private SpriteBatch spriteBatch;
    private Game game;

    private Sprite LOGO;
    private Sprite VOLUME;
    private Sprite BRIGHTNESS;
    private Sprite BACK;

    public SettingsScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        spriteBatch = new SpriteBatch();
        stage = new Stage(new ScreenViewport());
        background = new Texture(Gdx.files.internal("homeBackground.png"));

        Gdx.input.setInputProcessor(stage);
        UIskin = new Skin(Gdx.files.internal("skins/uiskin.json"));

        Texture logoTexture = new Texture(Gdx.files.internal("settingsLogo.png"));
        LOGO = new Sprite(logoTexture);
        LOGO.setSize(400f, 80f);

        Texture backTexture = new Texture(Gdx.files.internal("exit_app.png"));
        BACK = new Sprite(backTexture);
        BACK.setSize(300f, 100f);

        Texture volumeTexture = new Texture(Gdx.files.internal("volumeButton.png"));
        VOLUME = new Sprite(volumeTexture);
        VOLUME.setSize(100f, 40f);

        Texture brightnessTexture = new Texture(Gdx.files.internal("brightnessButton.png"));
        BRIGHTNESS = new Sprite(brightnessTexture);
        BRIGHTNESS.setSize(100f, 40f);

        Table table = new Table();
        table.setFillParent(true);

        Image logoButtonImage = new Image(LOGO);
        logoButtonImage.setSize(400f, 100f);
        table.add(logoButtonImage).size(400f, 110f).center().padBottom(20f);
        table.row();

        Table controlsTable = new Table();

        Image volumeButtonImage = new Image(volumeTexture);
        volumeButtonImage.setSize(100f, 60f);
        Slider vslide = new Slider(0, 100, 1, false, UIskin);
        vslide.setValue(50);

        Image brightnessButtonImage = new Image(brightnessTexture);
        brightnessButtonImage.setSize(100f, 80f);
        Slider bslide = new Slider(0, 100, 1, false, UIskin);
        bslide.setValue(50);

        controlsTable.add(volumeButtonImage).size(180f, 45f).center().padRight(30f);
        controlsTable.columnDefaults(0);
        controlsTable.add(brightnessButtonImage).size(220f, 45f).center().padRight(30f);

        controlsTable.row();

        controlsTable.add(vslide).width(150f).center().padBottom(10f);
        controlsTable.columnDefaults(0);
        controlsTable.add(bslide).width(150f).center().padBottom(10f);

        table.add(controlsTable).center().padBottom(30f);
        table.row();

        Image backButtonImage = new Image(BACK);
        backButtonImage.setSize(125f, 80f);
        stage.addActor(backButtonImage);
        backButtonImage.setPosition(20f, 20f);

        stage.addActor(table);

        ChangeListener volumeListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                System.out.println("Volume changed to: " + vslide.getValue());
            }
        };

        ChangeListener brightnessListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                System.out.println("Brightness changed to: " + bslide.getValue());
            }
        };

        ClickListener backButtonListener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new HomeScreen(game));
            }
        };

        backButtonImage.addListener(backButtonListener);
        vslide.addListener(volumeListener);
        bslide.addListener(brightnessListener);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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
        BACK.getTexture().dispose();
        LOGO.getTexture().dispose();
    }
}
