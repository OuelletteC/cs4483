package com.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

public class Player implements InputProcessor
{
	public enum PlayerState {
		IDLE, WALKING, JUMPING, FALLING, CLING, HOVER,
		DAMAGED, DEAD
	}
	
	private float x, y;
	private float width, height;
	
	/** Movement vector storing x/y velocity */
	private Vector2 velocity = new Vector2(0, -1);
	private float movementSpeed = 125, gravity = 20 * 9.8f;; //Movement speed and gravity vector upon the player
	private TiledMapTileLayer collisionLayer;
	
	private int timesJumped = 0;
	
	private boolean canJump, canDoubleJump;
	private boolean wallCling, collidedWithWall;
	private boolean isInvincible;
	private boolean isFacingRight;
	private boolean isDead;
	private boolean onObject;
	
	private PlayerState state;
	private int healthPoints;
	private int currentLayer; //Keep track of the current layer the player is on
	private int invincibleTimer;
	
	private float stateTime = 0;
	
	/* =========== ANIMATIONS =========== */
	private Texture idle;
	private Texture turnaround;
	private Texture walking;
	private Texture running;
	
	private Animation<TextureRegion> idleAnim;
	private Animation<TextureRegion> turnaroundAnim;
	private Animation<TextureRegion> walkingAnim;
	private Animation<TextureRegion> runningAnim;
	/* ================================== */
	
	public Player(Vector2 spawnPoint, TiledMapTileLayer collisionLayer)
	{
		this.collisionLayer = collisionLayer;
		this.state = PlayerState.IDLE; // initialize the state to idle, though this will likely update
		this.isFacingRight = true; // initialize the facing to be to the right
		this.healthPoints = 2;
		
		this.isInvincible = true;
		this.invincibleTimer = 120;
		
		this.x = spawnPoint.x;
		this.y = spawnPoint.y;
		
		loadTextures();
		
		this.width = idle.getWidth();
		this.height = idle.getHeight();
	}
	
	
	/*
	 * Private method that is called when the Player object is created.
	 * Initializes all of the animation fields, so that they can be used.
	 */
	private void loadTextures() {
		this.idle = new Texture("playerTest.png");
		this.turnaround = new Texture("polarity_mc_turnarnound-sheet.png");
		this.walking = new Texture("polarity_pc_walk-sheet.png");
		this.running = new Texture("polarity_pc_run-bounce-sheet.png");
		
		TextureRegion[][] idleFrames = TextureRegion.split(idle, idle.getWidth(), idle.getHeight());
		TextureRegion[][] turnaroundFrames = TextureRegion.split(turnaround, (turnaround.getWidth() / 8), turnaround.getHeight());
		TextureRegion[][] walkingFrames = TextureRegion.split(walking, (walking.getWidth() / 4), walking.getHeight());
		TextureRegion[][] runningFrames = TextureRegion.split(running, (running.getWidth() / 8), running.getHeight());
		
		TextureRegion[] idleFrames2 = new TextureRegion[1 * 1];
		int index = 0;
		for (int i = 0; i < 1; i++) {
			for (int j = 0; j < 1; j++) {
				//idleFrames2[index++] = idleFrames[i][j];
				
				// TODO: Temporary! Currently we're just getting a specific
				// frame of the turnaround animation.
				
				idleFrames2[index++] = turnaroundFrames[i][1];
			}
		}
		
		
		TextureRegion[] turnaroundFrames2 = new TextureRegion[8 * 1];
		index = 0;
		for (int i = 0; i < 1; i++) {
			for (int j = 0; j < 8; j++) {
				turnaroundFrames2[index++] = turnaroundFrames[i][j];
			}
		}
		
		
		TextureRegion[] walkingFrames2 = new TextureRegion[4 * 1];
		index = 0;
		for (int i = 0; i < 1; i++) {
			for (int j = 0; j < 4; j++) {
				walkingFrames2[index++] = walkingFrames[i][j];
			}
		}
		
		TextureRegion[] runningFrames2 = new TextureRegion[8 * 1];
		index = 0;
		for (int i = 0; i < 1; i++) {
			for (int j = 0; j < 8; j++) {
				runningFrames2[index++] = runningFrames[i][j];
			}
		}
		
		this.idleAnim = new Animation<TextureRegion>(0.1f, idleFrames2);
		this.turnaroundAnim = new Animation<TextureRegion>(0.1f, turnaroundFrames2);
		this.walkingAnim = new Animation<TextureRegion>(0.1f, walkingFrames2);
		this.runningAnim = new Animation<TextureRegion>(0.1f, runningFrames2);
	}
	
