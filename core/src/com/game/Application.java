package com.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.*;
import com.game.screens.MainMenuScreen;
import com.game.screens.PlayScreen;
import static com.game.Constants.PPM;

import static com.game.Constants.PPM;

public class Application extends Game { //As far as I can tell, this is our "main" that isnt quite called main

    public SpriteBatch batch;
    private boolean DEBUG = false;

   // private boolean DEBUG = true;
    TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;
    private OrthographicCamera camera;

    private Box2DDebugRenderer b2dr;
    private World world;
    private Body player;

    
    @Override
    public void create ()
    {
    	batch = new SpriteBatch();
    	this.setScreen(new MainMenuScreen(this));

    	//setScreen(new PlayScreen(DEBUG));

    }
    
    @Override
    public void render() 
    {
    	super.render();
    }

    @Override
    public void resize(int width, int height)
    {
    super.resize(width, height);	
    }

    @Override
    public void dispose() {
        super.dispose();
    }
    /*
     * public void update(float delta) {
     * 	world.step(1 / 60f, 6, 2);
     * 	inputUpdate(delta);
     *	cameraUpdate(delta);
     * }
     */ 
    
    /* == DEPRECATED ==
    public void inputUpdate(float delta) {
        int horizontalForce = 0;

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            horizontalForce -= 1;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            horizontalForce += 1;
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            player.applyForceToCenter(0, 300, false);
        }

        player.setLinearVelocity(horizontalForce * 5, player.getLinearVelocity().y); //Updates player body linear velocity based on last key input
    }
    */

    public void cameraUpdate(float delta) {
        Vector3 position = camera.position;
        position.x = player.getPosition().x * PPM;
        position.y = player.getPosition().y * PPM;
        camera.position.set(position);

        camera.update();
    }

    public Body createPlatform(float x, float y, int width, int height, boolean isStatic) {
        Body pBody;
        BodyDef def = new BodyDef();
        

        if(isStatic)
            def.type = BodyDef.BodyType.StaticBody;
        else
            def.type = BodyDef.BodyType.DynamicBody;

        def.position.set(x / PPM, y / PPM);
        def.fixedRotation = true;
        pBody = world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2 / PPM, height / 2 / PPM);

        pBody.createFixture(shape, 1.0f);
        shape.dispose();
        return pBody;
    }
    
    
    
    
    
}
