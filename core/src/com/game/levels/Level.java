package com.game.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.game.*;

public class Level {
	
	protected TiledMap map;
	protected Vector2 spawnPoint;
	public static Player player; // static because the enemy class needs to have access to player coordinates
	
	protected int levelID; // probably useful for connecting levels
	protected int currentLayer = 1; // always starting out at the first layer
	protected int maxNumLayers; // integer that stores the highest number of layers for this map
	
	protected int maxEnemyCount; // integer with which to create the enemyArray, the maximum enemies in the level
	protected int initializedEnemiesLayer1 = 0; // number of initialized enemies, used for addEnemyToArray()
	protected int initializedEnemiesLayer2 = 0;
	protected int initializedEnemiesLayer3 = 0;
	protected int initializedEnemiesLayer4 = 0;
	protected Enemy[] enemyArrayLayer1;
	protected Enemy[] enemyArrayLayer2;
	protected Enemy[] enemyArrayLayer3;
	protected Enemy[] enemyArrayLayer4;
	
	protected Bubble[] bubbleArray = new Bubble[4]; // there will only be a max of four bubbles ever
	
	public Level(TiledMap tm, Vector2 spawn) {
		this.map = tm;
		this.spawnPoint = spawn;
		
		//Creates the player with a given spawn point, and places them on the given LAYER
		this.player = new Player(spawnPoint, (TiledMapTileLayer) map.getLayers().get("Background"), this);
		player.setPosition(spawnPoint.x, spawnPoint.y); //Set spawn point
		player.setCurrentLayer(1);
		Gdx.input.setInputProcessor(player); //Tells the game that all user input comes from the player object
		
		for(int i = 0; i < this.bubbleArray.length; i++) {
			bubbleArray[i] = new Bubble(new Vector2(0,0), (TiledMapTileLayer) map.getLayers().get("Background"), this, i + 2);
		}
	}
	
	public void renderEnemies(Batch batch, boolean debug) {
		
		if (currentLayer == 1) {
			for(int i = 0; i < enemyArrayLayer1.length; i++) {
				enemyArrayLayer1[i].drawEnemy(batch, debug);
			}
		}
		else if (currentLayer == 2) {
			for(int i = 0; i < enemyArrayLayer2.length; i++) {
				enemyArrayLayer2[i].drawEnemy(batch, debug);
			}
		}
		else if (currentLayer == 3) {
			for(int i = 0; i < enemyArrayLayer3.length; i++) {
				enemyArrayLayer3[i].drawEnemy(batch, debug);
			}
		}
		else {
			for(int i = 0; i < enemyArrayLayer4.length; i++) {
				enemyArrayLayer4[i].drawEnemy(batch, debug);
			}
		}
	}
	
	public void renderBubbles(Batch batch, boolean debug) {
		for(int i = 0; i < this.bubbleArray.length; i++) {
			bubbleArray[i].drawBubble(batch, debug);
		}
	}
	
	/*
	 * Set Bubble #i's location to the location of Vector2
	 */
	public void setBubbleLocation(int i, Vector2 point) {
		bubbleArray[i].setX(point.x);
		bubbleArray[i].setY(point.y);
	}	
	
	public void disposeMap() {
		map.dispose();
	}
	
	
	/*
	 * ===========================================
	 * GETTERS AND SETTERS BELOW THIS POINT
	 * ===========================================
	 */

	public TiledMap getMap() {
		return map;
	}

	public void setMap(TiledMap map) {
		this.map = map;
	}

	public Vector2 getSpawnPoint() {
		return spawnPoint;
	}

	public void setSpawnPoint(Vector2 spawnPoint) {
		this.spawnPoint = spawnPoint;
	}

	public Player getPlayer() {
		return player;
	}
	
	public int getLevelID() {
		return levelID;
	}

	public void setLevelID(int levelID) {
		this.levelID = levelID;
	}

	public int getCurrentLayer() {
		return currentLayer;
	}

	public void setCurrentLayer(int currentLayer) {
		this.currentLayer = currentLayer;
	}

	public int getMaxNumLayers() {
		return maxNumLayers;
	}

	public void setMaxNumLayers(int maxNumLayers) {
		this.maxNumLayers = maxNumLayers;
	}

	public int getMaxEnemyCount() {
		return maxEnemyCount;
	}

	public void setMaxEnemyCount(int maxEnemyCount) {
		this.maxEnemyCount = maxEnemyCount;
	}
	
	public Enemy[] getEnemyArray() {
		if (currentLayer == 1) {
			return enemyArrayLayer1; 
		}
		else if (currentLayer == 2) {
			return enemyArrayLayer2; 
		}
		else if (currentLayer == 3) {
			return enemyArrayLayer3; 
		}
		else {
			return enemyArrayLayer4; 
		}
	}
	
	public Bubble[] getBubbleArray() {
		return this.bubbleArray;
	}
	}
