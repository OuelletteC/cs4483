package com.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.game.Application;

public class StageStartScreen implements Screen{

	Application game;
	Texture stage1Txt;
	Texture stage2Txt;
	Texture stage3Txt;
	Texture stage4Txt;
	private static final int STAGE_TXT_WIDTH = 500;
	private static final int STAGE_TXT_HEIGHT = 200;
	private float elapsed;
	private int count;
	private boolean DEBUG = true;
	public StageStartScreen(Application game, int count){
		this.game=game;
		this.count=count;
		stage1Txt = new Texture("GameStartScreens/Stage1.png");
		stage2Txt = new Texture("GameStartScreens/Stage2.png");
		stage3Txt = new Texture("GameStartScreens/Stage3.png");
		stage4Txt = new Texture("GameStartScreens/Stage4.png");
	}


	@Override
	public void render(float delta) {
		//count the time
        elapsed += delta;
        //set the screen
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.batch.begin();
		switch(count){
		case 1:	game.batch.draw(stage1Txt, 1280/2-STAGE_TXT_WIDTH/2, 269, STAGE_TXT_WIDTH, STAGE_TXT_HEIGHT);
		        break;
		case 2:	game.batch.draw(stage2Txt, 1280/2-STAGE_TXT_WIDTH/2, 269, STAGE_TXT_WIDTH, STAGE_TXT_HEIGHT);
		        break;
		case 3:	game.batch.draw(stage3Txt, 1280/2-STAGE_TXT_WIDTH/2, 269, STAGE_TXT_WIDTH, STAGE_TXT_HEIGHT);
		        break;
		case 4:	game.batch.draw(stage4Txt, 1280/2-STAGE_TXT_WIDTH/2, 269, STAGE_TXT_WIDTH, STAGE_TXT_HEIGHT);
        		break;
		}

		game.batch.end();
		//delay for 2 seconds
		if(elapsed >2.0)
        game.setScreen(new PlayScreen(DEBUG,count, game));


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
