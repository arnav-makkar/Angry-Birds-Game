package com.AngryBirds;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Screen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ClassTest {
    private Texture mockBirdTexture;
    private Stage mockStage;
    private Viewport mockViewport;
    private L1Screen l1Screen;
    private SettingsScreen settingsScreen;
    private LevelSuccessScreen levelSuccessScreen;
    private World world;
    private Game game;
    private Image mockPauseButton;

    @BeforeEach
    void init(){
        game = mock(Game.class);
        mockStage = mock(Stage.class);
        mockViewport = mock(Viewport.class);


        when(mockStage.getViewport()).thenReturn(mockViewport);
        Texture mockTexture = mock(Texture.class);
        mockPauseButton = new Image(mockTexture);
        l1Screen = new L1Screen(game);
        settingsScreen = new SettingsScreen(mockStage);

        Gdx.graphics = mock(Graphics.class);
        world=new World(new Vector2(0,-10.0f),true);
        l1Screen = new L1Screen(world);
        levelSuccessScreen=new LevelSuccessScreen(game,100,1);
        game = new Game() {
            @Override
            public void create() {
                setScreen(l1Screen);
            }

            @Override
            public void setScreen(Screen screen) {
                super.setScreen(screen);
            }

            @Override
            public Screen getScreen() {
                return super.getScreen();
            }
        };
    }

    @Test
    void testCreateGroundObj() {
        l1Screen.create_Ground_obj(0, 0, 0, 0);

        List<Body> createBodies = l1Screen.getCreatedBodies();

        assertEquals(1, createBodies.size(), "body is created");
        Body createBody = createBodies.get(0);

        assertEquals(BodyDef.BodyType.StaticBody, createBody.getType());
    }


    @Test
    void testScreenToWorldCoordinates() {

        when(Gdx.graphics.getHeight()).thenReturn(1080);
        int screenX = 300;
        int screenY = 300;

        Vector2 expect = new Vector2(screenX / 100f, (1080 - screenY) / 100f);
        Vector2 result = l1Screen.screenToWorldCoordinates(screenX, screenY);

        assertEquals(expect.x, result.x, 0.001);
        assertEquals(expect.y, result.y, 0.001);
    }


    @Test
    void testResize() {
        int width = 800;
        int height = 600;
        settingsScreen.resize(width, height);
        verify(mockViewport, times(1)).update(width, height, true);
    }

    @Test
    void testCalcHS() {
        int gameNum = levelSuccessScreen.game_num;
        int expectScore = 0;

        int result = levelSuccessScreen.calcHS(gameNum);

        assertEquals(expectScore, result);
    }
}
