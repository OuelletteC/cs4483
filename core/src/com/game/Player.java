package com.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Player extends Sprite implements InputProcessor
{
	
	/** Movement vector storing x/y velocity */
	private Vector2 velocity = new Vector2(0, -1);
	private Vector2 spawn = new Vector2();
	private float movementSpeed = 125, gravity = 60 * 1.8f; //Movement speed and gravity vector upon the player
	private boolean canJump;
	
	public Player(Sprite sprite, Vector2 spawnPoint)
	{
		super(sprite); //how the player looks
		
	}
	
	public void draw(Batch spriteBatch)
	{
	update(Gdx.graphics.getDeltaTime());
	super.draw(spriteBatch);	
	
	}
	
    public void update(float delta) 
    {
        velocity.y -= gravity * delta; //applies gravity to the y velocity for each unit of time (delta) that passes
        
    	//Objects have a terminal velocity when gravity is applied, so this handles that
        if(velocity.y > movementSpeed)
        	velocity.y = movementSpeed;
        else if(velocity.y < -movementSpeed)
        	velocity.y = -movementSpeed;
    	
        setX(getX() + velocity.x * delta);
        setY(getY() + velocity.y * delta);
        
    }

	@Override
	public boolean keyDown(int keycode) { //This method handles what occurs when the key is pressed DOWN
		
		switch(keycode)
		{
		case Keys.LEFT:
			velocity.x = -movementSpeed;
			break;
		case Keys.RIGHT:
			velocity.x = movementSpeed;
			break;
		case Keys.UP:
			if(canJump)
			{
			velocity.y = movementSpeed;
			canJump = false; //We need to set a flag so that the player cant jump over and over again.
			}
			break;

		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) { //So when the player releases the key, the player will stop moving
		
		switch(keycode)
		{
		case Keys.LEFT:
			velocity.x = 0;
			break;
		case Keys.RIGHT:
			velocity.x = 0;
			break;
		case Keys.UP:
			velocity.y = 0;
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

}
