package com.game.screens;

import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.TimeUtils;
import com.game.Application;

public class MainMenuScreen implements Screen {
	private static final int PLAY_WIDTH = 150;
	private static final int PLAY_HEIGHT = 100;
	private static final int EXIT_WIDTH = 150;
	private static final int EXIT_HEIGHT = 100;
	private static final int NAME_WIDTH = 400;
	private static final int NAME_HEIGHT = 200;

	private boolean DEBUG;



	Application game;
	Texture playButton;
	Texture exitButton;
	Texture gameName;
	Texture background;
	
	Texture flameEyeSheet;
	Texture playerCharacterSheet;
	
	private Animation<TextureRegion> flameEyeAnim;
	private Animation<TextureRegion> playerCharacterAnim;
	
	private float menuTimer = 0; // time variable used in the rotating "Polarity" functions


	public MainMenuScreen(Application game){
		this.game = game;
		this.DEBUG = game.DEBUG;
		
		playButton = new Texture("play_button.png");
		exitButton = new Texture("exit_button.png");
		gameName = new Texture("game_title.png");
		background = new Texture("Polarity-Title-BG.png");
		
		flameEyeSheet = new Texture("Title_Flaming-Eye_Sheet.png");
		playerCharacterSheet = new Texture("Title_PC_Anim.png");
		
		TextureRegion[][] flameFrames = TextureRegion.split(flameEyeSheet, 
				flameEyeSheet.getWidth() / 3, flameEyeSheet.getHeight() / 3);
		
		TextureRegion[] flameFrames2 = new TextureRegion[8];
		int index = 0;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if(j == 2 && i == 2) {
					// nothing
				}
				else {
					flameFrames2[index++] = flameFrames[i][j];
				}
			}
		}
		
		TextureRegion[][] pcFrames = TextureRegion.split(playerCharacterSheet, 
				playerCharacterSheet.getWidth() / 4, playerCharacterSheet.getHeight() / 4);
		TextureRegion[] pcFrames2 = new TextureRegion[4 * 4];
		index = 0;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				pcFrames2[index++] = pcFrames[i][j];
			}
		}
		
		this.flameEyeAnim = new Animation<TextureRegion>(6f, flameFrames2);
		this.playerCharacterAnim = new Animation<TextureRegion>(5f, pcFrames2);	
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.batch.begin();
		
		/*set the background*/
		game.batch.draw(background, 0, 0);
		
		/*set game name*/		
		float cx = 1280/2-NAME_WIDTH/2;
		float cy = 460;
		
		// draw green
		game.batch.setColor(0, 1, 0, 0.6f);
		game.batch.draw(gameName, (float) (cx + -(5 * Math.cos((float)menuTimer / 30)) ), 
				(float) (cy + (5 * Math.sin((float)menuTimer / 30)) ), NAME_WIDTH, NAME_HEIGHT);
		// draw red
		game.batch.setColor(1, 0, 0, 0.6f);
		game.batch.draw(gameName, (float)(cx + (5 * Math.cos((float)menuTimer / 30)) ), 
				(float)(cy + (5 * Math.sin((float)menuTimer / 30)) ), NAME_WIDTH, NAME_HEIGHT);
		// draw blue
		game.batch.setColor(0, 0, 1, 0.6f);
		game.batch.draw(gameName, (float) (cx + (5 * Math.cos((float)menuTimer / 30)) ), 
				(float) (cy + -(5 * Math.sin((float)menuTimer / 30)) ), NAME_WIDTH, NAME_HEIGHT);
		
		// clear the colours
		game.batch.setColor(1, 1, 1, 1);
		menuTimer++; // increment the time variable
		
		game.batch.draw(gameName, cx, cy, NAME_WIDTH, NAME_HEIGHT);
		
		/* set the play button*/ 
		int x =1280/2-PLAY_WIDTH/2;
		if(Gdx.input.getX()>x && Gdx.input.getX()<x+PLAY_WIDTH && 728- Gdx.input.getY()>200 && 728- Gdx.input.getY()<200+PLAY_HEIGHT){
			// rather than adding a whole new image, if the game detects that we're hovering over
			// it draws the button in a different colour
			game.batch.setColor(0,1,0,1);
			game.batch.draw(playButton, ((1280/2)-(PLAY_WIDTH/2)), 200, PLAY_WIDTH, PLAY_HEIGHT);
			game.batch.setColor(1,1,1,1);
			
			if(Gdx.input.isTouched()){
				game.setScreen(new GameStartScreen(game));
			}
		}
		else{
			game.batch.draw(playButton, ((1280/2)-(PLAY_WIDTH/2)), 200, PLAY_WIDTH, PLAY_HEIGHT);
		}
		
		/* set the exit button*/
		int a =1280/2-EXIT_WIDTH/2;
		if(Gdx.input.getX()>a && Gdx.input.getX()<a+EXIT_WIDTH && 728-Gdx.input.getY()>100 && 728-Gdx.input.getY()<100+EXIT_HEIGHT){
			// see comments under the play button
			game.batch.setColor(0,1,0,1);
			game.batch.draw(exitButton, 1280/2-EXIT_WIDTH/2, 100, EXIT_WIDTH, EXIT_HEIGHT);
			game.batch.setColor(1,1,1,1);
			
			if(Gdx.input.isTouched()){
				Gdx.app.exit();

			}
		}
		else{
			game.batch.draw(exitButton, 1280/2-EXIT_WIDTH/2, 100, EXIT_WIDTH, EXIT_HEIGHT);

		}
		
		TextureRegion flameEyeFrame = flameEyeAnim.getKeyFrame((float) menuTimer, true);
		
		TextureRegion pcFrame = playerCharacterAnim.getKeyFrame((float) menuTimer, true);
		
		game.batch.draw(flameEyeFrame, 75, (float)(300 + (5 * Math.sin((float)menuTimer / 30))) );
		game.batch.draw(pcFrame, 950, 0);
		
		game.batch.end();

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}
}