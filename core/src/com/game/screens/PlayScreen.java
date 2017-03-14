package com.game.screens;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.game.Player;

public class PlayScreen implements Screen
{
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;
	
	private Player player;
	

	@Override
	public void render(float delta)
	{
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		renderer.setView(camera); //Tell the renderer to use the camera we made.
		renderer.render(); //render it all!
		
		renderer.getBatch().begin();
		player.draw(renderer.getBatch());
		renderer.getBatch().end();
	}
	
	@Override
	public void resize(int width, int height)
	{
		camera.viewportWidth = width;
		camera.viewportHeight = height;
		camera.update();
	}

	@Override
	public void show()
	{
		map = new TmxMapLoader().load("World1.tmx"); //Create the map
		
		renderer = new OrthogonalTiledMapRenderer(map); //Create the renderer
		
		camera = new OrthographicCamera(); //create a new camera focused on the map we are rendering
		
		Vector2 spawnPoint = new Vector2(128, 160);
		player = new Player(new Sprite(new Texture("playerTest.png")), spawnPoint ); //Creates player object with base sprite batch and spawn point
		player.setPosition(spawnPoint.x, spawnPoint.y); 
		
		Gdx.input.setInputProcessor(player); //Tells the game that all user input comes from the player object
		
		
	}
	
	@Override
	public void pause() 
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() 
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void hide()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() 
	{
		map.dispose();
		renderer.dispose();
		player.getTexture().dispose();
		
	}

}
