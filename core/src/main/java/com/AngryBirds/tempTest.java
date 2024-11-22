package com.AngryBirds;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.DistanceJoint;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class tempTest implements Screen {
    private static final float PPM = 100f;
    private static final float LMULT = 0.5f;

    private SpriteBatch batch;
    private Texture birdTexture;
    private Texture catapultTexture;
    private Texture background;
    private Texture woodBoxtex;
    private Texture pigTexture;

    private Texture redBirdTexture;
    private Texture yellowBirdTexture;
    private Texture blackBirdTexture;

    private Queue<Texture> birdTextQ;
    private Map<Body, Texture> birdTextM;

    private World world;
    private Box2DDebugRenderer debugRenderer;
    private Body catapultBaseBody;
    private Body catapultArmBody;

    private RevoluteJoint catapultJoint;
    private DistanceJoint ballJoint;

    private boolean dragged = false;
    private Vector2 dragStart;

    private final LinkedList<Obstacle> obstacles = new LinkedList<>();

    private Queue<Body> birdsQueue;
    private final LinkedList<Body> allBirds = new LinkedList<>();
    private Body currentBird;

    private BitmapFont font;
    private float totalTime = 0f;

    public tempTest(Game game) {}

    @Override
    public void show() {
        batch = new SpriteBatch();
        catapultTexture = new Texture("catapult.png");
        background = new Texture(Gdx.files.internal("game_screenBG.png"));
        woodBoxtex = new Texture("wood_box.png");
        pigTexture = new Texture("pig.png");

        redBirdTexture = new Texture("redBird.png");
        yellowBirdTexture = new Texture("yellowBird.png");
        blackBirdTexture = new Texture("blackBird.png");

        birdTextM = new HashMap<>();
        birdTextQ = new LinkedList<>();
        birdTextQ.add(redBirdTexture);
        birdTextQ.add(yellowBirdTexture);
        birdTextQ.add(blackBirdTexture);
        birdTextQ.add(redBirdTexture);
        birdTextQ.add(yellowBirdTexture);
        birdTextQ.add(blackBirdTexture);

        font = new BitmapFont();
        font.getData().setScale(2f);

        world = new World(new Vector2(0, -10.0f), true);
        debugRenderer = new Box2DDebugRenderer();

        BodyDef baseDef = new BodyDef();
        baseDef.type = BodyDef.BodyType.StaticBody;
        baseDef.position.set(2f, 2.75f);
        catapultBaseBody = world.createBody(baseDef);

        PolygonShape baseShape = new PolygonShape();
        baseShape.setAsBox(0.2f, 0.5f);
        catapultBaseBody.createFixture(baseShape, 0);
        baseShape.dispose();

        BodyDef armDef = new BodyDef();
        armDef.type = BodyDef.BodyType.DynamicBody;
        armDef.position.set(2f, 2.75f);
        catapultArmBody = world.createBody(armDef);

        PolygonShape armShape = new PolygonShape();
        armShape.setAsBox(0.3f, 0.5f);
        FixtureDef armFixtureDef = new FixtureDef();
        armFixtureDef.shape = armShape;
        armFixtureDef.density = 1f;
        catapultArmBody.createFixture(armFixtureDef);
        armShape.dispose();

        RevoluteJointDef revoluteDef = new RevoluteJointDef();
        revoluteDef.initialize(catapultBaseBody, catapultArmBody, new Vector2(2f, 2.75f));
        catapultJoint = (RevoluteJoint) world.createJoint(revoluteDef);

        create_Ground_obj(5.8f, 0.7f, 1.7f, 0.5f);
        create_Ground_obj(5.85f, 1.3f, 0.45f, 0.2f);

        createObstacle(5.9f, 2f, woodBoxtex, 0.8f, 0.8f);
        createObstacle(5.9f, 2.2f, pigTexture, 0.1f, 0.1f);

        birdsQueue = new LinkedList<>();
        initNewBird();

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Vector2 worldCoords = screenToWorldCoordinates(screenX, screenY);
                if (currentBird != null && currentBird.getFixtureList().first().testPoint(worldCoords.x, worldCoords.y)) {
                    dragged = true;
                    dragStart = worldCoords;
                    return true;
                }
                return false;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                if (dragged) {
                    Vector2 dragEnd = screenToWorldCoordinates(screenX, screenY);
                    Vector2 launchVector = dragStart.sub(dragEnd).scl(LMULT);
                    if (ballJoint != null) {
                        world.destroyJoint(ballJoint);
                        ballJoint = null;
                    }

                    currentBird.applyLinearImpulse(launchVector, currentBird.getWorldCenter(), true);
                    dragged = false;
                    initNewBird();
                }
                return true;
            }
        });
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
        obstacleShape.setAsBox(x1, y1); // Dimensions: 1x1 meters

        FixtureDef obstacleFixtureDef = new FixtureDef();
        obstacleFixtureDef.shape = obstacleShape;
        obstacleFixtureDef.density = 1f;
        obstacleFixtureDef.friction = 0.6f;
        obstacleFixtureDef.restitution = 0.1f;

        obstacleBody.createFixture(obstacleFixtureDef);
        obstacleShape.dispose();
    }

    private void createObstacle(float x, float y, Texture texture, float xscale, float yscale) {
        float width = texture.getWidth() / PPM / 3;
        float height = texture.getHeight() / PPM / 3;

        BodyDef obstacleDef = new BodyDef();
        obstacleDef.type = BodyDef.BodyType.DynamicBody; // Allow it to move
        obstacleDef.position.set(x, y);

        Body obstacleBody = world.createBody(obstacleDef);

        PolygonShape obstacleShape = new PolygonShape();
        obstacleShape.setAsBox(width*xscale/2, height*yscale/2);

        FixtureDef obstacleFixtureDef = new FixtureDef();
        obstacleFixtureDef.shape = obstacleShape;
        obstacleFixtureDef.density = 1f; // Adjust density
        obstacleFixtureDef.friction = 0.6f;
        obstacleFixtureDef.restitution = 0.2f;

        obstacleBody.createFixture(obstacleFixtureDef);
        obstacleShape.dispose();

        obstacles.add(new Obstacle(obstacleBody, texture, xscale, yscale));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        world.step(1 / 60f, 6, 2);
        totalTime += delta;

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        batch.draw(redBirdTexture, 0, 0, 40, 40);
        batch.draw(yellowBirdTexture, 35, 0, 40, 40);
        batch.draw(blackBirdTexture, 70, 0, 40, 40);
        batch.draw(redBirdTexture, 105, 0, 40, 40);
        batch.draw(yellowBirdTexture, 140, 0, 40, 40);
        batch.draw(blackBirdTexture, 175, 0, 40, 40);

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

            batch.draw(textureRegion, position.x * PPM - width / 2, position.y * PPM - height / 2, width / 2f,height / 2f, width/1,height/1, obstacle.x,obstacle.y, angle/1
            );
        }

        String timerText = String.format("test timer: %.1f", totalTime);
        GlyphLayout layout = new GlyphLayout(font, timerText);
        font.draw(batch, timerText, Gdx.graphics.getWidth() - layout.width - 20, Gdx.graphics.getHeight() - 20);

        batch.end();

        debugRenderer.render(world, batch.getProjectionMatrix().cpy().scale(PPM, PPM, 0));
    }

    @Override
    public void dispose() {
        batch.dispose();
        birdTexture.dispose();
        catapultTexture.dispose();
        background.dispose();
        world.dispose();
        debugRenderer.dispose();
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
