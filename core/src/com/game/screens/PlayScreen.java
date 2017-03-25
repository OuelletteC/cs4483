package com.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.game.*;
import com.game.levels.Level;
import com.game.Application;

public class PlayScreen implements Screen
{
	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;
	Application game;
	
	private int numberOfEnemies = 1;
	private int stageCount = 0;
	
	public Level currLevel;
	public String currStage[] = {"Stage1_a.tmx", "Stage2_a.tmx", "Stage3_a.tmx"};
	
	private boolean debug;
	
	public PlayScreen(boolean debug) {
		this.debug = debug;
	}

	@Override
	public void render(float delta)
	{		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.position.set(currLevel.getPlayer().getX() + currLevel.getPlayer().getWidth() / 2, currLevel.getPlayer().getY() + currLevel.getPlayer().getHeight() / 2, 0);
		camera.update();
		
		renderer.setView(camera); //Tell the renderer to use the camera we made.
		
		/**THIS IS IMPORTANT!! We need to pass an array of layers to the renderer, so it knows what order to render the layers in. So its very important, in Tiled
		/to keep layers organized and neat.*/
		renderer.getBatch().begin();
		renderer.renderTileLayer((TiledMapTileLayer) currLevel.getMap().getLayers().get("Background")); //Renders the background of our Tiled maps
		
		
		// Space to render enemies
		currLevel.renderEnemies(renderer.getBatch(), this.debug);
		
	    //Renders the player
		currLevel.getPlayer().update(Gdx.graphics.getDeltaTime(), currLevel.getEnemyArray());
		currLevel.getPlayer().drawPlayer(renderer.getBatch(), this.debug);
		
		renderer.renderTileLayer((TiledMapTileLayer) currLevel.getMap().getLayers().get("Foreground")); //Renders the foreground platforms
		
		renderer.getBatch().end();
		
		if(this.debug == true) {
			BitmapFont font = new BitmapFont();
			SpriteBatch spriteBatch = new SpriteBatch();
			
			spriteBatch.begin();
			font.draw(spriteBatch, "x = " + currLevel.getPlayer().getX(), 10, 50);
			font.draw(spriteBatch, "y = " + currLevel.getPlayer().getY(), 150, 50);
			font.draw(spriteBatch, "currentLayer = " + currLevel.getPlayer().getCurrentLayer(), 300, 50);
			
			font.draw(spriteBatch, "isFacingRight = " + currLevel.getPlayer().getFacingRight(), 10, 30);
			font.draw(spriteBatch, "State = " + currLevel.getPlayer().getState(), 150, 30);
			font.draw(spriteBatch, "HP = " + currLevel.getPlayer().getHealthPoints(), 300, 30);
			font.draw(spriteBatch, "isInvincible = " + currLevel.getPlayer().isInvincible(), 450, 30);
			font.draw(spriteBatch, "onWinTile = " + currLevel.getPlayer().getVictory(), 750, 30);
			font.draw(spriteBatch, "isCollided = " + currLevel.getPlayer().getCollidedX(), 875, 30);
			if(currLevel.getPlayer().isInvincible()) {
				font.draw(spriteBatch, "IFRAMES = " + currLevel.getPlayer().getInvincibleTimer(), 600, 30);
			}
			spriteBatch.end();
		}
		
		if(currLevel.getPlayer().isDead())
		{
			currLevel.getPlayer().setPosition(currLevel.getSpawnPoint().x, currLevel.getSpawnPoint().y);
		}
		
		if(currLevel.getPlayer().getVictory()) //If the player encounters a 'victory tile,' change the current level the player is on
		{
			stageCount++;
			dispose();
			show();
			currLevel.getPlayer().setVelocity(new Vector2(0, 0));
			
			try 
			{
				Thread.sleep(600);
			} catch (InterruptedException e) 
			{
				e.printStackTrace();
			}

			resize(1280, 730); //Honestly our code is pretty messy so a super cheesy way to 'update' the screen is to just resize it to the same thing it already is
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
	}

	@Override
	public void show()
	{
		// create new Level object
		currLevel = new Level(new TmxMapLoader().load(currStage[stageCount]), new  //currStage holds the string value of the current stage the player is on
				Vector2(48, 100), numberOfEnemies); // create the level
	
		renderer = new OrthogonalTiledMapRenderer(currLevel.getMap()); //Create the renderer
		
		camera = new OrthographicCamera(); //create a new camera focused on the map we are rendering
		
		// Creating enemies here
		//currLevel.addEnemyToArray(new BasicEnemy(new Vector2(128,160), (TiledMapTileLayer) currLevel.getMap().getLayers().get("Background"), 50, currLevel));
		//currLevel.addEnemyToArray(new IntermediateEnemy(new Vector2(256,160), (TiledMapTileLayer) currLevel.getMap().getLayers().get("Background"), 50, currLevel));
		//currLevel.addEnemyToArray(new FlameEye(new Vector2(160, 160), (TiledMapTileLayer) currLevel.getMap().getLayers().get("Background"), 50, currLevel));
		
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
		currLevel.disposeMap();
		renderer.dispose();
	}

}
