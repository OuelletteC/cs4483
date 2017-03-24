package com.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

public abstract class Enemy {
	
	protected float x, y;
	protected float width, height;
	
	protected float hitXStart, hitYStart;
	protected float hitWidth, hitHeight;
	
	protected Vector2 velocity = new Vector2(0, -1);
	protected Vector2 spawn;
	protected float movementSpeed, gravity = 20 * 9.8f; //Movement speed and gravity vector upon the player
	protected TiledMapTileLayer collisionLayer;
	
	protected boolean enemyFlag = true;
	protected float stateTime = 0;
	
	public Enemy(Vector2 spawnPoint, TiledMapTileLayer collisionLayer, float moveSpeed) {
		this.collisionLayer = collisionLayer;
		spawn = spawnPoint;
		movementSpeed = moveSpeed;
	}
	
	public abstract void drawEnemy(Batch batch, boolean debug);
	public abstract void update(float delta);
	
	public void collision(float delta) {
		
		velocity.y -= gravity * delta;
		
		//Objects have a terminal velocity when gravity is applied, so this handles that
        if(velocity.y > movementSpeed) {
        	velocity.y = movementSpeed;
        }
        else if (velocity.y < -movementSpeed) {	
           	velocity.y = -movementSpeed;
        }
		
		//We need to handle what happens when the player collides with a tile, so we save the old position in case we need to move the player BACK if they collide with something
        float oldX = getX(), oldY = getY(), tileWidth = collisionLayer.getTileWidth(), tileHeight = collisionLayer.getTileHeight();
    	boolean collidedX = false, collidedY = false;
        
        
        // move on x
        setX(getX() + velocity.x * delta);
        
        //moving left
        if(velocity.x < 0)
        {
        	/* 
        	 * @ So here, we need to make a check for each of the tiles to the sides of the player. Because movement is omnidirectional
        	 * @ we are going to check each of the three tiles to the right, left, top, and bottom of the players current position.
        	 * 
        	 * So the first line is a bit much...basically, we are going to get the cell which is at the position of the player, divided by the tile width.
        	 * This gives us values in relation to the actual tiled map. We then get the tile in that cell, and its properties, to check and see if it is a blocked tile
        	 * and thus must collide with the player.
        	 * */
        	
        	//top left tile, if the tile containsKey, collidedX will be true (containsKey returns a boolean)
        	collidedX = collisionLayer.getCell((int) (getX() / tileWidth), (int) ((getY() + getHeight()) / tileHeight)).getTile().getProperties().containsKey("blocked");
        	
        	//middle left tile
        	if(!collidedX)
        	collidedX = collisionLayer.getCell((int) (getX() / tileWidth), (int) ((getY() + getHeight() / 2 ) / tileHeight)).getTile().getProperties().containsKey("blocked");
        	
        	//bottom left tile
        	if(!collidedX)
        	collidedX = collisionLayer.getCell((int) (getX() / tileWidth), (int) (getY() / tileHeight)).getTile().getProperties().containsKey("blocked");
        	
        }
        else if(velocity.x > 0) //moving right
        {
        	
        	//top right
        	collidedX = collisionLayer.getCell((int) ((getX() + getWidth()) / tileWidth), (int) ((getY() + getHeight()) / tileHeight)).getTile().getProperties().containsKey("blocked");
        	
        	//middle right. If it still hasnt collided, check if it does for this next tile
        	if(!collidedX)
            	collidedX = collisionLayer.getCell((int) ((getX() + getWidth()) / tileWidth), (int) ((getY() + getHeight() / 2 ) / tileHeight)).getTile().getProperties().containsKey("blocked");
        	
        	//bottom right
        	if(!collidedX)
            	collidedX = collisionLayer.getCell((int) ((getX() + getWidth()) / tileWidth), (int) (getY() / tileHeight)).getTile().getProperties().containsKey("blocked");
        }
        	
        if(collidedX) //reaction to x collision
        {
        	setX(oldX); //We set it to the oldX because we technically dont move
        	velocity.x = 0;
        }
        	
        //move on y
        setY(getY() + velocity.y * delta);
        //falling
        if(velocity.y < 0)
        {
        	
        	// bottom left
            	collidedY = collisionLayer.getCell((int) (getX() / tileWidth), (int) (getY() / tileHeight)).getTile().getProperties().containsKey("blocked");
          	
        	//bottom middle
            	if(!collidedY)
            		collidedY = collisionLayer.getCell((int) ((getX() + getWidth() / 2 ) / tileWidth), (int) (getY() / tileHeight)).getTile().getProperties().containsKey("blocked");
        	
        	//bottom right
            	if(!collidedY)
            		collidedY = collisionLayer.getCell((int) ((getX() + getWidth()) / tileWidth), (int) (getY() / tileHeight)).getTile().getProperties().containsKey("blocked");
        }
        else if(velocity.y > 0) //jumping up into
        {
        	
        	//top left
        	collidedY = collisionLayer.getCell((int) (getX() / tileWidth), (int) ( (getY() + getHeight() )/ tileHeight)).getTile().getProperties().containsKey("blocked");
        	
        	//top middle
        	if(!collidedY)
        		collidedY = collisionLayer.getCell((int) (( getX() + getWidth() / 2 ) / tileWidth), (int) ( (getY() + getHeight())/ tileHeight)).getTile().getProperties().containsKey("blocked");
        	
        	//top right
        	if(!collidedY)
        		collidedY = collisionLayer.getCell((int) (getX() / tileWidth), (int) ( (getY() + getHeight()) / tileHeight)).getTile().getProperties().containsKey("blocked");
        }
        if(collidedY) //reaction to y collision
        {
        	setY(oldY); //We set it to the oldX because we technically dont move
        	velocity.y = 0;
        } 
        
	}
	
	public void setX(float newX) {
		this.x = newX;
	}
	
	public float getX() {
		return this.x;
	}
	
	public void setY(float newY) {
		this.y = newY;
	}
	
	public float getY() {
		return this.y;
	}
	
	public void setPosition(float theX, float theY) {
		this.x = theX;
		this.y = theY;
	}
	
	public void setWidth(float width) {
		this.width = width;
	}
	
	public float getWidth() {
		return this.width;
	}
	
	public void setHeight(float height) {
		this.height = height;
	}
	
	public float getHeight() {
		return this.height;
	}
	
	public float getHitXStart() {
		return hitXStart;
	}

	public void setHitXStart(float hitXStart) {
		this.hitXStart = hitXStart;
	}

	public float getHitYStart() {
		return hitYStart;
	}

	public void setHitYStart(float hitYStart) {
		this.hitYStart = hitYStart;
	}

	public float getHitWidth() {
		return hitWidth;
	}

	public void setHitWidth(float hitWidth) {
		this.hitWidth = hitWidth;
	}

	public float getHitHeight() {
		return hitHeight;
	}

	public void setHitHeight(float hitHeight) {
		this.hitHeight = hitHeight;
	}

	public boolean getEnemyFlag() {
		return this.enemyFlag;
	}
}



