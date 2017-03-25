package com.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;

import com.game.*;

public class PlayScreen implements Screen
{   
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;
	Vector2 spawnPoint = new Vector2(24, 160);
	
	private boolean debug;
	public static Player player; // static because the enemy class needs to have access to player coordinates
	
	private Enemy[] eArray = new Enemy[3]; // TODO: untangle the eArray further
	Application game;
	
	/*
	private BasicEnemy basicEnemy1;
	private IntermediateEnemy intEnemy1;
	private FlameEye fE1;
	*/
	
	public PlayScreen(boolean debug) {
		this.debug = debug;
	}

	@Override
	public void render(float delta)
	{		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.position.set(player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() / 2, 0);
		camera.update();
		
		renderer.setView(camera); //Tell the renderer to use the camera we made.
		
		/**THIS IS IMPORTANT!! We need to pass an array of layers to the renderer, so it knows what order to render the layers in. So its very important, in Tiled
		/to keep layers organized and neat.*/
		renderer.getBatch().begin();
		renderer.renderTileLayer((TiledMapTileLayer) map.getLayers().get("Background")); //Renders the background of our Tiled maps
		
		
		// Space to render enemies
		for(int i = 0; i < eArray.length; i++) {
			eArray[i].drawEnemy(renderer.getBatch(), this.debug);
		}
		
	    //Renders the player
		player.update(Gdx.graphics.getDeltaTime(), eArray);
		player.drawPlayer(renderer.getBatch(), this.debug);
		
		renderer.renderTileLayer((TiledMapTileLayer) map.getLayers().get("Foreground")); //Renders the foreground platforms
		
		renderer.getBatch().end();
		
		if(this.debug == true) {
			BitmapFont font = new BitmapFont();
			SpriteBatch spriteBatch = new SpriteBatch();
			
			spriteBatch.begin();
			font.draw(spriteBatch, "x = " + player.getX(), 10, 50);
			font.draw(spriteBatch, "y = " + player.getY(), 150, 50);
			font.draw(spriteBatch, "currentLayer = " + player.getCurrentLayer(), 300, 50);
			
			font.draw(spriteBatch, "isFacingRight = " + player.getFacingRight(), 10, 30);
			font.draw(spriteBatch, "State = " + player.getState(), 150, 30);
			font.draw(spriteBatch, "HP = " + player.getHealthPoints(), 300, 30);
			font.draw(spriteBatch, "isInvincible = " + player.isInvincible(), 450, 30);
			if(player.isInvincible()) {
				font.draw(spriteBatch, "IFRAMES = " + player.getInvincibleTimer(), 600, 30);
			}
			spriteBatch.end();
		}
		
		if(player.isDead())
		{
			player.setPosition(spawnPoint.x, spawnPoint.y);
//			game.setScreen(new MainMenuScreen(game));
		}
		
	}
	
	@Override
	public void resize(int width, int height)
	{
		/*
		 * The viewport is gonna help us focus in closer to the game, so we dont have a zoomed out game. 
		 * */
		camera.viewportWidth = width / 2;
		camera.viewportHeight = height / 2;
		
		//camera.update(); Dont need to update the camera since we do so in the render function, as the player is not being followe by the camera.
	}

	@Override
	public void show()
	{
		map = new TmxMapLoader().load("Stage1_a.tmx"); //Create the map
		
		renderer = new OrthogonalTiledMapRenderer(map); //Create the renderer
		
		camera = new OrthographicCamera(); //create a new camera focused on the map we are rendering
		
		//Vector2 spawnPoint = new Vector2(24, 160);
		
		//Creates the player with a given sprite batch, spawn point, and places them on the given LAYER
		player = new Player(spawnPoint, (TiledMapTileLayer) map.getLayers().get("Background")); 
		player.setPosition(spawnPoint.x, spawnPoint.y); //Set spawn point
		player.setCurrentLayer(1);
		
		Gdx.input.setInputProcessor(player); //Tells the game that all user input comes from the player object
		
		// Creating enemies here
		eArray[0] = new BasicEnemy(new Vector2(128,160), (TiledMapTileLayer) map.getLayers().get("Background"), 50);
		eArray[0].setPosition(128, 160);
		
		eArray[1] = new IntermediateEnemy(new Vector2(256,160), (TiledMapTileLayer) map.getLayers().get("Background"), 50);
		eArray[1].setPosition(256, 160);
		
		eArray[2] = new FlameEye(new Vector2(160, 160), (TiledMapTileLayer) map.getLayers().get("Background"), 50);
		eArray[2].setPosition(160,160);
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
		//player.getTexture().dispose();
		
	}

}
