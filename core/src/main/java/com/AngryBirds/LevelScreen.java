package com.AngryBirds;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
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

import java.util.Random;

public class LevelScreen implements Screen {
    private Stage stage;
    private Skin UIskin;
    private Texture background;
    private SpriteBatch spriteBatch;
    private Music music;

    private Game game;

    private Sprite LEVEL1;
    private Sprite LEVEL2;
    private Sprite LEVEL3;
    private Sprite LEVEL4;
    private Sprite BACK;
    private Sprite random;

    public LevelScreen(Game game) {
        this.game=game;
    }

    @Override
    public void show() {
        spriteBatch=new SpriteBatch();
        background =new Texture(Gdx.files.internal("levelsBackground 2.png"));

        Texture levelTexture1 = new Texture(Gdx.files.internal("level1.png"));
        Texture levelTexture2 = new Texture(Gdx.files.internal("level2.png"));
        Texture levelTexture3 = new Texture(Gdx.files.internal("level3.png"));
        Texture levelTexture4 = new Texture(Gdx.files.internal("level4.png"));

        Texture backTexture = new Texture(Gdx.files.internal("exit_app.png"));
        BACK = new Sprite(backTexture);

        Texture randomtex = new Texture(Gdx.files.internal("random.png"));
        random = new Sprite(randomtex);

        music = Gdx.audio.newMusic(Gdx.files.internal(GameSettings.SONG_PATH));
        music.setLooping(true);
        music.setVolume(GameSettings.volume);
        music.play();

        LEVEL1 = new Sprite(levelTexture1);
        LEVEL2 = new Sprite(levelTexture2);
        LEVEL3 = new Sprite(levelTexture3);
        LEVEL4 = new Sprite(levelTexture4);

        LEVEL1.setSize(200f,200f);
        LEVEL2.setSize(200f,200f);
        LEVEL3.setSize(200f,200f);
        LEVEL4.setSize(150f,150f);

        UIskin = new Skin(Gdx.files.internal("skins/uiskin.json"));
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);

        table.center().padBottom(75);

        Image l1ButtonImage=new Image(LEVEL1);
        l1ButtonImage.setSize(200f,200f);
        table.add(l1ButtonImage).size(180f, 60f).pad(20f);
        table.columnDefaults(0).left();

        Image l2ButtonImage=new Image(LEVEL2);
        l2ButtonImage.setSize(200f,200f);
        table.add(l2ButtonImage).size(180f, 60f).pad(20f);
        table.columnDefaults(0).left();

        Image l3ButtonImage=new Image(LEVEL3);
        l3ButtonImage.setSize(200f,200f);
        table.add(l3ButtonImage).size(180f, 60f).pad(20f);
        table.columnDefaults(0).left();

        Image l4ButtonImage =new Image(LEVEL4);
        l4ButtonImage.setSize(100f,100f);
        table.add(l4ButtonImage).size(180f, 60f).pad(20f);
        table.columnDefaults(0).left();

        stage.addActor(table);

        Image backButtonImage = new Image(BACK);
        backButtonImage.setSize(100f, 67f);
        stage.addActor(backButtonImage);
        backButtonImage.setPosition(20f, 20f);

        Color color = backButtonImage.getColor();
        backButtonImage.setColor(color.r, color.g, color.b, 0.6f);

        Image random_btn_img = new Image(random);
        random_btn_img.setSize(450, 60);
        stage.addActor(random_btn_img);
        random_btn_img.setPosition(240, 100);

        ClickListener backButtonListener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new HomeScreen(game));
            }
        };

        ClickListener l1Listener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new L1Screen(game));
            }
        };

        ClickListener l2Listener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new L2Screen(game));
            }
        };

        ClickListener l3Listener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new L3Screen(game));
            }
        };

        ClickListener l4Listener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new L4Screen(game));
            }
        };

        l1ButtonImage.addListener(l1Listener);
        l2ButtonImage.addListener(l2Listener);
        l3ButtonImage.addListener(l3Listener);
        l4ButtonImage.addListener(l4Listener);
        backButtonImage.addListener(backButtonListener);

        random_btn_img.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Random random = new Random();

                int choice = random.nextInt(3);
                switch (choice) {
                    case 0:
                        game.setScreen(new L2Screen(game));
                        break;
                    case 1:
                        game.setScreen(new L3Screen(game));
                        break;
                    case 2:
                        game.setScreen(new L4Screen(game));
                        break;
                }
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        OrthographicCamera camera=new OrthographicCamera();
        camera.setToOrtho(false,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        camera.update();

        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        spriteBatch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        spriteBatch.end();

        stage.act((Math.min(Gdx.graphics.getDeltaTime(),1/30f)));
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
        music.stop();
    }

    @Override
    public void dispose() {
        stage.dispose();
        LEVEL1.getTexture().dispose();
        LEVEL2.getTexture().dispose();
        LEVEL3.getTexture().dispose();
        LEVEL4.getTexture().dispose();
    }
}
