package com.AngryBirds;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.DistanceJoint;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.io.*;
import java.util.*;

import static java.lang.Math.max;

public class L2Screen implements Screen {
    private static final float PPM = 100f;
    private static final float LAUNCH_MULTIPLIER = 0.7f;
    private Stage stage;
    private Music music;
    private Sound clickSound;

    private Sprite PAUSE;
    private Body prevBird;

    private SpriteBatch batch;
    private Texture birdTexture;
    private Texture catapultTexture;
    private Texture background;
    private Texture woodBoxtex;
    private Texture pigTexture;
    private Texture king_pigTexture;
    private int highscore;

    private Texture redBirdTexture;
    private Texture yellowBirdTexture;
    private Texture blackBirdTexture;

    private Queue<Texture> birdTextQ;
    private Map<Body, Texture> birdTextM;

    private BitmapFont font;
    private float totalTime = 0f;

    private World world;
    private Box2DDebugRenderer debugRenderer;
    private Body catapultBaseBody;
    private Body catapultArmBody;

    private static final short CATEGORY_CATAPULT = 0x0001;
    private static final short CATEGORY_BIRD = 0x0002;
    private static final short CATEGORY_OBSTACLE = 0x0004;
    private static final short MASK_CATAPULT = CATEGORY_OBSTACLE;
    private static final short MASK_BIRD = CATEGORY_OBSTACLE;

    private RevoluteJoint catapultJoint;
    private DistanceJoint ballJoint;

    private boolean isDragging = false;
    private Vector2 dragStart;

    private LinkedList<Obstacle> obstacles = new LinkedList<>();
    private final LinkedList<Pig> pigs = new LinkedList<>();

    private Queue<Body> birdsQueue;
    private LinkedList<Body> allBirds = new LinkedList<>();
    private Body currentBird;
    private Game game;

    private int birdCount = 0;

    public L2Screen(Game game) {this.game = game;}

    @Override
    public void show() {
        batch = new SpriteBatch();
        birdTexture = new Texture("redBird.png");
        catapultTexture = new Texture("catapult.png");
        background = new Texture(Gdx.files.internal("game_screenBG.png"));
        woodBoxtex = new Texture("wood_box.png");
        pigTexture = new Texture("pig.png");
        king_pigTexture = new Texture("king_pig.png");

        redBirdTexture = new Texture("redBird.png");
        yellowBirdTexture = new Texture("yellowBird.png");
        blackBirdTexture = new Texture("blackBird.png");


        birdTextM = new HashMap<>();
        birdTextQ = new LinkedList<>();
        birdTextQ.add(redBirdTexture);
        birdTextQ.add(redBirdTexture);
        birdTextQ.add(yellowBirdTexture);
        birdTextQ.add(yellowBirdTexture);
        birdTextQ.add(blackBirdTexture);
        birdTextQ.add(blackBirdTexture);
        birdTextQ.add(createTransparentTexture(32, 32, 0f));

        music = Gdx.audio.newMusic(Gdx.files.internal(GameSettings.SONG_PATH));
        music.setLooping(true);
        music.setVolume(GameSettings.volume);
        music.play();

        font = new BitmapFont();
        font.getData().setScale(2f);

        Texture pauseTexture = new Texture(Gdx.files.internal("pauseButton.png"));
        PAUSE = new Sprite(pauseTexture);
        PAUSE.setSize(150f, 50f);

        stage = new Stage(new ScreenViewport());

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);

        Gdx.input.setInputProcessor(inputMultiplexer);

        if (world == null) {
            world = new World(new Vector2(0, -10.0f), true);
        }

        debugRenderer = new Box2DDebugRenderer();

        BodyDef baseDef = new BodyDef();
        baseDef.type = BodyDef.BodyType.StaticBody;
        baseDef.position.set(2f, 2.75f);
        catapultBaseBody = world.createBody(baseDef);

        PolygonShape baseShape = new PolygonShape();
        baseShape.setAsBox(0.2f, 0.5f);

        FixtureDef baseFixtureDef = new FixtureDef();
        baseFixtureDef.shape = baseShape;
        baseFixtureDef.filter.categoryBits = CATEGORY_CATAPULT;
        baseFixtureDef.filter.maskBits = MASK_CATAPULT;
        catapultBaseBody.createFixture(baseFixtureDef);
        baseShape.dispose();

