package com.game.levels;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.game.Bubble;
import com.game.Enemy;
import com.game.FlameEye;
import com.game.Player;
import com.game.Reptile;
import com.game.Tentacles;

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
	
	protected Bubble[] bubbleArray;
	protected int initializedBubbles = 0;
	
	public Level(TiledMap tm, Vector2 spawn) {
		this.map = tm;
		this.spawnPoint = spawn;
		
		//Creates the player with a given spawn point, and places them on the given LAYER
		this.player = new Player(spawnPoint, (TiledMapTileLayer) map.getLayers().get("Background"), this);
		player.setPosition(spawnPoint.x, spawnPoint.y); //Set spawn point
		player.setCurrentLayer(1);
		Gdx.input.setInputProcessor(player); //Tells the game that all user input comes from the player object
		
		// Written like this so we can understand the hierarchy
		MapLayers mapLayers = map.getLayers();
		MapLayer mapLayer = mapLayers.get("Layer1");
		MapObjects mapObjects = mapLayer.getObjects();
		Iterator<MapObject> i = mapObjects.iterator();
		
		float objWidth, objHeight;
		
		int numEnemiesL1 = mapObjects.getCount();
		enemyArrayLayer1 = new Enemy[numEnemiesL1];
		
		// layer 1
		while (i.hasNext()) {
			MapObject obj = i.next();
			objWidth = (Float) obj.getProperties().get("x");
			objHeight = (Float) obj.getProperties().get("y");
			if (obj.getName().equals("Reptile")) {
				enemyArrayLayer1[initializedEnemiesLayer1++] = new Reptile(new Vector2(objWidth, objHeight), (TiledMapTileLayer) map.getLayers().get("Background"), (float)10, this);
			}
			else if (obj.getName().equals("Tentacle")) {
				enemyArrayLayer1[initializedEnemiesLayer1++] = new Tentacles(new Vector2(objWidth, objHeight), (TiledMapTileLayer) map.getLayers().get("Background"), (float)10, this);
			}
			else if (obj.getName().equals("FlameEye")) {
				enemyArrayLayer1[initializedEnemiesLayer1++] = new FlameEye(new Vector2(objWidth, objHeight), (TiledMapTileLayer) map.getLayers().get("Background"), (float)10, this);
			}
		}
		
		// layer 2
		mapLayers = map.getLayers();
		mapLayer = mapLayers.get("Layer2");
		mapObjects = mapLayer.getObjects();
		i = mapObjects.iterator();
		
		int numEnemiesL2 = mapObjects.getCount();
		enemyArrayLayer2 = new Enemy[numEnemiesL2];

		while (i.hasNext()) {
			MapObject obj = i.next();
			objWidth = (Float) obj.getProperties().get("x");
			objHeight = (Float) obj.getProperties().get("y");
			if (obj.getName().equals("Reptile")) {
				enemyArrayLayer2[initializedEnemiesLayer2++] = new Reptile(new Vector2(objWidth, objHeight), (TiledMapTileLayer) map.getLayers().get("Background"), (float)10, this);
			}
			else if (obj.getName().equals("Tentacle")) {
				enemyArrayLayer2[initializedEnemiesLayer2++] = new Tentacles(new Vector2(objWidth, objHeight), (TiledMapTileLayer) map.getLayers().get("Background"), (float)10, this);
			}
			else if (obj.getName().equals("FlameEye")) {
				enemyArrayLayer2[initializedEnemiesLayer2++] = new FlameEye(new Vector2(objWidth, objHeight), (TiledMapTileLayer) map.getLayers().get("Background"), (float)10, this);
			}
		}
		
		// layer 3
		mapLayers = map.getLayers();
		mapLayer = mapLayers.get("Layer3");
		mapObjects = mapLayer.getObjects();
		i = mapObjects.iterator();
		
		int numEnemiesL3 = mapObjects.getCount();
		enemyArrayLayer3 = new Enemy[numEnemiesL3];

		while (i.hasNext()) {
			MapObject obj = i.next();
			objWidth = (Float) obj.getProperties().get("x");
			objHeight = (Float) obj.getProperties().get("y");
			if (obj.getName().equals("Reptile")) {
				enemyArrayLayer3[initializedEnemiesLayer3++] = new Reptile(new Vector2(objWidth, objHeight), (TiledMapTileLayer) map.getLayers().get("Background"), (float)10, this);
			}
			else if (obj.getName().equals("Tentacle")) {
				enemyArrayLayer3[initializedEnemiesLayer3++] = new Tentacles(new Vector2(objWidth, objHeight), (TiledMapTileLayer) map.getLayers().get("Background"), (float)10, this);
			}
			else if (obj.getName().equals("FlameEye")) {
				enemyArrayLayer3[initializedEnemiesLayer3++] = new FlameEye(new Vector2(objWidth, objHeight), (TiledMapTileLayer) map.getLayers().get("Background"), (float)10, this);
			}
		}
		
		// layer 4
		/*
		mapLayers = map.getLayers();
		mapLayer = mapLayers.get("Layer4");
		mapObjects = mapLayer.getObjects(); // returns null pointer if there are none
		i = mapObjects.iterator();
		
		int numEnemiesL4 = mapObjects.getCount();
		enemyArrayLayer4 = new Enemy[numEnemiesL4];

		while (i.hasNext()) {
			MapObject obj = i.next();
			objWidth = (Float) obj.getProperties().get("x");
			objHeight = (Float) obj.getProperties().get("y");
			if (obj.getName().equals("Reptile")) {
				enemyArrayLayer4[initializedEnemiesLayer4++] = new Reptile(new Vector2(objWidth, objHeight), (TiledMapTileLayer) map.getLayers().get("Background"), (float)10, this);
			}
			else if (obj.getName().equals("Tentacle")) {
				enemyArrayLayer4[initializedEnemiesLayer4++] = new Tentacles(new Vector2(objWidth, objHeight), (TiledMapTileLayer) map.getLayers().get("Background"), (float)10, this);
			}
			else if (obj.getName().equals("FlameEye")) {
				enemyArrayLayer4[initializedEnemiesLayer4++] = new FlameEye(new Vector2(objWidth, objHeight), (TiledMapTileLayer) map.getLayers().get("Background"), (float)10, this);
			}
		}
		*/
		// layer 3
		mapLayers = map.getLayers();
		mapLayer = mapLayers.get("BubbleLayer");
		mapObjects = mapLayer.getObjects();
		i = mapObjects.iterator();
		
		int numBubbles = mapObjects.getCount();
		bubbleArray = new Bubble[numBubbles];

		while (i.hasNext()) {
			MapObject obj = i.next();
			objWidth = (Float) obj.getProperties().get("x");
			objHeight = (Float) obj.getProperties().get("y");
			if (obj.getName().equals("BubbleL2")) {
				bubbleArray[initializedBubbles++] = new Bubble(new Vector2(objWidth, objHeight), (TiledMapTileLayer) map.getLayers().get("Background"), this, 2);
			}
			else if (obj.getName().equals("BubbleL3")) {
				bubbleArray[initializedBubbles++] = new Bubble(new Vector2(objWidth, objHeight), (TiledMapTileLayer) map.getLayers().get("Background"), this, 3);
			}
			else if (obj.getName().equals("BubbleL4")) {
				bubbleArray[initializedBubbles++] = new Bubble(new Vector2(objWidth, objHeight), (TiledMapTileLayer) map.getLayers().get("Background"), this, 4);
			}
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
				enemyArrayLayer1[i].drawEnemy(batch, debug);
				enemyArrayLayer2[i].drawEnemy(batch, debug);
			}
		}
		else if (currentLayer == 3) {
			for(int i = 0; i < enemyArrayLayer3.length; i++) {
				enemyArrayLayer1[i].drawEnemy(batch, debug);
				enemyArrayLayer2[i].drawEnemy(batch, debug);
				enemyArrayLayer3[i].drawEnemy(batch, debug);
			}
		}
		else {
			for(int i = 0; i < enemyArrayLayer4.length; i++) {
				enemyArrayLayer1[i].drawEnemy(batch, debug);
				enemyArrayLayer2[i].drawEnemy(batch, debug);
				enemyArrayLayer3[i].drawEnemy(batch, debug);
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
