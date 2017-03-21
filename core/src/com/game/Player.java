package com.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

public class Player extends Sprite implements InputProcessor
{
	public enum PlayerState {
		IDLE, WALKING, JUMPING, FALLING, CLING, HOVER
	}
	
	/** Movement vector storing x/y velocity */
	private Vector2 velocity = new Vector2(0, -1);
	private Vector2 spawn;// = new Vector2();
	private float movementSpeed = 125, gravity = 20 * 9.8f; //Movement speed and gravity vector upon the player
	private TiledMapTileLayer collisionLayer;
	
	private boolean canJump;
	private boolean isFacingRight;
	
	private PlayerState state;
	
	private Sprite sb = new Sprite(new Texture("polarity_mc_turnarnound-sheet.png"));
	
	public Player(Sprite sprite, Vector2 spawnPoint, TiledMapTileLayer collisionLayer)
	{
		super(sprite); //how the player looks
		this.collisionLayer = collisionLayer;
		this.state = PlayerState.IDLE; // initialize the state to idle, though this will likely update
		this.isFacingRight = true; // initialize the facing to be to the right
	}
	
	public void draw(Batch batch)
	{
		update(Gdx.graphics.getDeltaTime());
		super.draw(batch);
	}
	
    public void update(float delta) 
    {
        velocity.y -= gravity * delta; //applies gravity to the y velocity for each unit of time (delta) that passes
        
    	//Objects have a terminal velocity when gravity is applied, so this handles that
        if(velocity.y > movementSpeed)
        	velocity.y = movementSpeed;
        else if(velocity.y < -movementSpeed)
        	velocity.y = -movementSpeed;
        
        //We need to handle what happens when the player collides with a tile, so we save the old position in case we need to move the player BACK if they collide with something
        float oldX = getX(), oldY = getY(), tileWidth = collisionLayer.getTileWidth(), tileHeight = collisionLayer.getTileHeight();
    	boolean collidedX = false, collidedY = false;
        
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
            	// re-enable the jump once the player has touched down
            	canJump = collidedY;
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
        	setY(oldY); //We set it to the oldY because we technically dont move
        	velocity.y = 0;
        }
        
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
        
        if(velocity.x == 0 && velocity.y == 0) {
        	this.state = PlayerState.IDLE;
        }
        else { // if velocity.x != 0 || velocity.y != 0 
        	if (velocity.x < 0 || velocity.x > 0) {
        		this.state = PlayerState.WALKING;
        	}
        	if (velocity.y < 0) {
        		this.state = PlayerState.FALLING;
        	}
        	if (velocity.y > 0) {
        		this.state = PlayerState.JUMPING;
        	}
        }
    }

	@Override
	//This method handles what occurs when the key is pressed DOWN
	public boolean keyDown(int keycode) {
		
		switch(keycode)
		{
		case Keys.LEFT:
			velocity.x = -movementSpeed;
			this.state = PlayerState.WALKING;
			this.isFacingRight = false;
			
			// System.out.println("left");
			break;
		case Keys.RIGHT:
			velocity.x = movementSpeed;
			this.state = PlayerState.WALKING;
			this.isFacingRight = true;
			
			// System.out.println("right");
			break;
		case Keys.UP:
			if(canJump) {
				velocity.y = movementSpeed;
				this.state = PlayerState.JUMPING;
				
				canJump = false; //We need to set a flag so that the player cant jump over and over again midair.
				// System.out.println("up");
			}
			break;

		}
		return true;
	}

	@Override
	//So when the player releases the key, the player will stop moving
	public boolean keyUp(int keycode) {
		
		switch(keycode)
		{
		case Keys.LEFT:
			velocity.x = 0;
			this.state = PlayerState.IDLE;
			break;
		case Keys.RIGHT:
			velocity.x = 0;
			this.state = PlayerState.IDLE;
			break;
		case Keys.UP:
			if(velocity.y > 0)
			{
				velocity.y = 0;	
				//this.state = PlayerState.FALLING;
			}
			
			break;

		}
		
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
	
	//Getters and setters for various things.
	
	public void setCollisionLayer(TiledMapTileLayer collisionLayer)
	{
		this.collisionLayer = collisionLayer;
	}
	
	public TiledMapTileLayer getCollisionLayer()
	{
		return collisionLayer;
	}
	
	public void setGravity(float gravity)
	{
		this.gravity = gravity;
	}
	
	public float getGravity()
	{
		return gravity;
	}
	
	public void setSpeed(float speed)
	{
		this.movementSpeed = speed;
	}
	
	public float getSpeed()
	{
		return movementSpeed;
	}
	
	public void setVelocity(Vector2 velocity)
	{
		this.velocity = velocity;
	}
	
	public Vector2 getVelocity()
	{
		return velocity;
	}
	
	/*
	 * Method that sets whether the player is facing right
	 */
	public void setFacingRight(boolean facingRight) {
		this.isFacingRight = facingRight;
	}
	
	/*
	 * Method that gets whether the player is facing right or not (i.e. left)
	 * 
	 * @returns this.isFacingRight, a Boolean:
	 * - True if the player is facing right
	 * - False if the player is facing left
	 */
	public boolean getFacingRight() {
		return this.isFacingRight;
	}
	
	public void setState(PlayerState state) {
		this.state = state;
	}

	public PlayerState getState() {
		return this.state;
	}

}