        BodyDef armDef = new BodyDef();
        armDef.type = BodyDef.BodyType.DynamicBody;
        armDef.position.set(2f, 2.75f);
        catapultArmBody = world.createBody(armDef);

        PolygonShape armShape = new PolygonShape();
        armShape.setAsBox(0.3f, 0.5f);

        FixtureDef armFixtureDef = new FixtureDef();
        armFixtureDef.shape = armShape;
        armFixtureDef.filter.categoryBits = CATEGORY_CATAPULT;
        armFixtureDef.filter.maskBits = MASK_CATAPULT;
        catapultArmBody.createFixture(armFixtureDef);
        armShape.dispose();

        RevoluteJointDef revoluteDef = new RevoluteJointDef();
        revoluteDef.initialize(catapultBaseBody, catapultArmBody, new Vector2(2f, 2.75f));
        revoluteDef.enableMotor = true;
        revoluteDef.maxMotorTorque = 1000f;
        catapultJoint = (RevoluteJoint) world.createJoint(revoluteDef);

        create_Ground_obj(5.8f, 0.2f, 1.7f, 1f);
        create_Ground_obj(5.85f, 1.3f, 0.45f, 0.2f);
        create_Ground_obj(3f, -1f, 50f, 0.2f);
        create_Ground_obj(10f, 5f, 0.25f, 50f);
        create_Ground_obj(-1f, 5f, 0.25f, 50f);

        createObstacle(4.8f, 1.5f, woodBoxtex, 0.5f, 0.5f, 10);
        createPig(4.8f, 1.6f, pigTexture, 0.05f, 0.05f);

        createObstacle(5.9f, 2f, woodBoxtex, 0.8f, 0.8f, 10);
        createPig(5.9f, 2.2f, king_pigTexture, 0.22f, 0.22f);

        createObstacle(6.85f, 1.5f, woodBoxtex, 0.5f, 0.5f, 10);
        createPig(6.85f, 1.6f, pigTexture, 0.05f, 0.05f);

        Table table = new Table();
        table.setFillParent(true);

        Image pauseButtonImage = new Image(PAUSE);
        pauseButtonImage.setSize(30f, 30f);
        table.top().right();
        table.add(pauseButtonImage).size(60f, 55f).padTop(20).padRight(40);
        table.row();

        birdsQueue = new LinkedList<>();
        initNewBird();

        inputMultiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Vector2 worldCoords = screenToWorldCoordinates(screenX, screenY);

                if (currentBird != null && currentBird.getFixtureList().first().testPoint(worldCoords.x, worldCoords.y)) {
                    isDragging = true;
                    dragStart = worldCoords;

                    return true;
                }
                clickSound = Gdx.audio.newSound(Gdx.files.internal("click.mp3"));

                if(birdTextM.get(prevBird) != null){
                    if (birdTextM.get(prevBird).equals(blackBirdTexture)) {
                        if (Gdx.input.justTouched()) {
                            clickSound.play(0.25f);
                            triggerSpecialBlack(prevBird);
                        }
                    }
                }

                return false;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                if (isDragging) {
                    Vector2 worldCoords = screenToWorldCoordinates(screenX, screenY);

                    if (ballJoint != null) {
                        currentBird.setTransform(worldCoords, currentBird.getAngle());
                    }
                    return true;
                }
                return false;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                if (isDragging) {
                    Vector2 dragEnd = screenToWorldCoordinates(screenX, screenY);
                    Vector2 launchVector = dragStart.sub(dragEnd).scl(LAUNCH_MULTIPLIER);

                    if (ballJoint != null) {
                        world.destroyJoint(ballJoint);
                        ballJoint = null;
                    }

                    currentBird.applyLinearImpulse(launchVector, currentBird.getWorldCenter(), true);

                    isDragging = false;

                    birdCount+=1;
                    if(birdCount<=6){
                        prevBird = currentBird;
                        initNewBird();
                    }
                    else {
                        game.setScreen(new LevelFailScreen(game));
                    }
                }

