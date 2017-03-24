package com.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

public class FlameEye extends Enemy {
	
	private enum FlameState {
		CLOSING, CLOSED, OPENING, OPEN
	}
	
	private FlameState state;
	
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
			float moveSpeed) {
		super(spawnPoint, collisionLayer, moveSpeed);
		
		this.state = FlameState.CLOSED;
		
		loadTextures();
	}
	
	private void loadTextures() {
		this.flames = new Texture("flames-sheet.png");
		this.eye = new Texture("eyeblink-sheet.png");
		
		TextureRegion[][] flameFrames = TextureRegion.split(flames, (flames.getWidth() / 8), flames.getHeight());
		TextureRegion[][] eyeFrames = TextureRegion.split(eye, (eye.getWidth() / 24), flames.getHeight());
		
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
		this.eyeOpeningAnim = new Animation<TextureRegion>(0.1f, eyeOpeningFrames);
		this.eyeOpenedAnim = new Animation<TextureRegion>(0.1f, eyeOpenFrames);
		this.eyeClosingAnim = new Animation<TextureRegion>(0.1f, eyeClosingFrames);
	}

	public void drawEnemy(Batch batch, boolean debug) {
		Animation<TextureRegion> anim = null;
		Animation<TextureRegion> anim2 = null;
		
		boolean loop = true;
		
		update(Gdx.graphics.getDeltaTime());
		
		anim = this.flameAnim;
		anim2 = this.eyeClosingAnim;
		
		TextureRegion currentFlame = anim.getKeyFrame(stateTime, loop);
		this.width = currentFlame.getRegionWidth();
		this.height = currentFlame.getRegionHeight();
		
		hitXStart = x + 8;
		hitYStart = y + 2;
		
		this.hitWidth = 24;
		this.hitHeight = height - 2;
		
		TextureRegion currentEye = anim2.getKeyFrame(stateTime, loop);
		
		batch.draw(currentFlame, this.x, this.y);
		batch.draw(currentEye, this.x, this.y);
	}

	public void update(float delta) {
		this.stateTime += delta;
		
		// TODO: Implement behavior
	}

}
