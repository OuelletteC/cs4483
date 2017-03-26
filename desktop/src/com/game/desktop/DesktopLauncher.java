package com.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.game.Application;
import com.game.screens.PlayScreen;;

public class DesktopLauncher {
	
	private static int xReso = 1280;
	private static int yReso = 730;
	
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = xReso;
        config.height = yReso;
        config.title = ("Polarity");
        config.backgroundFPS = 60;
        config.foregroundFPS = 60;
		new LwjglApplication(new Application(), config);
	}

}
