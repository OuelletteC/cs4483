package com.game.levels;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.game.Enemy;
import com.game.FlameEye;
import com.game.Reptile;
import com.game.Tentacles;

public class Stage2 extends Level {
	
	public Stage2(TiledMap tm, Vector2 spawn) {
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
		enemyArrayLayer1[initializedEnemiesLayer1++] = new Tentacles(new Vector2(1190, 0), (TiledMapTileLayer) map.getLayers().get("Background"), (float)10, this);
		enemyArrayLayer1[initializedEnemiesLayer1++] = new Tentacles(new Vector2(1345, 0), (TiledMapTileLayer) map.getLayers().get("Background"), (float)10, this);
		enemyArrayLayer1[initializedEnemiesLayer1++] = new Tentacles(new Vector2(1450, 0), (TiledMapTileLayer) map.getLayers().get("Background"), (float)10, this);
		//enemyArrayLayer1[initializedEnemiesLayer1++] = new Tentacles(new Vector2(1472, 896), (TiledMapTileLayer) map.getLayers().get("Background"), (float)10, this);
		
	}
	
	public void layer2() {
		
		int numEnemies = 6;
		enemyArrayLayer2 = new Enemy[numEnemies];
		
		// enemies for layer 2
		enemyArrayLayer2[initializedEnemiesLayer2++] = new Tentacles(new Vector2(1190, 0), (TiledMapTileLayer) map.getLayers().get("Background"), (float)10, this);
		enemyArrayLayer2[initializedEnemiesLayer2++] = new Tentacles(new Vector2(1345, 0), (TiledMapTileLayer) map.getLayers().get("Background"), (float)10, this);
		enemyArrayLayer2[initializedEnemiesLayer2++] = new Tentacles(new Vector2(1450, 0), (TiledMapTileLayer) map.getLayers().get("Background"), (float)10, this);
		enemyArrayLayer2[initializedEnemiesLayer2++] = new Reptile(new Vector2(500, 224), (TiledMapTileLayer) map.getLayers().get("Background"), (float)10, this);
		enemyArrayLayer2[initializedEnemiesLayer2++] = new Reptile(new Vector2(150, 224), (TiledMapTileLayer) map.getLayers().get("Background"), (float)10, this);
		enemyArrayLayer2[initializedEnemiesLayer2++] = new Reptile(new Vector2(200, 384), (TiledMapTileLayer) map.getLayers().get("Background"), (float)10, this);
		
	}

	public void layer3() {
		
		int numEnemies = 13;
		enemyArrayLayer3 = new Enemy[numEnemies];

		// from layer 2
		enemyArrayLayer3[initializedEnemiesLayer3++] = new Tentacles(new Vector2(1190, 0), (TiledMapTileLayer) map.getLayers().get("Background"), (float)10, this);
		enemyArrayLayer3[initializedEnemiesLayer3++] = new Tentacles(new Vector2(1345, 0), (TiledMapTileLayer) map.getLayers().get("Background"), (float)10, this);
		enemyArrayLayer3[initializedEnemiesLayer3++] = new Tentacles(new Vector2(1450, 0), (TiledMapTileLayer) map.getLayers().get("Background"), (float)10, this);
		enemyArrayLayer3[initializedEnemiesLayer3++] = new Reptile(new Vector2(500, 224), (TiledMapTileLayer) map.getLayers().get("Background"), (float)10, this);
		enemyArrayLayer3[initializedEnemiesLayer3++] = new Reptile(new Vector2(150, 224), (TiledMapTileLayer) map.getLayers().get("Background"), (float)10, this);
		enemyArrayLayer3[initializedEnemiesLayer3++] = new Reptile(new Vector2(200, 384), (TiledMapTileLayer) map.getLayers().get("Background"), (float)10, this);
		// new in layer 3
		enemyArrayLayer3[initializedEnemiesLayer3++] = new Reptile(new Vector2(400, 384), (TiledMapTileLayer) map.getLayers().get("Background"), (float)10, this);
		enemyArrayLayer3[initializedEnemiesLayer3++] = new FlameEye(new Vector2(1000, 128), (TiledMapTileLayer) map.getLayers().get("Background"), (float)10, this);
		enemyArrayLayer3[initializedEnemiesLayer3++] = new FlameEye(new Vector2(615, 425), (TiledMapTileLayer) map.getLayers().get("Background"), (float)10, this);
		enemyArrayLayer3[initializedEnemiesLayer3++] = new FlameEye(new Vector2(670, 380), (TiledMapTileLayer) map.getLayers().get("Background"), (float)10, this);
		enemyArrayLayer3[initializedEnemiesLayer3++] = new FlameEye(new Vector2(890, 425), (TiledMapTileLayer) map.getLayers().get("Background"), (float)10, this);
		enemyArrayLayer3[initializedEnemiesLayer3++] = new FlameEye(new Vector2(890, 375), (TiledMapTileLayer) map.getLayers().get("Background"), (float)10, this);
		enemyArrayLayer3[initializedEnemiesLayer3++] = new Tentacles(new Vector2(1010, 32), (TiledMapTileLayer) map.getLayers().get("Background"), (float)10, this);
	
	}

	public void layer4() {
		
		int numEnemies = 12;
		enemyArrayLayer4 = new Enemy[numEnemies];
	
		// enemies for layer 4
		enemyArrayLayer4[initializedEnemiesLayer4++] = new Tentacles(new Vector2(1190, 0), (TiledMapTileLayer) map.getLayers().get("Background"), (float)10, this);
		enemyArrayLayer4[initializedEnemiesLayer4++] = new Tentacles(new Vector2(1345, 0), (TiledMapTileLayer) map.getLayers().get("Background"), (float)10, this);
		enemyArrayLayer4[initializedEnemiesLayer4++] = new Tentacles(new Vector2(1450, 0), (TiledMapTileLayer) map.getLayers().get("Background"), (float)10, this);
		enemyArrayLayer4[initializedEnemiesLayer4++] = new Reptile(new Vector2(500, 224), (TiledMapTileLayer) map.getLayers().get("Background"), (float)10, this);
		enemyArrayLayer4[initializedEnemiesLayer4++] = new Reptile(new Vector2(150, 224), (TiledMapTileLayer) map.getLayers().get("Background"), (float)10, this);
		enemyArrayLayer4[initializedEnemiesLayer4++] = new Reptile(new Vector2(200, 384), (TiledMapTileLayer) map.getLayers().get("Background"), (float)10, this);
		enemyArrayLayer4[initializedEnemiesLayer4++] = new Reptile(new Vector2(400, 384), (TiledMapTileLayer) map.getLayers().get("Background"), (float)10, this);
		enemyArrayLayer4[initializedEnemiesLayer4++] = new FlameEye(new Vector2(1000, 128), (TiledMapTileLayer) map.getLayers().get("Background"), (float)10, this);
		enemyArrayLayer4[initializedEnemiesLayer4++] = new FlameEye(new Vector2(615, 425), (TiledMapTileLayer) map.getLayers().get("Background"), (float)10, this);
		enemyArrayLayer4[initializedEnemiesLayer4++] = new FlameEye(new Vector2(670, 380), (TiledMapTileLayer) map.getLayers().get("Background"), (float)10, this);
		enemyArrayLayer4[initializedEnemiesLayer4++] = new FlameEye(new Vector2(890, 425), (TiledMapTileLayer) map.getLayers().get("Background"), (float)10, this);
		enemyArrayLayer4[initializedEnemiesLayer4++] = new FlameEye(new Vector2(890, 375), (TiledMapTileLayer) map.getLayers().get("Background"), (float)10, this);
	
	}

}
