//package com.AngryBirds;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Screen;
//import com.badlogic.gdx.graphics.GL20;
//import com.badlogic.gdx.graphics.OrthographicCamera;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.Sprite;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.badlogic.gdx.scenes.scene2d.InputEvent;
//import com.badlogic.gdx.scenes.scene2d.Stage;
//import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
//import com.badlogic.gdx.scenes.scene2d.ui.Image;
//import com.badlogic.gdx.scenes.scene2d.ui.Skin;
//import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
//import com.badlogic.gdx.utils.viewport.ScreenViewport;
//
///** First screen of the application. Displayed after the application is created. */
//public class LevelScreen implements Screen {
//    private Stage stage;
//    private Skin UIskin;
//    private Texture background;
//    private SpriteBatch spriteBatch;
//
//    private Sprite LEVEL1;
//    private Sprite LEVEL2;
//    private Sprite LEVEL3;
//    private Sprite LEVEL4;
//
//    private Sprite BACK;
//
//    @Override
//    public void show() {
//        spriteBatch=new SpriteBatch();
//        background =new Texture(Gdx.files.internal());
//
//        Texture levelTexture=new Texture(Gdx.files.internal());
//        LEVEL1=new Sprite(levelTexture);
//        LEVEL2=new Sprite(levelTexture);
//        LEVEL3=new Sprite(levelTexture);
//        LEVEL4=new Sprite(levelTexture);
//
//        LEVEL1.setSize(200f,200f);
//        LEVEL2.setSize(200f,200f);
//        LEVEL3.setSize(200f,200f);
//        LEVEL4.setSize(200f,200f);
//
//        UIskin=new Skin(Gdx.files.internal());
//        stage=new Stage(new ScreenViewport());
//        Gdx.input.setInputProcessor(stage);
//
//        HorizontalGroup horizontalGroup=new HorizontalGroup();
//        horizontalGroup.setFillParent(true);
//
//        Image l1ButtonImage=new Image(LEVEL1);
//        l1ButtonImage.setSize(200f,200f);
//        horizontalGroup.addActor(l1ButtonImage);
//
//        Image l2ButtonImage=new Image(LEVEL2);
//        l2ButtonImage.setSize(200f,200f);
//        horizontalGroup.addActor(l1ButtonImage);
//
//        Image l3ButtonImage=new Image(LEVEL3);
//        l3ButtonImage.setSize(200f,200f);
//        horizontalGroup.addActor(l1ButtonImage);
//
//        Image l4ButtonImage =new Image(LEVEL4);
//        l4ButtonImage.setSize(200f,200f);
//        horizontalGroup.addActor(l4ButtonImage);
//
//        stage.addActor(horizontalGroup);
//
//        ClickListener l1Listner = new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//            }
//        };
//        ClickListener l2Listner = new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//            }
//        };
//        ClickListener l3Listner = new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//            }
//        };
//        ClickListener l4Listner = new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//            }
//        };
//
//        l1ButtonImage.addListener(l1Listner);
//        l2ButtonImage.addListener(l1Listner);
//        l3ButtonImage.addListener(l1Listner);
//        l4ButtonImage.addListener(l1Listner);
//    }
//
//    @Override
//    public void render(float delta) {
//        Gdx.gl.glClearColor(1, 1, 1, 1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//
//        OrthographicCamera camera=new OrthographicCamera();
//        camera.setToOrtho(false,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
//        camera.update();
//
//        spriteBatch.setProjectionMatrix(camera.combined);
//        spriteBatch.begin();
//        spriteBatch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
//        spriteBatch.end();
//
//        stage.act((Math.min(Gdx.graphics.getDeltaTime(),1/30f));
//        stage.draw();
//    }
//
//    @Override
//    public void resize(int width, int height) {
//        stage.getViewport().update(width, height, true);
//
//    }
//
//    @Override
//    public void pause() {
//        // Invoked when your application is paused.
//    }
//
//    @Override
//    public void resume() {
//        // Invoked when your application is resumed after pause.
//    }
//
//    @Override
//    public void hide() {
//        dispose();
//    }
//
//    @Override
//    public void dispose() {
//        stage.dispose();
//        LEVEL1.getTexture().dispose();
//        LEVEL2.getTexture().dispose();
//        LEVEL3.getTexture().dispose();
//        LEVEL4.getTexture().dispose();
//
//        BACK.getTexture().dispose();
//    }
//}
