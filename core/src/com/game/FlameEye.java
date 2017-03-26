package com.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.game.levels.Level;

public class FlameEye extends Enemy {
	
	private enum FlameState {
		CLOSING, CLOSED, OPENING, OPEN
	}
	
	private FlameState state;
	
	private float eyeOpeningTime = 0; // variable to store the stateTime of the eye's animations
	private float eyeClosingTime = 0; // variable to store the stateTime of the eye's animations
	
	/* =========== ANIMATIONS =========== */
	private Texture flames;
	private Texture eye;

	private Animation<TextureRegion> flameAnim;
	private Animation<TextureRegion> eyeClosingAnim;
	private Animation<TextureRegion> eyeClosedAnim;
	private Animation<TextureRegion> eyeOpeningAnim;
	private Animation<TextureRegion> eyeOpenedAnim;
	/* ================================== */
	
	public FlameEye(Vector2 spawnPoint, TiledMapTileLayer collisionLayer,
			float moveSpeed, Level level) {
		super(spawnPoint, collisionLayer, moveSpeed, level);
		
		this.state = FlameState.CLOSED;
		
		this.movementSpeed = 15;
		this.gravity = 0;
		
		loadTextures();
	}
	
	private void loadTextures() {
		this.flames = new Texture("flames-sheet-resized.png");
		this.eye = new Texture("eyeblink-sheet-resized.png");
		
		TextureRegion[][] flameFrames = TextureRegion.split(flames, (flames.getWidth() / 8), flames.getHeight());
		TextureRegion[][] eyeFrames = TextureRegion.split(eye, (eye.getWidth() / 24), eye.getHeight());
		
		TextureRegion[] flameFrames2 = new TextureRegion[8 * 1];
		int index = 0;
		for (int i = 0; i < 1; i++) {
			for (int j = 0; j < 8; j++) {
				flameFrames2[index++] = flameFrames[i][j];
			}
		}
		
		TextureRegion[] eyeClosedFrames = new TextureRegion[1];
		eyeClosedFrames[0] = eyeFrames[0][0];
		
		TextureRegion[] eyeOpeningFrames = new TextureRegion[11 * 1];
		index = 0;
		for (int i = 0; i < 1; i++) {
			for (int j = 1; j < 12; j++) {
				eyeOpeningFrames[index++] = eyeFrames[i][j];
			}
		}
		
		TextureRegion[] eyeOpenFrames = new TextureRegion[9];
		index = 0;
		for (int i = 0; i < 1; i++) {
			for (int j = 12; j < 21; j++) {
				eyeOpenFrames[index++] = eyeFrames[i][j];
			}
		}
		
		TextureRegion[] eyeClosingFrames = new TextureRegion[4];
		index = 0;
		for (int i = 0; i < 1; i++) {
			for (int j = 20; j < 24; j++) {
				eyeClosingFrames[index++] = eyeFrames[i][j];
			}
		}
		
		this.flameAnim = new Animation<TextureRegion>(0.1f, flameFrames2);
		this.eyeClosedAnim = new Animation<TextureRegion>(0.1f, eyeClosedFrames);
		this.eyeOpeningAnim = new Animation<TextureRegion>(4f, eyeOpeningFrames);
		this.eyeOpenedAnim = new Animation<TextureRegion>(0.1f, eyeOpenFrames);
		this.eyeClosingAnim = new Animation<TextureRegion>(4f, eyeClosingFrames);
	}

	public void drawEnemy(Batch batch, boolean debug) {
		Animation<TextureRegion> anim = null;
		Animation<TextureRegion> anim2 = null;
		
		boolean loop = true;
		
		update(Gdx.graphics.getDeltaTime());
		
		anim = this.flameAnim;
		
		TextureRegion currentFlame = anim.getKeyFrame(stateTime, loop);
		this.width = currentFlame.getRegionWidth();
		this.height = currentFlame.getRegionHeight();
		
		// optimized for their current width and height
		hitXStart = x + 13;
		hitYStart = y + 2;
		
		this.hitWidth = 0;
		this.hitHeight = 20;
		
		TextureRegion currentEye;
		batch.setColor(0,0,0,1);
		// determine, based on the state, which animation to play for the eye
		switch(this.state) {
		case CLOSING:
			anim2 = this.eyeClosingAnim;
			currentEye = anim2.getKeyFrame(eyeClosingTime, false);
			eyeClosingTime++;
			break;
		case CLOSED: // heh
			anim2 = this.eyeClosedAnim;
			currentEye = anim2.getKeyFrame(stateTime, loop);
			break;
		case OPEN:
			anim2 = this.eyeOpenedAnim;
			currentEye = anim2.getKeyFrame(stateTime, loop);
			break;
		case OPENING:
			anim2 = this.eyeOpeningAnim;
			currentEye = anim2.getKeyFrame(eyeOpeningTime, false);
			eyeOpeningTime++;
			break;
		default:
			anim2 = this.eyeOpenedAnim;
			currentEye = anim2.getKeyFrame(stateTime, loop);
			break;
		}
		
		batch.draw(currentFlame, this.x, this.y);
		batch.setColor(1,1,1,1);
		batch.draw(currentEye, this.x, this.y);
	}

	public void update(float delta) {
		this.stateTime += delta;
		//setY(getY() + velocity.y * delta);
		
		
		// calculate vector from the middle of the player's sprite rather than the lower left edge
		Vector2 v1 = new Vector2((currLevel.player.getX() + currLevel.player.getWidth()) / 2, 
				(currLevel.player.getY() + currLevel.player.getHeight()) / 2);
		// ditto for the flame-eye
		Vector2 v2 = new Vector2((x + width) / 2, (y + height) / 2);
		
		float visionRange = 100;
		
		// calculate the x,y-distance between the player and the flame-eye 
		Vector2 v3 = v1.sub(v2);
		
		if (Math.abs(v3.x) < visionRange && Math.abs(v3.y) < visionRange) {
			setX(getX() + velocity.x * delta);
			setY((0.5f * (float)Math.sin(getX() / 10) + getY()) + velocity.y * delta);
			
			if(this.state.equals(FlameState.OPENING) || this.state.equals(FlameState.OPEN)) {
				if(this.eyeOpeningAnim.isAnimationFinished(eyeOpeningTime)) {
					this.state = FlameState.OPEN;
					eyeOpeningTime = 0;
				}
			}
			else {
				this.state = FlameState.OPENING;
			}
			
			if (v3.x > 0) {
				velocity.x = movementSpeed;
			}
			else {
				velocity.x = -movementSpeed;
			}
			
			if(v3.y > 0) {
				velocity.y = movementSpeed;
			}
			else {
				velocity.y = -movementSpeed;
			}
		}
		else {
			velocity.x = 0;
			velocity.y = 0;
			if(this.state.equals(FlameState.CLOSING) || this.state.equals(FlameState.CLOSED)) {
				if(this.eyeClosingAnim.isAnimationFinished(eyeClosingTime)) {
					this.state = FlameState.CLOSED;
					eyeClosingTime = 0;
				}
			}
			else {
				this.state = FlameState.CLOSING;
			}
		}
		
		//super.collision(delta);
		
		// TODO: Implement behavior
	}

}
