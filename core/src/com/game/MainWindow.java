package com.game;

import static com.game.Constants.PPM;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.game.Application;
import com.game.screens.PlayScreen;

public class MainWindow extends Game {

	
    @Override
    public void create ()
    {
    	setScreen(new PlayScreen());
    }

    @Override
    public void render() 
    {
    	super.render();
    }
	
	@Override
	public void dispose ()
	{
		super.dispose();
	}
	
	@Override
	public void pause()
	{
		super.pause();
	}
	
	@Override
	public void resume()
	{
		super.resume();
	}
	
	
}


