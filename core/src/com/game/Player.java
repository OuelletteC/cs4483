package com.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.game.levels.Level;

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
	private float movementSpeed = 125, gravity = 20 * 9.8f; //Movement speed and gravity vector upon the player
	private TiledMapTileLayer collisionLayer;
	private Level currLevel;
	
	private int timesJumped = 0;
	
	private boolean canJump, canDoubleJump;
	private boolean wallCling, collidedWithWall;
	private boolean canHover;
	private boolean isInvincible;
	private boolean isFacingRight;
	private boolean isDead;
//	private boolean onObject, onObject2, onObject3;
	private boolean victory, clingSoundPlayed;
	
	private PlayerState state;
	private int healthPoints;
	private int currentLayer;
	private int invincibleTimer, hoverTimer;
	
	//Music is streamed from a place in storage to deal with the higher file sizes, unlike sounds. If you end up calling one to play, dispose of it when appropriate.
	Music musicForLayer1 = Gdx.audio.newMusic(Gdx.files.internal("soundAssets/Furtive.wav"));	
	Music musicForLayer2 = Gdx.audio.newMusic(Gdx.files.internal("soundAssets/Voice_001.wav"));	
	Music musicForLayer3 = Gdx.audio.newMusic(Gdx.files.internal("soundAssets/Cacophony.wav"));	
	
	Sound soundEffect;
	
	
	private float stateTime = 0;
	
	/* =========== ANIMATIONS =========== */
	private Texture idle;
	private Texture turnaround;
	private Texture walking;
	private Texture running;
	private Texture clinging;
	private Texture jumpNFall;
	
	private Animation<TextureRegion> idleAnim;
	private Animation<TextureRegion> turnaroundAnim;
	private Animation<TextureRegion> walkingAnim;
	private Animation<TextureRegion> runningAnim;
	private Animation<TextureRegion> clingAnim;
	private Animation<TextureRegion> jumpAnim;
	private Animation<TextureRegion> fallAnim;
	/* ================================== */
	
	public Player(Vector2 spawnPoint, TiledMapTileLayer collisionLayer, Level level)
	{
		this.collisionLayer = collisionLayer;
		this.currLevel = level;
		
		this.state = PlayerState.IDLE; // initialize the state to idle, though this will likely update
		this.isFacingRight = true; // initialize the facing to be to the right
		this.healthPoints = 2;
		
		this.isInvincible = true;
		this.invincibleTimer = 120;
		this.hoverTimer = 80;
		
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
		this.clinging = new Texture("Polarity_PC_Wall-Hang-Sheet.png");
		this.jumpNFall = new Texture("Polarity_PC_Jump-Sheet.png");
		
		TextureRegion[][] idleFrames = TextureRegion.split(idle, idle.getWidth(), idle.getHeight());
		TextureRegion[][] turnaroundFrames = TextureRegion.split(turnaround, (turnaround.getWidth() / 8), turnaround.getHeight());
		TextureRegion[][] walkingFrames = TextureRegion.split(walking, (walking.getWidth() / 4), walking.getHeight());
		TextureRegion[][] runningFrames = TextureRegion.split(running, (running.getWidth() / 8), running.getHeight());
		TextureRegion[][] clingFrames = TextureRegion.split(clinging, clinging.getWidth(), clinging.getHeight());
		TextureRegion[][] jumpNFallFrames = TextureRegion.split(jumpNFall, jumpNFall.getWidth() / 3, jumpNFall.getHeight());
		
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
		
		TextureRegion [] clingFrames2 = new TextureRegion[1];
		clingFrames2[0] = clingFrames[0][0];
		
		TextureRegion[] jumpFrames = new TextureRegion[2];
		jumpFrames[0] = jumpNFallFrames[0][0];
		jumpFrames[1] = jumpNFallFrames[0][1];
		
		TextureRegion[] fallFrames = new TextureRegion[1];
		fallFrames[0] = jumpNFallFrames[0][0];
		
		this.idleAnim = new Animation<TextureRegion>(0.1f, idleFrames2);
		this.turnaroundAnim = new Animation<TextureRegion>(0.1f, turnaroundFrames2);
		this.walkingAnim = new Animation<TextureRegion>(0.1f, walkingFrames2);
		this.runningAnim = new Animation<TextureRegion>(0.1f, runningFrames2);
		this.clingAnim = new Animation<TextureRegion>(0.1f, clingFrames2);
		this.jumpAnim = new Animation<TextureRegion>(0.1f, jumpFrames);
		this.fallAnim = new Animation<TextureRegion>(0.1f, fallFrames);
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
			if(this.currentLayer <= 2) { // if the current layer is 2 or shallower, walk
				anim = this.walkingAnim;
			}
			else { // if the layer is 3 or deeper, RUN
				anim = this.runningAnim;
			}
			break;
		case JUMPING:
			// play the jumping animation
			anim = this.jumpAnim;
			loop = false;
			break;
		case FALLING:
			// play the falling animation
			anim = this.fallAnim;
			break;
		case CLING:
			// play the cling animation
			anim = this.clingAnim;
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
		if (this.isInvincible) {
			// draw the player on every other frame of invincibility to
			// simulate the "iframe blinking" effect
			// The "< 10" check is to give the player visual feedback such that they know they aren't invincible
			if(this.invincibleTimer % 2 != 0 || this.invincibleTimer < 10) {
				batch.draw(currentFrame, this.x, this.y);
			}
		}
		else {
			if(this.healthPoints < 2) {
				healthLowColours(batch, currentFrame);
			}
			batch.draw(currentFrame, this.x, this.y);
		}
	}
	
	public void update(float delta, Enemy[] eAry) {
		
		try {
			stateTime += delta; // update the stateTime based on the delta

			velocity.y -= gravity * delta; //applies gravity to the y velocity for each unit of time (delta) that passes

			//Objects have a terminal velocity when gravity is applied, so this handles that
			if(velocity.y > movementSpeed)
				velocity.y = movementSpeed;
			else if(velocity.y < -movementSpeed)
				velocity.y = -movementSpeed;

			//We need to handle what happens when the player collides with a tile, so we save the old position in case we need to move the player BACK if they collide with something
			float oldX = getX(), oldY = getY(), tileWidth = collisionLayer.getTileWidth(), tileHeight = collisionLayer.getTileHeight();
			boolean collidedX = false, collidedY = false, death = false, onBubble = false, onBubble2 = false, onBubble3 = false, victoryTile = false;

			// Decrement the invincibility timer if the player is invincible
			if(isInvincible == true) {
				invincibleTimer -= 1;
				if(invincibleTimer <= 0) { // if the invincibility timer hits zero, the player is no longer invincible
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

			// if the player is in a DAMAGED state, they won't be able to be controlled
			// so this handles the collision and returns control to the player after
			// they touch down. Castlevania style.

			// SEE ALSO: The "else" block for this code below for collision if the player DOES
			// have control
			if(this.state.equals(PlayerState.DAMAGED)) {
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

			}
			else { // player is not currently in the damaged state
				setY(getY() + velocity.y * delta);

				// FALLING
				if (velocity.y < 0) {
					// bottom left
					collidedY = collisionLayer.getCell((int) (getX() / tileWidth), (int) (getY() / tileHeight)).getTile().getProperties().containsKey("blocked");

					death = collisionLayer.getCell((int) (getX() / tileWidth), (int) (getY() / tileHeight)).getTile().getProperties().containsKey("death");

					//bottom middle
					if(!collidedY)
						collidedY = collisionLayer.getCell((int) ((getX() + getWidth() / 2 ) / tileWidth), (int) (getY() / tileHeight)).getTile().getProperties().containsKey("blocked");

					if(!death)
						death = collisionLayer.getCell((int) ((getX() + getWidth() / 2 ) / tileWidth), (int) (getY() / tileHeight)).getTile().getProperties().containsKey("death");

					//bottom right
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

					// this is for the wall-cling + jump workaround
					// if this detects that the player has collided on Y with an X velocity of less
					// than the movementSpeed (such as, half of the movementSpeed variable)
					// then it will stop the player's x-velocity on collision.
					if(Math.abs(velocity.x) < movementSpeed) {
						velocity.x = 0;
					}
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
				} //end of collision nightmare



				/**
				 * While it looks the part, this block here is actually not a collision dealio. Instead, it checks to see if the player character is facing a bubble, and depending on their direction
				 * which end of their hitbox to use. This is so they can activate bubbles with the 'E' key in the KeyDown() function. 
				 * 
				 */

				if(isFacingRight) 
				{
					victoryTile = collisionLayer.getCell((int) ( ( (getX() + getWidth() ) ) / tileWidth), (int) ( ( getY() + ( getHeight() / 2 )  ) / tileHeight)).getTile().getProperties().containsKey("victory");
					victory = victoryTile;

				}
				else if(!isFacingRight)
				{
					victoryTile = collisionLayer.getCell((int) ( getX() / tileWidth ), (int) ( ( getY() + ( getHeight() / 2 )  ) / tileHeight)).getTile().getProperties().containsKey("victory");
					victory = victoryTile;

				}
				else
				{
					victoryTile = collisionLayer.getCell((int) ( ( (getX() + getWidth() ) / 2) / tileWidth), (int) ( ( getY() + ( getHeight() / 2 )  ) / tileHeight)).getTile().getProperties().containsKey("bubble3");
					victory = victoryTile;

				}

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

				/*
				 * The block below handles music, as well as specific mechanics which activate upon entering layers. With the music, its important to dispose() of the assets when theyre done being used.
				 * */

				if(currentLayer == 1)
				{
					musicForLayer2.stop();
					musicForLayer2.dispose();
					musicForLayer3.stop();
					musicForLayer3.dispose();
					musicForLayer1.setVolume(0.2f);
					musicForLayer1.setLooping(true);
					musicForLayer1.play();
				}

				if(currentLayer > 1)
				{
					if(currentLayer == 2)
					{
						musicForLayer3.stop();
						musicForLayer3.dispose();
						musicForLayer1.stop();
						musicForLayer1.dispose();
						musicForLayer2.setVolume(0.2f);
						musicForLayer2.setLooping(true);
						musicForLayer2.play();
					}

					if(timesJumped < 2)
					{
						canDoubleJump = true;
					}
					else
					{
						canDoubleJump = false;
					}

					canHover = false;

				} //End of functions for layer 2 and above

				if(currentLayer > 2)
				{
					if(currentLayer == 3)
					{
						musicForLayer1.stop();
						musicForLayer1.dispose();
						musicForLayer2.stop();
						musicForLayer2.dispose();
						musicForLayer3.setVolume(0.2f);
						musicForLayer3.setLooping(true);
						musicForLayer3.play();
					}

					if(collidedWithWall)
					{
						gravity = 0;
						velocity.y = -10;
						timesJumped = 0;
						state = PlayerState.CLING;

						//credits to EdgardEdition from freesound for the wall cling sound!
						if(!clingSoundPlayed && state == PlayerState.CLING)
							Gdx.audio.newSound(Gdx.files.internal("soundAssets/wallClingSound.wav")).play(0.5f);
						Gdx.audio.newSound(Gdx.files.internal("soundAssets/wallClingSound.wav")).dispose();
						clingSoundPlayed = true;
					}
					else
					{
						gravity = 20 * 9.8f;
						clingSoundPlayed = false;
					}

					canHover = false;


				}//end of functions for layer 3 and above

				if(currentLayer > 3)
				{
					canHover = true;

				}//end of functions for layer 4 and above
			}
		}
		catch(NullPointerException e) {
			isDead = true;
		}
	}

	@Override
	//This method handles what occurs when the key is pressed DOWN
	public boolean keyDown(int keycode) {
		if(!(this.state.equals(PlayerState.DAMAGED))) {
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
				if(state == PlayerState.CLING) {
					if(isFacingRight == false) {
						velocity.x = movementSpeed / 2;
						this.isFacingRight = true;
					}
					else {
						velocity.x = -(movementSpeed) / 2;
						this.isFacingRight = false;
					}
				}
				if(canJump) 
				{
					//All credits to dklon from opengameart.org for the jump sound!
					Gdx.audio.newSound(Gdx.files.internal("soundAssets/jumpSound.wav")).play();
					Gdx.audio.newSound(Gdx.files.internal("soundAssets/jumpSound.wav")).dispose();
					timesJumped++;
					velocity.y = movementSpeed;
					this.state = PlayerState.JUMPING;			
					canJump = false; //We need to set a flag so that the player cant jump over and over again midair.
				}
				else if(!canJump && canDoubleJump)
				{
					Gdx.audio.newSound(Gdx.files.internal("soundAssets/jumpSound.wav")).play();
					Gdx.audio.newSound(Gdx.files.internal("soundAssets/jumpSound.wav")).dispose();
					timesJumped++;
					velocity.y = movementSpeed;
					this.state = PlayerState.JUMPING;
					canDoubleJump = false;
				}
				break;
			case Keys.E: // if the E key is pressed, check whether the player is on a bubble tile
				Bubble[] bub = currLevel.getBubbleArray();
				
				for(int i = 0; i < 4; i++) {
					if(bub[i].bubbleCollision(this) == true) {
						// if there is a collision, we want the pop animation (not idle), 
						// so we set it false
						bub[i].setIdle(false);
						
						// // if the player is in the same depth as the bubble, go shallower 
						if((currentLayer - bub[i].getTargetDepth()) == 0) {
							currentLayer--;
							bub[i].setHasDepthChanged(false);
						}
						// if the player is on a level deeper than the bubble by 1, go deeper
						else if((currentLayer - bub[i].getTargetDepth()) == -1) {
							currentLayer++;
							bub[i].setHasDepthChanged(true);
							
						}
					} // end of conditional check for bubble + player interaction
				}
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
				if (velocity.x < 0) {
					velocity.x = 0;
				}
				break;
			case Keys.RIGHT:
				if (velocity.x > 0) {
					velocity.x = 0;
				}
				break;
			case Keys.UP:
				if(velocity.y > 0)
					velocity.y = 0;
				
				if(canHover && hoverTimer >= 0)
				{
					gravity = 9.8f;
					hoverTimer = hoverTimer - 1;
				}
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
	
	public Music getMusicForLayer1()
	{
		return musicForLayer1;
	}
	
	public Music getMusicForLayer2()
	{
		return musicForLayer2;
	}
	
	public Music getMusicForLayer3()
	{
		return musicForLayer3;
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
	
	public boolean getVictory()
	{
		return victory;
	}
	
	public void setVictory(boolean x)
	{
		this.victory = x;
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
	
	public boolean getCollidedX()
	{
		return collidedWithWall;
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


	public int getHealthPoints() {
		return healthPoints;
	}


	public void setHealthPoints(int healthPoints) {
		this.healthPoints = healthPoints;
	}


	public int getCurrentLayer() {
		return currentLayer;
	}
	
	public boolean isDead()
	{
		return isDead;
	}


	public void setCurrentLayer(int currentLayer) {
		this.currentLayer = currentLayer;
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
			
			float x3 = e.getHitXStart();
			float y3 = e.getHitYStart();
			float x4 = e.getHitXStart() + e.getHitWidth();
			float y4 = e.getHitYStart() + e.getHitHeight();
			
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
			velocity.x = -(movementSpeed * (float) 1.25);
			velocity.y = movementSpeed / 2;
		}
		else { // facing left, knock back right
			velocity.x = movementSpeed * (float) 1.25;
			velocity.y = movementSpeed / 2;
		}
	}
	
	private void healthLowColours(Batch batch, TextureRegion currFrame) {
		TextureRegion cf2 = currFrame;
		float st = this.stateTime;
		
		if(st % 20 < 5) {
			batch.setColor(1.0f, 0, 0, 0.5f);
			batch.draw(cf2, this.x - 2, this.y - 1, (this.width / 2), (this.height / 2), 
					this.width, this.height, 1, 1, 
					(((st % 10)) * 4.5f)); // red
			batch.setColor(0, 1.0f, 0, 0.5f);
			batch.draw(cf2, this.x, this.y + 2, (this.width / 2), (this.height / 2), 
					this.width, this.height, 1, 1, 
					-(((st % 10)) * 4.5f)); // green
			batch.setColor(0, 0, 1.0f, 0.5f);
			batch.draw(cf2, this.x + 2, this.y - 1, (this.width / 2), (this.height / 2), 
					this.width, this.height, 1, 1, 
					(((st % 10)) * 4.5f)); // blue
		}
		else if(st % 20 >= 5 && st % 20 < 10) {
			batch.setColor(1.0f, 0, 0, 0.5f);
			batch.draw(cf2, this.x - 2, this.y - 1, (this.width / 2), (this.height / 2), 
					this.width, this.height, 1, 1, 
					-((st % 10) - 10) * 4.5f); // red
			batch.setColor(0, 1.0f, 0, 0.5f);
			batch.draw(cf2, this.x, this.y + 2, (this.width / 2), (this.height / 2), 
					this.width, this.height, 1, 1, 
					((st % 10) - 10) * 4.5f); // green
			batch.setColor(0, 0, 1.0f, 0.5f);
			batch.draw(cf2, this.x + 2, this.y - 1, (this.width / 2), (this.height / 2), 
					this.width, this.height, 1, 1, 
					-((st % 10) - 10) * 4.5f); // blue
		}
		else if(st % 20 >= 10 && st % 20 < 15) {
			batch.setColor(1.0f, 0, 0, 0.5f);
			batch.draw(cf2, this.x - 2, this.y - 1, (this.width / 2), (this.height / 2), 
					this.width, this.height, 1, 1, 
					-((st % 10)) * 4.5f); // red
			batch.setColor(0, 1.0f, 0, 0.5f);
			batch.draw(cf2, this.x, this.y + 2, (this.width / 2), (this.height / 2), 
					this.width, this.height, 1, 1, 
					((st % 10)) * 4.5f); // green
			batch.setColor(0, 0, 1.0f, 0.5f);
			batch.draw(cf2, this.x + 2, this.y - 1, (this.width / 2), (this.height / 2), 
					this.width, this.height, 1, 1, 
					-((st % 10)) * 4.5f); // blue
		}
		else {
			batch.setColor(1.0f, 0, 0, 0.5f);
			batch.draw(cf2, this.x - 2, this.y - 1, (this.width / 2), (this.height / 2), 
					this.width, this.height, 1, 1, 
					((st % 10) - 10) * 4.5f); // red
			batch.setColor(0, 1.0f, 0, 0.5f);
			batch.draw(cf2, this.x, this.y + 2, (this.width / 2), (this.height / 2), 
					this.width, this.height, 1, 1, 
					-((st % 10) - 10) * 4.5f); // green
			batch.setColor(0, 0, 1.0f, 0.5f);
			batch.draw(cf2, this.x + 2, this.y - 1, (this.width / 2), (this.height / 2), 
					this.width, this.height, 1, 1, 
					((st % 10) - 10) * 4.5f); // blue
		}
		batch.setColor(1,1,1,1);
	}
}