                return true;
            }
        });

        stage.addActor(table);

        ClickListener pauseButtonListener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                saveState("savegame.dat");
                game.setScreen(new PauseScreen2(game));
            }
        };

        pauseButtonImage.addListener(pauseButtonListener);
    }

    private void triggerSpecialBlack(Body bird) {
        bird.setLinearVelocity(bird.getLinearVelocity().x*1.5f, bird.getLinearVelocity().y*-1f);
    }

    private void initNewBird() {
        if (!birdsQueue.isEmpty()) {
            currentBird = birdsQueue.poll();

            currentBird.setLinearVelocity(0, 0);
            currentBird.setAngularVelocity(0);

            DistanceJointDef jointDef = new DistanceJointDef();
            jointDef.initialize(catapultArmBody, currentBird,
                catapultArmBody.getWorldCenter(),
                currentBird.getWorldCenter());
            jointDef.collideConnected = false;
            ballJoint = (DistanceJoint) world.createJoint(jointDef);
        } else {
            createBird(2f, 2.75f);
        }
    }

    private void createBird(float x, float y) {
        BodyDef ballDef = new BodyDef();
        ballDef.type = BodyDef.BodyType.DynamicBody;
        ballDef.position.set(x, y);
        Body bird = world.createBody(ballDef);

        CircleShape ballShape = new CircleShape();
        ballShape.setRadius(0.2f);

        FixtureDef ballFixtureDef = new FixtureDef();
        ballFixtureDef.shape = ballShape;
        ballFixtureDef.density = 1f;
        ballFixtureDef.friction = 0.3f;
        ballFixtureDef.restitution = 0.5f;

        ballFixtureDef.filter.groupIndex = -1;
        bird.createFixture(ballFixtureDef);
        ballShape.dispose();

        allBirds.add(bird);
        currentBird = bird;

        if (!birdTextQ.isEmpty()) {
            Texture birdTexture = birdTextQ.poll();
            birdTextM.put(bird, birdTexture);
        }

        DistanceJointDef jointDef = new DistanceJointDef();
        jointDef.initialize(catapultArmBody, bird,
            catapultArmBody.getWorldCenter(),
            bird.getWorldCenter());
        jointDef.collideConnected = false;
        ballJoint = (DistanceJoint) world.createJoint(jointDef);

        currentBird.setLinearVelocity(0, 0);
        currentBird.setAngularVelocity(0);
    }

    private Vector2 screenToWorldCoordinates(int screenX, int screenY) {
        return new Vector2(screenX / PPM, (Gdx.graphics.getHeight() - screenY) / PPM);
    }

    private void create_Ground_obj(float x, float y, float x1, float y1){
        BodyDef obstacleDef = new BodyDef();
        obstacleDef.type = BodyDef.BodyType.StaticBody;
        obstacleDef.position.set(x, y);

        Body obstacleBody = world.createBody(obstacleDef);

        PolygonShape obstacleShape = new PolygonShape();
        obstacleShape.setAsBox(x1, y1);

        FixtureDef obstacleFixtureDef = new FixtureDef();
        obstacleFixtureDef.shape = obstacleShape;
        obstacleFixtureDef.density = 1f;
        obstacleFixtureDef.friction = 0.6f;
        obstacleFixtureDef.restitution = 0.1f;

        obstacleBody.createFixture(obstacleFixtureDef);
        obstacleShape.dispose();
    }

    private void createObstacle(float x, float y, Texture texture, float xscale, float yscale, int n) {
        float width = texture.getWidth() / PPM / 3;
        float height = texture.getHeight() / PPM / 3;

        BodyDef obstacleDef = new BodyDef();
        obstacleDef.type = BodyDef.BodyType.DynamicBody;
        obstacleDef.position.set(x, y);

        Body obstacleBody = world.createBody(obstacleDef);

        PolygonShape obstacleShape = new PolygonShape();
        obstacleShape.setAsBox(width*xscale/2, height*yscale/2);

        FixtureDef obstacleFixtureDef = new FixtureDef();
        obstacleFixtureDef.shape = obstacleShape;
        obstacleFixtureDef.density = 1f;
        obstacleFixtureDef.friction = 0.6f;
        obstacleFixtureDef.restitution = 0.2f;

        obstacleBody.createFixture(obstacleFixtureDef);
        obstacleShape.dispose();

        obstacles.add(new Obstacle(obstacleBody, texture, xscale, yscale, n));
    }

    private void createPig(float x, float y, Texture texture, float xscale, float yscale) {
        float width = texture.getWidth() / PPM / 3;
        float height = texture.getHeight() / PPM / 3;

        BodyDef obstacleDef = new BodyDef();
        obstacleDef.type = BodyDef.BodyType.DynamicBody;
        obstacleDef.position.set(x, y);

        Body obstacleBody = world.createBody(obstacleDef);

        PolygonShape obstacleShape = new PolygonShape();
        obstacleShape.setAsBox(width*xscale/2, height*yscale/2);

        FixtureDef obstacleFixtureDef = new FixtureDef();
        obstacleFixtureDef.shape = obstacleShape;
        obstacleFixtureDef.density = 1f;
        obstacleFixtureDef.friction = 0.6f;
        obstacleFixtureDef.restitution = 0.2f;

        obstacleBody.createFixture(obstacleFixtureDef);
        obstacleShape.dispose();

        pigs.add(new Pig(obstacleBody, texture, xscale, yscale));
    }

    @Override

    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(GameSettings.brightness, GameSettings.brightness * 0.5f, GameSettings.brightness * 0.3f, 1.0f);

        int totPigCount = 3;
        int cnt = 0;

        world.step(1 / 60f, 6, 2);
        totalTime += delta;

        batch.setColor(GameSettings.brightness, GameSettings.brightness, GameSettings.brightness, 1.0f);
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        batch.draw(redBirdTexture, 175, 0, 40, 40);
        batch.draw(redBirdTexture, 140, 0, 40, 40);
        batch.draw(yellowBirdTexture, 105, 0, 40, 40);
        batch.draw(yellowBirdTexture, 70, 0, 40, 40);
        batch.draw(blackBirdTexture, 35, 0, 40, 40);
        batch.draw(blackBirdTexture, 0, 0, 40, 40);

        String timerText = String.format("Total time: 20s\n   Timer: %.1f", totalTime);
        GlyphLayout layout = new GlyphLayout(font, timerText);
        font.draw(batch, timerText, Gdx.graphics.getWidth() - layout.width-380, Gdx.graphics.getHeight() - 20);

        batch.draw(
            catapultTexture,
            catapultArmBody.getPosition().x * PPM - 65,
            catapultArmBody.getPosition().y * PPM - 53,
            150,
            55
        );

        for (Body bird : allBirds) {
            Vector2 position = bird.getPosition();
            Texture texture = birdTextM.get(bird);
            batch.draw(texture, position.x * PPM - 16, position.y * PPM - 16, 32, 32);
        }

        for (Obstacle obstacle : obstacles) {
            Body body = obstacle.body;
            Texture texture = obstacle.texture;
            TextureRegion textureRegion = new TextureRegion(texture);

            Vector2 position = body.getPosition();
            float angle = (float) Math.toDegrees(body.getAngle());

            float width = (float) texture.getWidth()/3;
            float height = (float) texture.getHeight()/3;

            batch.draw(textureRegion, position.x * PPM - width / 2, position.y * PPM - height / 2, width / 2f,height / 2f, width, height, obstacle.x, obstacle.y, angle);
        }

        for (Pig pig : pigs) {
            Body body = pig.body;
            Texture texture = pig.texture;
            TextureRegion textureRegion = new TextureRegion(texture);

            Vector2 position = body.getPosition();
            float angle = (float) Math.toDegrees(body.getAngle());

            float width = (float) texture.getWidth()/3;
            float height = (float) texture.getHeight()/3;

            batch.draw(textureRegion, position.x * PPM - width / 2, position.y * PPM - height / 2, width / 2f,height / 2f, width, height, pig.x, pig.y, angle);

            if (pig.checkCollision()) {
                pigs.remove(pig);
                break;
            }
        }

        if (pigs.isEmpty() && totalTime<=20) {
            float score = (20 - totalTime) * 50;
            highscore = max(highscore, (int)score);

            List<String[]> data = new ArrayList<>();

            try (BufferedReader br = new BufferedReader(new FileReader("highscore.csv"))) {
                String line;

                while ((line = br.readLine()) != null) {
                    data.add(line.split(","));
                }

                if (data.size() > 1) {
                    highscore = Integer.parseInt(data.get(1)[1]);
                    data.get(1)[1] = String.valueOf(max(highscore, (int)score));
                }

                try (BufferedWriter bw = new BufferedWriter(new FileWriter("highscore.csv"))) {
                    for (String[] row : data) {
                        bw.write(String.join(",", row));
                        bw.newLine();
                    }
                }
            }
            catch (IOException e) {
                System.err.println("Error updating the file: " + e.getMessage());
            }

            game.setScreen(new LevelSuccessScreen(this.game, score, 1));
        }

        if(totalTime>20){
            game.setScreen(new LevelFailScreen(this.game));
        }

        stage.act(delta);
        stage.draw();
        batch.end();
