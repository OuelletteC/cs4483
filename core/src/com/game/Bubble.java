package com.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.game.levels.Level;

/*
 * The Bubble class is our "depth switching" mechanic -- it takes the player to and from
 * the next layer of sub-reality, bestowing Insights upon the player.
 */
public class Bubble {
	private boolean isIdle;
	private boolean hasDepthChanged; // boolean that indicates whether this bubble has been used as a warp point yet
	private int targetDepth;
	private Level level;
	
	private float x, y;
	private float width, height;
	
	private TiledMapTileLayer collisionLayer;
	
	private Texture bubble;
	private Animation<TextureRegion> bubbleAnim;
	private Animation<TextureRegion> poppingAnim;
	
	private float stateTime = 0;
	private float popStateTime = 0;
	private int popTimer = 50; // duration of how long the bubble should disappear for

	public Bubble(Vector2 spawnPoint, TiledMapTileLayer collisionLayer, Level level, int depth) {
		this.collisionLayer = collisionLayer;
		this.x = spawnPoint.x;
		this.y = spawnPoint.y;
		
		this.width = 48;
		this.height = 48;
		
		this.level = level;
		
		this.isIdle = true;
		this.hasDepthChanged = false;
		this.targetDepth = depth;
		
		// SOURCE: https://www.spriters-resource.com/ds_dsi/finalfantasyfablesct/sheet/62941/
		// ripped by redblueyellow
		// Final Fantasy Fables: Chocobo Tales (c) Square Enix
		bubble = new Texture("bubble-sheet.png");
		TextureRegion[][] bubbleFrames = TextureRegion.split(bubble, bubble.getWidth() / 6, bubble.getHeight());
		
		TextureRegion[] idleFrames = new TextureRegion[3];
		idleFrames[0] = bubbleFrames[0][0];
		idleFrames[1] = bubbleFrames[0][1];
		idleFrames[2] = bubbleFrames[0][2];
		
		TextureRegion[] poppingFrames = new TextureRegion[4];
		poppingFrames[0] = bubbleFrames[0][2];
		poppingFrames[1] = bubbleFrames[0][3];
		poppingFrames[2] = bubbleFrames[0][4];
		poppingFrames[3] = bubbleFrames[0][5];
		
		this.bubbleAnim = new Animation<TextureRegion>(0.1f, idleFrames);
		this.poppingAnim = new Animation<TextureRegion>(4f, poppingFrames);
	}

	public void drawBubble(Batch batch, boolean debug) {
		Animation<TextureRegion> anim = null;
		boolean loop = true;
		
		update(Gdx.graphics.getDeltaTime());
		
		TextureRegion currentFrame;
		
		if(isIdle == true) {
			anim = bubbleAnim;
			currentFrame = anim.getKeyFrame(stateTime, loop);
		}
		else {
			anim = poppingAnim;
			popStateTime++;
			currentFrame = anim.getKeyFrame(popStateTime, false);
		}
		
		floating();
		
		batch.draw(currentFrame, this.x, this.y);
	}

	public void update(float delta) {
		stateTime += delta;
		
		if(isIdle == false) {
			popTimer--;

			if(popTimer <= 0) {
				isIdle = true;
				popTimer = 50;
				popStateTime = 0;
			}
		}
	}
	
	private void floating() {
		float n = stateTime % 10;
		
		if(n < 5) {
			this.y = this.y + ((n - 2)/30);
		}
		else { // n >= 5
			this.y = this.y + ((n - ((2 * ((n % 5) + 1) ) + 1)) / 30); // just... don't ask.
		}
	}
	
	public boolean bubbleCollision(Player player) {
		boolean retVal = false;
		
		float x1 = player.getX();
		float y1 = player.getY();
		float x2 = player.getX() + player.getWidth();
		float y2 = player.getY() + player.getHeight();

		float x3 = this.getX();
		float y3 = this.getY();
		float x4 = this.getX() + this.getWidth();
		float y4 = this.getY() + this.getHeight();

		// player coords do not overlap with bubble
		if(x3 > x2 || y3 > y2 || x1 > x4 || y1 > y4) {
			retVal = false;
		}
		else { // player coords overlap with bubble
			retVal = true;
		}

		return retVal;
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
