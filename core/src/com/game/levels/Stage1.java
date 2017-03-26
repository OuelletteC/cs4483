package com.game.levels;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.game.Enemy;
import com.game.FlameEye;
import com.game.Reptile;
import com.game.Tentacles;

public class Stage1 extends Level {

	public Stage1(TiledMap tm, Vector2 spawn) {
		super(tm, spawn);
		layer1();
		layer2();
		layer3();
		layer4();
	}
	
	public void layer1() {
		
		int numEnemies = 3;
		enemyArrayLayer1 = new Enemy[numEnemies];
		
		// enemies for layer 1
		enemyArrayLayer1[initializedEnemiesLayer1++] = new Tentacles(new Vector2(150, 96), (TiledMapTileLayer) map.getLayers().get("Background"), (float)10, this);
		enemyArrayLayer1[initializedEnemiesLayer1++] = new Reptile(new Vector2(100, 96), (TiledMapTileLayer) map.getLayers().get("Background"), (float)10, this);
		enemyArrayLayer1[initializedEnemiesLayer1++] = new FlameEye(new Vector2(400, 96), (TiledMapTileLayer) map.getLayers().get("Background"), (float)10, this);
		
	}
	
	public void layer2() {
		
		int numEnemies = 3;
		enemyArrayLayer2 = new Enemy[numEnemies];
		
		// enemies for layer 2
		enemyArrayLayer2[initializedEnemiesLayer2++] = new Tentacles(new Vector2(150, 96), (TiledMapTileLayer) map.getLayers().get("Background"), (float)10, this);
		enemyArrayLayer2[initializedEnemiesLayer2++] = new Reptile(new Vector2(100, 96), (TiledMapTileLayer) map.getLayers().get("Background"), (float)10, this);
		enemyArrayLayer2[initializedEnemiesLayer2++] = new FlameEye(new Vector2(400, 96), (TiledMapTileLayer) map.getLayers().get("Background"), (float)10, this);
		
	}

	public void layer3() {
		
		int numEnemies = 3;
		enemyArrayLayer3 = new Enemy[numEnemies];

		// enemies for layer 3
		enemyArrayLayer3[initializedEnemiesLayer3++] = new Tentacles(new Vector2(150, 96), (TiledMapTileLayer) map.getLayers().get("Background"), (float)10, this);
		enemyArrayLayer3[initializedEnemiesLayer3++] = new Reptile(new Vector2(100, 96), (TiledMapTileLayer) map.getLayers().get("Background"), (float)10, this);
		enemyArrayLayer3[initializedEnemiesLayer3++] = new FlameEye(new Vector2(400, 96), (TiledMapTileLayer) map.getLayers().get("Background"), (float)10, this);
	
	}

	public void layer4() {
		
		int numEnemies = 3;
		enemyArrayLayer4 = new Enemy[numEnemies];
	
		// enemies for layer 4
		enemyArrayLayer4[initializedEnemiesLayer4++] = new Tentacles(new Vector2(150, 96), (TiledMapTileLayer) map.getLayers().get("Background"), (float)10, this);
		enemyArrayLayer4[initializedEnemiesLayer4++] = new Reptile(new Vector2(100, 96), (TiledMapTileLayer) map.getLayers().get("Background"), (float)10, this);
		enemyArrayLayer4[initializedEnemiesLayer4++] = new FlameEye(new Vector2(400, 96), (TiledMapTileLayer) map.getLayers().get("Background"), (float)10, this);
	
	}

}