//        debugRenderer.render(world, batch.getProjectionMatrix().cpy().scale(PPM, PPM, 0));
    }

    @Override
    public void dispose() {
        batch.dispose();
        music.dispose();
        birdTexture.dispose();
        catapultTexture.dispose();
        background.dispose();
        world.dispose();
        debugRenderer.dispose();

    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
        music.stop();
    }

    private void saveState(String filePath) {
        try (ObjectOutputStream Objo = new ObjectOutputStream(new FileOutputStream(filePath))) {
            GameState gameState = new GameState();
            gameState.totalTime = totalTime;
            gameState.birdCount = birdCount;

            gameState.birdStates = new LinkedList<>();
            for (Body bird : allBirds) {
                Vector2 pos = bird.getPosition();
                Vector2 vel = bird.getLinearVelocity();
                Texture birdTexture = birdTextM.get(bird);
                gameState.birdStates.add(new BodyState(pos.x, pos.y, vel.x, vel.y, bird.getAngle(),birdTexture));
            }

            gameState.obstacles = obstacles;
            gameState.pigs = pigs;

            Objo.writeObject(gameState);
            System.out.println("Game Successfully Saved!");
            System.out.println("Game Paused");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Texture createTransparentTexture(int width, int height, float alpha) {
        alpha = Math.max(0, Math.min(alpha, 1));

        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);

        pixmap.setColor(1, 1, 1, alpha);
        pixmap.fill();

        Texture texture = new Texture(pixmap);

        pixmap.dispose();

        return texture;
    }

    public void loadState(String fileName) {
        if (birdTextM == null) {
            birdTextM = new HashMap<>();
        }
        if (allBirds == null) {
            allBirds = new LinkedList<>();
        }
        if (birdsQueue == null) {
            birdsQueue = new LinkedList<>();
        }
        if (obstacles == null) {
            obstacles = new LinkedList<>();
        }

        try (ObjectInputStream Obji = new ObjectInputStream(new FileInputStream(fileName))) {
            GameState gameState = (GameState) Obji.readObject();

            if (world != null) world.dispose();
            world = new World(new Vector2(0, -10.0f), true);

            allBirds.clear();
            obstacles.clear();
            pigs.clear();
            birdTextM.clear();

            totalTime = gameState.totalTime;
            birdCount = gameState.birdCount;

            for (BodyState bodyState : gameState.birdStates) {
                BodyDef ballDef = new BodyDef();
                ballDef.type = BodyDef.BodyType.DynamicBody;
                ballDef.position.set(bodyState.posX, bodyState.posY);
                ballDef.angle = bodyState.angle;

                Body bird = world.createBody(ballDef);

                CircleShape ballShape = new CircleShape();
                ballShape.setRadius(0.2f);

                FixtureDef ballFixtureDef = new FixtureDef();
                ballFixtureDef.shape = ballShape;
                ballFixtureDef.density = 1f;
                ballFixtureDef.friction = 0.3f;
                ballFixtureDef.restitution = 0.5f;

                bird.createFixture(ballFixtureDef);
                ballShape.dispose();

                bird.setLinearVelocity(bodyState.velX, bodyState.velY);

                Texture birdTexture;
                switch (bodyState.birdType) {
                    case "red":
                        birdTexture = redBirdTexture;
                        break;
                    case "yellow":
                        birdTexture = yellowBirdTexture;
                        break;
                    case "black":
                        birdTexture = blackBirdTexture;
                        break;
                    default:
                        birdTexture = redBirdTexture;
                        break;
                }
                birdTextM.put(bird, birdTexture);
                allBirds.add(bird);
            }

            obstacles.clear();
            for (Obstacle oldObstacle : gameState.obstacles) {
                createObstacle(oldObstacle.body.getPosition().x, oldObstacle.body.getPosition().y, woodBoxtex, oldObstacle.x, oldObstacle.y, 10);
            }

            pigs.clear();
            for (Pig oldPig : gameState.pigs) {
                createPig(oldPig.body.getPosition().x, oldPig.body.getPosition().y, pigTexture, oldPig.x, oldPig.y);
            }

            if (!allBirds.isEmpty()) {
                currentBird = allBirds.getLast();
            }
            if (gameState.birdStates == null || gameState.birdStates.isEmpty()) {
                System.out.println("no birds so cant load");
                return;
            }
            System.out.println("successful game load");
        } catch (Exception e) {
//            System.out.println("failed to load");
            System.out.println("Game Resumed");
            System.out.println("Game Successfully Loaded!\n");
        }
    }
}
