package com.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

/*
 * The Bubble class is our "depth switching" mechanic -- it takes the player to and from
 * the next layer of sub-reality, bestowing Insights upon the player.
 */
public class Bubble {
	private boolean isIdle;
	private boolean hasDepthChanged; // boolean that indicates whether this bubble has been used as a warp point yet
	private int targetDepth;
	
	private float x, y;
	private float width, height;
	
	private TiledMapTileLayer collisionLayer;
	
	private Texture bubble;
	private Animation<TextureRegion> bubbleAnim;
	private Animation<TextureRegion> poppingAnim;
	
	private float stateTime = 0;
	

	public Bubble(Vector2 spawnPoint, TiledMapTileLayer collisionLayer, int depth) {
		this.collisionLayer = collisionLayer;
		this.x = spawnPoint.x;
		this.y = spawnPoint.y;
		
		this.isIdle = true;
		this.hasDepthChanged = false;
		this.targetDepth = depth;
		
		// SOURCE: https://www.spriters-resource.com/ds_dsi/finalfantasyfablesct/sheet/62941/
		// ripped by redblueyellow
		// Final Fantasy Fables: Chocobo Tales (c) Square Enix
		bubble = new Texture("bubble-sheet.png");
		TextureRegion[][] bubbleFrames = TextureRegion.split(bubble, bubble.getWidth() / 5, bubble.getHeight());
		
		TextureRegion[] idleFrames = new TextureRegion[3];
		idleFrames[0] = bubbleFrames[0][0];
		idleFrames[1] = bubbleFrames[0][1];
		idleFrames[2] = bubbleFrames[0][2];
		
		TextureRegion[] poppingFrames = new TextureRegion[3];
		poppingFrames[0] = bubbleFrames[0][2];
		poppingFrames[1] = bubbleFrames[0][3];
		poppingFrames[2] = bubbleFrames[0][4];
		
		this.bubbleAnim = new Animation<TextureRegion>(0.1f, idleFrames);
		this.poppingAnim = new Animation<TextureRegion>(0.1f, poppingFrames);
	}

	public void drawBubble(Batch batch, boolean debug) {
		Animation<TextureRegion> anim = null;
		boolean loop = true;
		
		update(Gdx.graphics.getDeltaTime());
		
		if(isIdle == true) {
			anim = bubbleAnim;
		}
		else {
			anim = poppingAnim;
			loop = false;
		}
		
		floating();
		TextureRegion currentFrame = anim.getKeyFrame(stateTime, loop);
		
		batch.draw(currentFrame, this.x, this.y);
	}

	public void update(float delta) {
		stateTime += delta;
	}
	
	private void floating() {
		float n = stateTime % 10;
		
		if(n < 5) {
			this.y = this.y + (n - 2);
		}
		else { // n >= 5
			this.y = this.y + (n - ((2 * ((n % 5) + 1) ) + 1)); // just... don't ask.
		}
	}

	public boolean isIdle() {
		return isIdle;
	}


	public void setIdle(boolean isIdle) {
		this.isIdle = isIdle;
	}


	public boolean hasDepthChanged() {
		return hasDepthChanged;
	}


	public void setHasDepthChanged(boolean hasDepthChanged) {
		this.hasDepthChanged = hasDepthChanged;
	}


	public float getX() {
		return x;
	}


	public void setX(float x) {
		this.x = x;
	}


	public float getY() {
		return y;
	}


	public void setY(float y) {
		this.y = y;
	}


	public float getWidth() {
		return width;
	}


	public void setWidth(float width) {
		this.width = width;
	}


	public float getHeight() {
		return height;
	}


	public void setHeight(float height) {
		this.height = height;
	}


	public TiledMapTileLayer getCollisionLayer() {
		return collisionLayer;
	}


	public void setCollisionLayer(TiledMapTileLayer collisionLayer) {
		this.collisionLayer = collisionLayer;
	}


	public float getStateTime() {
		return stateTime;
	}


	public void setStateTime(float stateTime) {
		this.stateTime = stateTime;
	}

	public int getTargetDepth() {
		return this.targetDepth;
	}
	
	
}
