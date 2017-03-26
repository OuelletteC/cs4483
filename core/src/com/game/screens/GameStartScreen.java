package com.game.screens;

import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.TimeUtils;
import com.game.Application;

public class GameStartScreen implements Screen{
	Application game;
	Texture gameStartTxt;
	private static final int START_WIDTH = 500;
	private static final int START_HEIGHT = 200;
	private boolean DEBUG = true;
	private float elapsed;
	public GameStartScreen(Application game){
		this.game=game;
		gameStartTxt = new Texture("game_start.png");
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		//count the time
        elapsed += delta;
        //set the screen
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.batch.begin();
		game.batch.draw(gameStartTxt, 1280/2-START_WIDTH/2, 269, START_WIDTH, START_HEIGHT);
		game.batch.end();
		//delay for 2 seconds
		if(elapsed >2.0)
        game.setScreen(new StageStartScreen(game,1));


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