	/*
	 * drawPlayer()
	 * 
	 * Selects the player's animation based on the current state and
	 * the stateTime, which are decided by calling update() PRIOR TO
	 * calling drawPlayer().
	 */
	public void drawPlayer(Batch batch, boolean debug) {
		Animation<TextureRegion> anim = null;
		boolean loop = true;
		
		switch(this.state) {		
		case IDLE:
			// do we even have an idle animation...?
			anim = this.idleAnim;
			break;
		case WALKING:
			// play the walking animation
			if(this.currentLayer <= 2) {
				anim = this.walkingAnim;
			}
			else {
				anim = this.runningAnim;
			}
			break;
		case JUMPING:
			// play the spinning animation lol
			anim = this.turnaroundAnim;
			break;
		default:
			anim = this.idleAnim;
			break;
		}
		
		TextureRegion currentFrame = anim.getKeyFrame(stateTime, loop);
		this.width = currentFrame.getRegionWidth();
		this.height = currentFrame.getRegionHeight();
		
		// draw contingent on the isFacingRight flag
		if(this.isFacingRight == false) {
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
	
	public void update(float delta, Enemy[] eAry) {
		stateTime += delta; // update the stateTime based on the delta

		velocity.y -= gravity * delta; //applies gravity to the y velocity for each unit of time (delta) that passes

		//Objects have a terminal velocity when gravity is applied, so this handles that
		if(velocity.y > movementSpeed)
			velocity.y = movementSpeed;
		else if(velocity.y < -movementSpeed)
			velocity.y = -movementSpeed;

		//We need to handle what happens when the player collides with a tile, so we save the old position in case we need to move the player BACK if they collide with something
		float oldX = getX(), oldY = getY(), tileWidth = collisionLayer.getTileWidth(), tileHeight = collisionLayer.getTileHeight();
		boolean collidedX = false, collidedY = false, death = false, onBubble = false;
		if(isInvincible == true) {
			invincibleTimer -= 1;
			if(invincibleTimer <= 0) {
				isInvincible = false;
			}
		}
		if(checkEnemyCollision(eAry) == true && this.state != PlayerState.DAMAGED
				&& isInvincible == false) {
			this.state = PlayerState.DAMAGED;
			damageRecoil();
			this.isInvincible = true;
			this.invincibleTimer = 120;
		}
		
		if(this.state.equals(PlayerState.DAMAGED) || this.state.equals(PlayerState.FALLING)) {
			//move on y
			setY(getY() + velocity.y * delta);
			
			if(velocity.y < 0) // moving downward
			{
				collidedY = collisionLayer.getCell((int) (getX() / tileWidth), (int) (getY() / tileHeight)).getTile().getProperties().containsKey("blocked");
				death = collisionLayer.getCell((int) (getX() / tileWidth), (int) (getY() / tileHeight)).getTile().getProperties().containsKey("death");
				
				if(!collidedY)
					collidedY = collisionLayer.getCell((int) ((getX() + getWidth() / 2 ) / tileWidth), (int) (getY() / tileHeight)).getTile().getProperties().containsKey("blocked");
				
				if(!death)
					death = collisionLayer.getCell((int) ((getX() + getWidth() / 2 ) / tileWidth), (int) (getY() / tileHeight)).getTile().getProperties().containsKey("death");
				
				if(!collidedY)
					collidedY = collisionLayer.getCell((int) ((getX() + getWidth()) / tileWidth), (int) (getY() / tileHeight)).getTile().getProperties().containsKey("blocked");     
				
				if(!death)
					death = collisionLayer.getCell((int) ((getX() + getWidth()) / tileWidth), (int) (getY() / tileHeight)).getTile().getProperties().containsKey("death");   
				
				// re-enable the jump once the player has touched down
				canJump = collidedY;
				
				if(canJump)
					timesJumped = 0;
				
				isDead = death;
			}
			else if(velocity.y > 0) // moving upwards
			{
				collidedY = collisionLayer.getCell((int) (getX() / tileWidth), (int) ( (getY() + getHeight() )/ tileHeight)).getTile().getProperties().containsKey("blocked");
				if(!collidedY)
					collidedY = collisionLayer.getCell((int) (( getX() + getWidth() / 2 ) / tileWidth), (int) ( (getY() + getHeight())/ tileHeight)).getTile().getProperties().containsKey("blocked");
				if(!collidedY)
					collidedY = collisionLayer.getCell((int) (getX() / tileWidth), (int) ( (getY() + getHeight()) / tileHeight)).getTile().getProperties().containsKey("blocked");
			}
			if(collidedY) //reaction to y collision
			{
				/* 
				 * Once the player touches the ground, control should be restored
				 * and the momentum should stop. To do this, we return the player
				 * to the IDLE state, and set both x and y velocities to 0.
				 */
				setY(oldY);
				setX(oldX);
				velocity.y = 0;
				velocity.x = 0;
				this.state = PlayerState.IDLE;
			}

			// move on x
			setX(getX() + velocity.x * delta);

			//moving left
			if(velocity.x < 0)
			{
				collidedX = collisionLayer.getCell((int) (getX() / tileWidth), (int) ((getY() + getHeight()) / tileHeight)).getTile().getProperties().containsKey("blocked");
				if(!collidedX)
					collidedX = collisionLayer.getCell((int) (getX() / tileWidth), (int) ((getY() + getHeight() / 2 ) / tileHeight)).getTile().getProperties().containsKey("blocked");
				if(!collidedX)
					collidedX = collisionLayer.getCell((int) (getX() / tileWidth), (int) (getY() / tileHeight)).getTile().getProperties().containsKey("blocked");
				
				collidedWithWall = collidedX;
			}
			else if(velocity.x > 0) //moving right
			{
				collidedX = collisionLayer.getCell((int) ((getX() + getWidth()) / tileWidth), (int) ((getY() + getHeight()) / tileHeight)).getTile().getProperties().containsKey("blocked");
				if(!collidedX)
					collidedX = collisionLayer.getCell((int) ((getX() + getWidth()) / tileWidth), (int) ((getY() + getHeight() / 2 ) / tileHeight)).getTile().getProperties().containsKey("blocked");
				if(!collidedX)
					collidedX = collisionLayer.getCell((int) ((getX() + getWidth()) / tileWidth), (int) (getY() / tileHeight)).getTile().getProperties().containsKey("blocked");
				
				collidedWithWall = collidedX; 
			}

			if(collidedX) //reaction to x collision
			{
				setX(oldX);
				velocity.x = 0;
			}

		} //end of specific damage if
		else { // player is not currently in the damaged state
			setY(getY() + velocity.y * delta);
			
			//Checks to see if the player is currently on a tile which, when translated to the object layer, is a bubble tile. If so, set onObject to true
			
			if(isFacingRight)
			{
				onBubble = collisionLayer.getCell((int) ( ( (getX() + getWidth() ) ) / tileWidth), (int) ( ( getY() + ( getHeight() / 2 )  ) / tileHeight)).getTile().getProperties().containsKey("bubble");
				onObject = onBubble;
			}
			else if(!isFacingRight)
			{
				onBubble = collisionLayer.getCell((int) ( getX() / tileWidth ), (int) ( ( getY() + ( getHeight() / 2 )  ) / tileHeight)).getTile().getProperties().containsKey("bubble");
				onObject = onBubble;
			}
			else
			{
				onBubble = collisionLayer.getCell((int) ( ( (getX() + getWidth() ) / 2) / tileWidth), (int) ( ( getY() + ( getHeight() / 2 )  ) / tileHeight)).getTile().getProperties().containsKey("bubble");
				onObject = onBubble;
			}

			// FALLING
			if (velocity.y < 0) {
				// bottom left
				collidedY = collisionLayer.getCell((int) (getX() / tileWidth), (int) (getY() / tileHeight)).getTile().getProperties().containsKey("blocked");

				//bottom middle
				if(!collidedY)
					collidedY = collisionLayer.getCell((int) ((getX() + ( getWidth() / 2 ) ) / tileWidth), (int) (getY() / tileHeight)).getTile().getProperties().containsKey("blocked");

				//bottom right
				if(!collidedY)
					collidedY = collisionLayer.getCell((int) ((getX() + getWidth()) / tileWidth), (int) (getY() / tileHeight)).getTile().getProperties().containsKey("blocked");

				// re-enable the jump once the player has touched down
				canJump = collidedY; 
			}
			// JUMPING
			else if(velocity.y > 0) {
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
				
				collidedWithWall = collidedX; 

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
				
				collidedWithWall = collidedX;
			}

			if(collidedX) //reaction to x collision
			{
				setX(oldX); //We set it to the oldX because we technically dont move
				velocity.x = 0;
				
			} //end of collision nightmare
			
			
			// Update the PlayerState based on the resolution of the above 
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
			
			if(currentLayer > 1)
			{
				if(timesJumped < 2)
				{
					canDoubleJump = true;
				}
				else
				{
					canDoubleJump = false;
				}
				
			} //End of functions for layer 2 and above
			
			if(currentLayer > 2)
			{
				
				if(collidedWithWall)
				{
					gravity = 0;
					velocity.y = 0;
					timesJumped = 0;
				}
				else
				{
					gravity = 20 * 9.8f;
				}
						
			}//end of functions for layer 3 and above
			
		}
	}

	@Override
	//This method handles what occurs when the key is pressed DOWN
	public boolean keyDown(int keycode) {
		
		if(!(this.state.equals(PlayerState.DAMAGED))) //If not damaged, the player is able to control their movements, as they are not recoiling in horrendous agony
		{
			switch(keycode)
			{
			case Keys.LEFT:
				if(!wallCling)
				{
				velocity.x = -movementSpeed;
				this.isFacingRight = false;
				}
				else if(wallCling)
				{
					velocity.x = -movementSpeed;
					if(collidedWithWall)
					{
						velocity.x = -movementSpeed;
					}
					this.isFacingRight = false;				
				}
				break;
			case Keys.RIGHT:
				
				if(!wallCling)
				{
				velocity.x = movementSpeed;
				this.isFacingRight = true;
				}
				else if(wallCling)
				{
					velocity.x = movementSpeed;
					if(collidedWithWall)
					{
						velocity.x = movementSpeed;
					}
					this.isFacingRight = true;	
				}
				break;
			case Keys.UP:
				if(canJump) 
				{
					timesJumped++;
					velocity.y = movementSpeed;
					this.state = PlayerState.JUMPING;
					canJump = false; //We need to set a flag so that the player cant jump over and over again midair.				
				}
				else if(!canJump && canDoubleJump) //If you are midair, and able to double jump
				{
					timesJumped++;
					velocity.y = movementSpeed;
					this.state = PlayerState.JUMPING;
					canJump = false; //We need to set a flag so that the player cant jump over and over again midair.
				}

				break;
			case Keys.E:
				if(onObject) //!TODO This will, in future, only allow incremental shifts by one depending on the specific bubble that links to the specific layer
					currentLayer++;
				break;
			}
		}
		return true;
	}

	@Override
	//So when the player releases the key, the player will stop moving
	public boolean keyUp(int keycode) {
		if(!(this.state.equals(PlayerState.DAMAGED))) {
			switch(keycode)
			{
			case Keys.LEFT:
				velocity.x = 0;
				break;
			case Keys.RIGHT:
				velocity.x = 0;
				break;
			case Keys.UP:

				if(velocity.y > 0)
					velocity.y = 0;	
				break;
			}
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
	
	public int getTimesJumped()
	{
		return timesJumped;
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
	
	public void setX(float newX) {
		this.x = newX;
	}
	
	public boolean collidedX()
	{
		return collidedWithWall;
	}
	
	public float getX() {
		return this.x;
	}
	
	public boolean isDead()
	{
		return isDead;
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
	
	public boolean isOnBubble()
	{
		return onObject; 
	}
	
	public void setHeight(float height) {
		this.height = height;
	}
	
	public float getHeight() {
		return this.height;
	}


	public int getHealthPoints() {
		return healthPoints;
	}


	public void setHealthPoints(int healthPoints) {
		this.healthPoints = healthPoints;
	}


	public int getCurrentLayer() {
		return currentLayer;
	}


	public void setCurrentLayer(int currentLayer) {
		this.currentLayer = currentLayer;
	}
	
	public boolean getDoubleJump()
	{
		return canDoubleJump;
	}
	
	/*
	 * 
	 * http://stackoverflow.com/questions/23302698/java-check-if-two-rectangles-overlap-at-any-point/32088787#32088787
	 */
	public boolean checkEnemyCollision(Enemy[] eAry) {
		boolean retVal = false;
		
		for(int i = 0; i < eAry.length; i++) {
			Enemy e = eAry[i];
			
			float x1 = this.x;
			float y1 = this.y;
			float x2 = this.x + this.width;
			float y2 = this.y + this.height;
			
			float x3 = e.getX();
			float y3 = e.getY();
			float x4 = e.getX() + e.getWidth();
			float y4 = e.getY() + e.getHeight();
			
			// player coords do not overlap with enemy hitbox
			if(x3 > x2 || y3 > y2 || x1 > x4 || y1 > y4) {
				retVal = false;
			}
			else { // player coords overlap with enemy hitbox
				retVal = true;
				break;
			}
		}
		
		return retVal;
	}
	
	private void damageRecoil() {
		if(isFacingRight == true) { // facing right, knock back left
			velocity.x = -(movementSpeed * (float) 1.05);
			velocity.y = movementSpeed / 2;
		}
		else { // facing left, knock back right
			velocity.x = movementSpeed * (float) 1.05;
			velocity.y = movementSpeed / 2;
		}
	}

	public boolean isInvincible() {
		return isInvincible;
	}


	public void setInvincible(boolean isInvincible) {
		this.isInvincible = isInvincible;
	}


	public int getInvincibleTimer() {
		return invincibleTimer;
	}


	public void setInvincibleTimer(int invincibleTimer) {
		this.invincibleTimer = invincibleTimer;
	}

}
