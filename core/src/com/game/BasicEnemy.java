package com.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

public class BasicEnemy extends Enemy {
	
	private boolean direction = true;
	
	public BasicEnemy(Sprite sprite, Vector2 spawnPoint, TiledMapTileLayer collisionLayer, float moveSpeed) {
		super(sprite, spawnPoint, collisionLayer, moveSpeed);
	}

	public void draw(Batch spriteBatch) {
		update(Gdx.graphics.getDeltaTime());
		super.draw(spriteBatch);
	}	

	public void update(float delta) {
             
        // Movement for basic AI (move x steps right, then x steps left)
        // Direction set to true for moving right and false for moving left
        if (direction) {
        	velocity.x = movementSpeed;
        	if (getX() > spawn.x + 160) {
        		direction = false;
        	}
        }
        else {
        	velocity.x = -movementSpeed;
        	if (getX() < spawn.x) {
        		direction = true;
        	}
        }
       
        super.collision(delta);
        
	}
	
}
