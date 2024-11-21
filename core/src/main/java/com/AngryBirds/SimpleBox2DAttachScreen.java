package com.AngryBirds;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.DistanceJoint;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

import java.util.LinkedList;
import java.util.Queue;

public class SimpleBox2DAttachScreen implements Screen {
    private static final float PPM = 100f;
    private static final float LAUNCH_MULTIPLIER = 1f;

    private SpriteBatch batch;
    private Texture birdTexture;
    private Texture boxTexture;
    private Texture background;

    private World world;
    private Box2DDebugRenderer debugRenderer;
    private Body catapultBaseBody;
    private Body catapultArmBody;
    private Body woodBody;

    private RevoluteJoint catapultJoint;
    private DistanceJoint ballJoint;

    private boolean isDragging = false;
    private Vector2 dragStart;

    private Queue<Body> birdsQueue;
    private final LinkedList<Body> allBirds = new LinkedList<>();
    private Body currentBird;

    public SimpleBox2DAttachScreen(Game game) {}

    @Override
    public void show() {
        batch = new SpriteBatch();
        birdTexture = new Texture("redBird.png");
        boxTexture = new Texture("catapult.png");
        background = new Texture(Gdx.files.internal("game_screenBG.png"));

        world = new World(new Vector2(0, -10.0f), true);
        debugRenderer = new Box2DDebugRenderer();

        BodyDef baseDef = new BodyDef();
        baseDef.type = BodyDef.BodyType.StaticBody;
        baseDef.position.set(3f, 3f);
        catapultBaseBody = world.createBody(baseDef);

        PolygonShape baseShape = new PolygonShape();
        baseShape.setAsBox(0.2f, 0.5f);
        catapultBaseBody.createFixture(baseShape, 0);
        baseShape.dispose();

        BodyDef armDef = new BodyDef();
        armDef.type = BodyDef.BodyType.DynamicBody;
        armDef.position.set(3f, 2f);
        catapultArmBody = world.createBody(armDef);

        PolygonShape armShape = new PolygonShape();
        armShape.setAsBox(0.3f, 0.5f);
        FixtureDef armFixtureDef = new FixtureDef();
        armFixtureDef.shape = armShape;
        armFixtureDef.density = 1f;
        catapultArmBody.createFixture(armFixtureDef);
        armShape.dispose();

        RevoluteJointDef revoluteDef = new RevoluteJointDef();
        revoluteDef.initialize(catapultBaseBody, catapultArmBody, new Vector2(3f, 2f));
        revoluteDef.enableMotor = true;
        revoluteDef.maxMotorTorque = 1000f;
        catapultJoint = (RevoluteJoint) world.createJoint(revoluteDef);

        createObstacle(6f, 1f);
        birdsQueue = new LinkedList<>();
        spawnNewBird();

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Vector2 worldCoords = screenToWorldCoordinates(screenX, screenY);

                if (currentBird != null && currentBird.getFixtureList().first().testPoint(worldCoords.x, worldCoords.y)) {
                    isDragging = true;
                    dragStart = worldCoords;
                    return true;
                }
                return false;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                if (isDragging) {
                    Vector2 dragEnd = screenToWorldCoordinates(screenX, screenY);
                    Vector2 launchVector = dragStart.sub(dragEnd).scl(LAUNCH_MULTIPLIER);

                    // Remove distance joint
                    if (ballJoint != null) {
                        world.destroyJoint(ballJoint);
                        ballJoint = null;
                    }

                    // Apply launch impulse
                    currentBird.applyLinearImpulse(launchVector, currentBird.getWorldCenter(), true);

                    isDragging = false;

                    // Spawn the next bird
                    spawnNewBird();
                }
                return true;
            }
        });
    }

    private void spawnNewBird() {
        if (!birdsQueue.isEmpty()) {
            currentBird = birdsQueue.poll();

            // Reset bird velocity
            currentBird.setLinearVelocity(0, 0);
            currentBird.setAngularVelocity(0);

            // Attach the bird to the catapult using a distance joint
            DistanceJointDef jointDef = new DistanceJointDef();
            jointDef.initialize(catapultArmBody, currentBird,
                catapultArmBody.getWorldCenter(),
                currentBird.getWorldCenter());
            jointDef.collideConnected = false;
            ballJoint = (DistanceJoint) world.createJoint(jointDef);
        } else {
            createBird(3f, 2f); // Create a new bird if the queue is empty
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

        // Set collision filters
        ballFixtureDef.filter.groupIndex = -1; // Negative group index to prevent collisions with catapult
        bird.createFixture(ballFixtureDef);
        ballShape.dispose();

        // Add bird to tracking lists
        allBirds.add(bird);
        currentBird = bird;

        // Attach the bird to the catapult using a distance joint
        DistanceJointDef jointDef = new DistanceJointDef();
        jointDef.initialize(catapultArmBody, bird,
            catapultArmBody.getWorldCenter(),
            bird.getWorldCenter());
        jointDef.collideConnected = false;
        ballJoint = (DistanceJoint) world.createJoint(jointDef);

        // Reset velocity to ensure stability
        currentBird.setLinearVelocity(0, 0);
        currentBird.setAngularVelocity(0);
    }
    private Vector2 screenToWorldCoordinates(int screenX, int screenY) {
        return new Vector2(screenX / PPM, (Gdx.graphics.getHeight() - screenY) / PPM);
    }

    private void createObstacle(float x, float y) {
        BodyDef obstacleDef = new BodyDef();
        obstacleDef.type = BodyDef.BodyType.StaticBody;
        obstacleDef.position.set(x, y);

        Body obstacleBody = world.createBody(obstacleDef);

        PolygonShape obstacleShape = new PolygonShape();
        obstacleShape.setAsBox(0.5f, 0.5f); // Dimensions: 1x1 meters

        FixtureDef obstacleFixtureDef = new FixtureDef();
        obstacleFixtureDef.shape = obstacleShape;
        obstacleFixtureDef.density = 1f;
        obstacleFixtureDef.friction = 0.6f;
        obstacleFixtureDef.restitution = 0.1f;

        obstacleBody.createFixture(obstacleFixtureDef);
        obstacleShape.dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        world.step(1 / 60f, 6, 2);

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        for (Body bird : allBirds) {
            Vector2 position = bird.getPosition();
            batch.draw(
                birdTexture,
                position.x * PPM - 16,
                position.y * PPM - 16,
                32,
                32
            );
        }

        // Render the catapult
        batch.draw(
            boxTexture,
            catapultArmBody.getPosition().x * PPM - 50,
            catapultArmBody.getPosition().y * PPM - 32,
            150,
            55
        );

        batch.end();

        debugRenderer.render(world, batch.getProjectionMatrix().cpy().scale(PPM, PPM, 0));
    }

    @Override
    public void dispose() {
        batch.dispose();
        birdTexture.dispose();
        boxTexture.dispose();
        background.dispose();
        world.dispose();
        debugRenderer.dispose();
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
