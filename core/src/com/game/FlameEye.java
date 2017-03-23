package com.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

public class FlameEye extends Enemy {

	/* =========== ANIMATIONS =========== */
	private Texture flames;
	private Texture eye;

	private Animation<TextureRegion> flameAnim;
	private Animation<TextureRegion> eyeAnim;
	/* ================================== */
	
	public FlameEye(Vector2 spawnPoint, TiledMapTileLayer collisionLayer,
			float moveSpeed) {
		super(spawnPoint, collisionLayer, moveSpeed);
		
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
			for (int j = 0; j < 8; j++) {
				eyeFrames2[index++] = eyeFrames[i][j];
			}
		}
		
		this.flameAnim = new Animation<TextureRegion>(0.1f, flameFrames2);
		this.eyeAnim = new Animation<TextureRegion>(0.1f, eyeFrames2);
	}

	public TextureRegion drawEnemy() {
		Animation<TextureRegion> anim = null;
		boolean loop = true;
		
		update(Gdx.graphics.getDeltaTime());
		
		anim = this.flameAnim;
		
		TextureRegion currentFrame = anim.getKeyFrame(stateTime, loop);
		this.width = currentFrame.getRegionWidth();
		this.height = currentFrame.getRegionHeight();
		
		return currentFrame;
	}

	public void update(float delta) {
		this.stateTime += delta;
		
		// TODO: Implement behavior
	}

}
