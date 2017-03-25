package com.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.game.levels.Level;
import com.game.screens.PlayScreen;

public class Tentacles extends Enemy {
	
	private enum FlameState {
		CLOSING, CLOSED, OPENING, OPEN
	}
	
	private FlameState state;
	
	/* =========== ANIMATIONS =========== */
	private Texture tentacleSheet;

	private Animation<TextureRegion> tentacleAnim;

	/* ================================== */
	
	public Tentacles(Vector2 spawnPoint, TiledMapTileLayer collisionLayer,
			float moveSpeed, Level level) {
		super(spawnPoint, collisionLayer, moveSpeed, level);
		
		this.state = FlameState.CLOSED;
		
		this.movementSpeed = 15;
		this.gravity = 0;
		
		loadTextures();
	}
	
	private void loadTextures() {
		
		this.tentacleSheet = new Texture("Tentacle-Sheet-Resized.png");
		
		int numImagesAcross = 10;
		int numImagesHigh = 1;
		
		TextureRegion[][] tentacleFrames = TextureRegion.split(tentacleSheet, (tentacleSheet.getWidth() / numImagesAcross), tentacleSheet.getHeight() / numImagesHigh);
			
		TextureRegion[] tentacleFrames2 = new TextureRegion[10 * 1];
		int index = 0;
		for (int i = 0; i < numImagesHigh; i++) {
			for (int j = 0; j < numImagesAcross; j++) {
				tentacleFrames2[index++] = tentacleFrames[i][j];
			}
		}
		
		this.tentacleAnim = new Animation<TextureRegion>(0.1f, tentacleFrames2);

	}

	public void drawEnemy(Batch batch, boolean debug) {
		Animation<TextureRegion> anim = null;
		
		boolean loop = true;
		
		update(Gdx.graphics.getDeltaTime());
		
		anim = this.tentacleAnim;
		
		TextureRegion currentTentacle = anim.getKeyFrame(stateTime, loop);
		this.width = currentTentacle.getRegionWidth();
		this.height = currentTentacle.getRegionHeight();
		
		hitXStart = x + 10;
		hitYStart = y + 2;
		
		this.hitWidth = 0;
		this.hitHeight = 22;
		
		batch.draw(currentTentacle, this.x, this.y);
	}

	public void update(float delta) {
		
		this.stateTime += delta;
		
		super.collision(delta);
	}

}
