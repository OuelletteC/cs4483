package com.game.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.game.Enemy;
import com.game.Player;

public class Level {
	
	protected TiledMap map;
	protected Vector2 spawnPoint;
	public static Player player; // static because the enemy class needs to have access to player coordinates
	
	protected int levelID; // probably useful for connecting levels
	protected int currentLayer;
	protected int maxNumLayers; // integer that stores the highest number of layers for this map
	
	protected int maxEnemyCount; // integer with which to create the enemyArray, the maximum enemies in the level
	protected int initializedEnemies = 0; // number of initialized enemies, used for addEnemyToArray()
	protected Enemy[] enemyArray;
	
	public Level(TiledMap tm, Vector2 spawn, int enemyCount) {
		this.map = tm;
		this.spawnPoint = spawn;
		
		//Creates the player with a given spawn point, and places them on the given LAYER
		this.player = new Player(spawnPoint, (TiledMapTileLayer) map.getLayers().get("Background"));
		player.setPosition(spawnPoint.x, spawnPoint.y); //Set spawn point
		player.setCurrentLayer(1);
		Gdx.input.setInputProcessor(player); //Tells the game that all user input comes from the player object
		
		this.maxEnemyCount = enemyCount;
		this.enemyArray = new Enemy[this.maxEnemyCount];
	}
	
	public void addEnemyToArray(Enemy enemy) {
		try {
			this.enemyArray[initializedEnemies] = enemy;
		}
		catch(Exception e) {
			System.out.println("Enemy " + enemy + " could not be added to array");
			initializedEnemies--; // enemy was not initialized
		}
		initializedEnemies++;
	}
	
	public void renderEnemies(Batch batch, boolean debug) {
		for(int i = 0; i < this.enemyArray.length; i++) {
			enemyArray[i].drawEnemy(batch, debug);
		}
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

	public void setPlayer(Player player) {
		this.player = player;
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

	public int getInitializedEnemies() {
		return initializedEnemies;
	}

	public void setInitializedEnemies(int initializedEnemies) {
		this.initializedEnemies = initializedEnemies;
	}

	public Enemy[] getEnemyArray() {
		return enemyArray;
	}

	public void setEnemyArray(Enemy[] enemyArray) {
		this.enemyArray = enemyArray;
	}
	
	
}
