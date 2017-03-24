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
		CLOSED, OPEN, OPENING
	}
	
	private FlameState state;
	
	/* =========== ANIMATIONS =========== */
	private Texture flames;
	private Texture eye;

	private Animation<TextureRegion> flameAnim;
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
		
		TextureRegion[] eyeFrames2 = new TextureRegion[24 * 1];
		index = 0;
		for (int i = 0; i < 1; i++) {
			for (int j = 0; j < 24; j++) {
				eyeFrames2[index++] = eyeFrames[i][j];
			}
		}
		
		this.flameAnim = new Animation<TextureRegion>(0.1f, flameFrames2);
		this.eyeOpeningAnim = new Animation<TextureRegion>(0.1f, eyeFrames2);
	}

	public void drawEnemy(Batch batch, boolean debug) {
		Animation<TextureRegion> anim = null;
		Animation<TextureRegion> anim2 = null;
		
		boolean loop = true;
		
		update(Gdx.graphics.getDeltaTime());
		
		anim = this.flameAnim;
		anim2 = this.eyeOpeningAnim;
		
		TextureRegion currentFlame = anim.getKeyFrame(stateTime, loop);
		this.width = currentFlame.getRegionWidth();
		this.height = currentFlame.getRegionHeight();
		
		TextureRegion currentEye = anim2.getKeyFrame(stateTime, loop);
		
		batch.draw(currentFlame, this.x, this.y);
		batch.draw(currentEye, this.x, this.y);
	}

	public void update(float delta) {
		this.stateTime += delta;
		
		// TODO: Implement behavior
	}

}
