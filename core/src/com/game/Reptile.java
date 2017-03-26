package com.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.game.levels.Level;

public class Reptile extends Enemy {
	
	private boolean isFacingRight;
	
	/* =========== ANIMATIONS =========== */
	private Texture walking;
	
	private Animation<TextureRegion> walkingAnim;
	/* ================================== */
	
	public Reptile(Vector2 spawnPoint, TiledMapTileLayer collisionLayer, float moveSpeed, Level level) {
		super(spawnPoint, collisionLayer, moveSpeed, level);
		
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

	public void drawEnemy(Batch batch, boolean debug) {
		Animation<TextureRegion> anim = null;
		boolean loop = true;
		
		update(Gdx.graphics.getDeltaTime());
		
		anim = this.walkingAnim;
		
		TextureRegion currentFrame = anim.getKeyFrame(stateTime, loop);
		this.width = currentFrame.getRegionWidth();
		this.height = currentFrame.getRegionHeight();
		
		this.hitXStart = x + 12;
		this.hitYStart = y + 1;
		
		// width + height of the hitbox
		this.hitWidth = 38;
		this.hitHeight = 22;
		
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
		
		batch.draw(currentFrame, this.x, this.y);
	}

	public void update(float delta) {
        this.stateTime += delta; 
		
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
       
        super.collision(delta);
        
	}

}
