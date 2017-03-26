package com.game.screens;

import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;
import com.game.Application;

public class MainMenuScreen implements Screen {
	private static final int PLAY_WIDTH = 150;
	private static final int PLAY_HEIGHT = 100;
	private static final int EXIT_WIDTH = 150;
	private static final int EXIT_HEIGHT = 100;
	private static final int NAME_WIDTH = 400;
	private static final int NAME_HEIGHT = 200;
	
	
	Application game;
	Texture playButtonActive;
	Texture playButtonInactive;
	Texture exitButtonActive;
	Texture exitButtonInactive;
	Texture gameName;
	Texture background;

	
	public MainMenuScreen(Application game){
		this.game = game;
		playButtonActive = new Texture("play_button_active.png");
		playButtonInactive = new Texture("play_button_inactive.png");
		exitButtonActive = new Texture("exit_button_active.png");
		exitButtonInactive = new Texture("exit_button_inactive.png");
		gameName = new Texture("game_title.png");
		background = new Texture("Village_Terrain.png");

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
		game.batch.draw(background, 0, 0, 1280, 728);
		/*set game name*/
		game.batch.draw(gameName, 1280/2-NAME_WIDTH/2, 460, NAME_WIDTH, NAME_HEIGHT);
       /* set the play button*/ 
		int x =1280/2-PLAY_WIDTH/2;
		if(Gdx.input.getX()>x && Gdx.input.getX()<x+PLAY_WIDTH && 728- Gdx.input.getY()>200 && 728- Gdx.input.getY()<200+PLAY_HEIGHT){
		game.batch.draw(playButtonActive, 1280/2-PLAY_WIDTH/2, 200, PLAY_WIDTH, PLAY_HEIGHT);
		if(Gdx.input.isTouched()){
			game.setScreen(new GameStartScreen(game));
		}
		}
		else{
			game.batch.draw(playButtonInactive, 1280/2-PLAY_WIDTH/2, 200, PLAY_WIDTH, PLAY_HEIGHT);
		}
		/* set the exit button*/
		int a =1280/2-EXIT_WIDTH/2;
		if(Gdx.input.getX()>a && Gdx.input.getX()<a+EXIT_WIDTH && 728-Gdx.input.getY()>100 && 728-Gdx.input.getY()<100+EXIT_HEIGHT){
			game.batch.draw(exitButtonActive, 1280/2-EXIT_WIDTH/2, 100, EXIT_WIDTH, EXIT_HEIGHT);
			if(Gdx.input.isTouched()){
				Gdx.app.exit();
				
			}
			}
			else{
				game.batch.draw(exitButtonInactive, 1280/2-EXIT_WIDTH/2, 100, EXIT_WIDTH, EXIT_HEIGHT);
		
			}
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