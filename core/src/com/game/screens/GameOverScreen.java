package com.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.game.Application;

public class GameOverScreen implements Screen{

	Application game;
	Texture gameOverTxt;
	private static final int GAME_OVER_WIDTH = 500;
	private static final int GAME_OVER_HEIGHT = 200;

	private float elapsed;
	public GameOverScreen(Application game){
		this.game=game;
		gameOverTxt = new Texture("game_over.png");
	}


	@Override
	public void render(float delta) {
		//count the time
        elapsed += delta;
        //set the screen
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.batch.begin();
		game.batch.draw(gameOverTxt, 1280/2-GAME_OVER_WIDTH/2, 269, GAME_OVER_WIDTH, GAME_OVER_HEIGHT);
		game.batch.end();
		//delay for 2 seconds
		if(elapsed >2.0)
        game.setScreen(new MainMenuScreen(game));


}
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
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
