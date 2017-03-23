package com.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.game.screens.PlayScreen;

public class IntermediateEnemy extends Enemy {
	
private boolean isFacingRight;

/* =========== ANIMATIONS =========== */
private Texture walking;

private Animation<TextureRegion> walkingAnim;
/* ================================== */
	
	public IntermediateEnemy(Vector2 spawnPoint, TiledMapTileLayer collisionLayer, float moveSpeed) {
		super(spawnPoint, collisionLayer, moveSpeed);
		
		this.isFacingRight = true;
		
		loadTextures();
	}
	
	private void loadTextures() {
		this.walking = new Texture("enemy1.png");
		
		TextureRegion[][] walkingFrames = TextureRegion.split(walking, (walking.getWidth() / 6), walking.getHeight());
		
		TextureRegion[] walkingFrames2 = new TextureRegion[6 * 1];
		int index = 0;
		for (int i = 0; i < 1; i++) {
			for (int j = 0; j < 6; j++) {
				walkingFrames2[index++] = walkingFrames[i][j];
			}
		}
		
		this.walkingAnim = new Animation<TextureRegion>(0.1f, walkingFrames2);
	}

	public TextureRegion drawEnemy() {
		Animation<TextureRegion> anim = null;
		boolean loop = true;
		
		update(Gdx.graphics.getDeltaTime());
		
		anim = this.walkingAnim;
		
		TextureRegion currentFrame = anim.getKeyFrame(stateTime, loop);
		this.width = currentFrame.getRegionWidth();
		this.height = currentFrame.getRegionHeight();
		
		if(this.isFacingRight == true) {
			if(!currentFrame.isFlipX()) {
				currentFrame.flip(true, false);
			}
		}
		else {
			if(currentFrame.isFlipX()) {
				currentFrame.flip(true, false);
			}
		}
		
		return currentFrame;
	}

	public void update(float delta) {
		this.stateTime += delta;
		
		// Checking if the player is near
		float playerX = PlayScreen.player.getX();
		float playerY = PlayScreen.player.getY();
		float enemyX = getX();
		float enemyY = getY();
		int visionRange = 80;
		float distanceBetween = playerX - enemyX;
		
		if (distanceBetween < visionRange && distanceBetween > -visionRange) {
			if (distanceBetween > 0) {
				velocity.x = movementSpeed;
			}
			else {
				velocity.x = -movementSpeed;
			}
		}
		else {
        
	        // Movement for basic AI (move x steps right, then x steps left)
	        // Direction set to true for moving right and false for moving left
	        if (isFacingRight) {
	        	velocity.x = movementSpeed;
	        	if (getX() > spawn.x + 160) {
	        		isFacingRight = false;
	        	}
	        }
	        else {
	        	velocity.x = -movementSpeed;
	        	if (getX() < spawn.x) {
	        		isFacingRight = true;
	        	}
	        }
       
		}
		
		super.collision(delta);
        
	}	
}
